(ns fn-fx.render
  (:import (javafx.embed.swing JFXPanel)
           (javafx.scene Scene)
           (javafx.stage Stage)
           (java.lang.reflect Method Modifier)
           (javafx.collections ObservableList)
           (javafx.event EventHandler)
           (javafx.collections FXCollections)
           (java.util WeakHashMap))
  (:require [fn-fx.util :as util]
            [fn-fx.diff :as diff]))

(set! *warn-on-reflection* false)

(JFXPanel. )

(def constructors (atom {}))
(def types (atom {}))

(def ^:dynamic *log* false)

(defn log [form]
  (when *log*
    (println form)))

(declare create-component)
(declare convert-observable-list)
(declare create-event-handler)

(def converter-code (atom (partition-all 2 [Integer/TYPE `int
                                            java.lang.Integer `int
                                            Double/TYPE `double
                                            java.lang.Double `double
                                            Boolean/TYPE `boolean
                                            java.lang.Boolean `boolean
                                            java.lang.String `str
                                            EventHandler `create-event-handler
                                            ObservableList `convert-observable-list])))

(defmacro import-components [& components]
  `(do ~@(for [component components]
           (let [builder-name (symbol (str (name component)))
                 kw-name (->> (.lastIndexOf (name component) ".")
                              inc
                              (subs (name component))
                              keyword)
                 ]
             `(do (clojure.core/import ~component)
                  (swap! converter-code conj [~component `create-component])
                  (swap! constructors assoc ~kw-name ~builder-name)
                  (swap! types assoc ~kw-name ~component))))))

(import-components
  javafx.stage.Stage
  javafx.stage.Window
  javafx.scene.Scene
  javafx.scene.Parent
  javafx.scene.control.Button
  javafx.scene.control.Label
  javafx.scene.control.ListView
  javafx.scene.control.TextField
  javafx.scene.control.PasswordField
  javafx.scene.control.MultipleSelectionModel
  javafx.scene.layout.VBox
  javafx.scene.layout.HBox
  javafx.scene.layout.StackPane
  javafx.scene.layout.GridPane
  javafx.scene.text.Font
  javafx.scene.text.Text
  javafx.geometry.Insets
  javafx.scene.control.Separator
  javafx.scene.shape.Rectangle
  javafx.scene.control.SplitPane
  javafx.scene.layout.BorderPane
  javafx.scene.text.TextFlow)

(def get-enum-converter
  (memoize (fn [^Class to-tp]
             (let [clauses (mapcat
                             (fn [o]
                               `[~(keyword (.toLowerCase ^String (str o)))
                                 ~(symbol (.getName to-tp) (str o))])
                             (.getEnumConstants to-tp))
                   fn-name (symbol (str "-enum-converter-" (munge (.getName to-tp))))
                   form `(fn ~fn-name [v#]
                           (case v#
                             ~@clauses
                             :else (assert false (str "Bad enum value " v#))))]
               (log form)
               (eval form)))))

(def get-converter
  (memoize (fn [^Class to-tp]
             (if (.isEnum to-tp)
               `(get-enum-converter ~to-tp)
               (first (keep (fn [[^Class klass converter]]

                              (when (.isAssignableFrom to-tp klass)
                                converter))
                            @converter-code))))))

(defn find-getters
  [tp]
  (for [m (.getMethods tp)
        :let [mn (.getName m)]
        :when (= 0 (.getParameterCount m))
        :when (or (.startsWith mn "is") (.startsWith mn "get"))
        :let [prop-name (cond
                          (.startsWith mn "is") (.substring mn 2)
                          (.startsWith mn "get") (.substring mn 3))]]
    {:prop   (apply str (Character/toLowerCase ^Character (first prop-name)) (rest prop-name))
     :getter {:name mn :ret-type (.getName (.getReturnType ^Method m))}}))

(defn find-setters
  [tp]
  (for [m (.getMethods tp)
        :when (= 1 (.getParameterCount m))
        :let [mn (.getName m)]
        :when (.startsWith mn "set")
        :when (= Void/TYPE (.getReturnType m))
        :let [prop-name (.substring mn 3)
              ptype (aget (.getParameterTypes m) 0)]]
    {:prop   (apply str (Character/toLowerCase ^Character (first prop-name)) (rest prop-name))
     :setter {:name mn :param-type (.getName ptype)}}))

; TODO: Make sure you're always feeding the arguments from the spec to the constructor in the right order.
; TODO: Hook this up with the main fn-fx stuff, in place of builders.
; TODO: Make sure all args to constructors pass through conversion.
; TODO: Probably needs something to handle constructor var-args too (see KeyFrame for an example of what I mean)
(defn constructor
  [tp]
  (assert (not= nil tp) "The template provided to the constructor must not be nil.")

  (assert (not (empty? (.getConstructors tp))) (str "There is no public constructor for: " tp))
  (let [smallest (apply min-key (memfn getParameterCount) (.getConstructors tp))
        ctor (symbol (.getName smallest))
        params (into {} (map-indexed (fn [idx anns]
                                       (let [ann (first (filter
                                                          (fn [ann] (= javafx.beans.NamedArg (.annotationType ann)))
                                                          anns))]
                                         [idx (keyword (.value ann))]))
                                     (.getParameterAnnotations smallest)))
        _ (println params)
        form `(fn [template#]
                (let [{:keys [~@(map (comp symbol name) (vals params))]} template#]
                  (assert (every? #(not (nil? %)) (vector ~@(map (comp symbol name) (vals params))))
                          "A required parameter is missing from the provided template.")
                  (new ~ctor ~@(map (comp symbol name) (vals params)))))]
    (log form)
    (eval form)))

(def get-ctor
  (memoize
    (fn [nm]
      (let [ctor (constructor (@constructors nm))
            _ (assert ctor (str "No constructor for " nm))]
        ctor))))


(def ^:dynamic *id-map*)


(def get-setter
  (memoize
    (fn [tp]
      (assert tp)
      (let [^Class tp tp
            obj-sym (with-meta (gensym "obj")
                               {:tag (symbol (.getName tp))})
            val-sym (gensym "val")
            clauses (for [m (.getMethods tp)
                          :when (= (.getParameterCount ^Method m) 1)
                          :when (.startsWith (.getName ^Method m) "set")
                          :let [arg-type (aget (.getParameterTypes ^Method m) 0)]
                          :let [converter (get-converter arg-type)]
                          :let [prop-name (let [mn (subs (.getName ^Method m) 3)]
                                            (str (.toLowerCase (subs mn 0 1)) (subs mn 1)))]
                          :when converter]
                      `[~(keyword prop-name)
                        (. ~obj-sym
                           ~(symbol (.getName ^Method m))
                           ~(with-meta `(~converter ~val-sym)
                                       {:tag arg-type}))])

            form `(fn [~obj-sym property# ~val-sym]
                    (case property#
                      ~@(apply concat clauses)
                      (println "Unknown property" property# " on " ~tp)))]

        (log form)
        (eval form)))))

(def get-static-setter
  (memoize
    (fn [tp]
      (assert tp)
      (let [^Class tp tp
            child-sym (gensym "child")
            val-sym (gensym "val")
            clauses (for [m (.getMethods tp)
                          :when (Modifier/isStatic (.getModifiers ^Method m))
                          :when (= (.getParameterCount ^Method m) 2)
                          :when (.startsWith (.getName ^Method m) "set")
                          :let [arg-type0 (aget (.getParameterTypes ^Method m) 0)
                                arg-type1 (aget (.getParameterTypes ^Method m) 1)
                                converter (get-converter arg-type1)
                                prop-name (let [mn (subs (.getName ^Method m) 3)]
                                            (str (.toLowerCase (subs mn 0 1)) (subs mn 1)))]
                          :when converter]
                      `[~prop-name
                        ( ~(symbol (.getName tp)
                                    (.getName ^Method m))
                           ~(with-meta child-sym
                                       {:tag arg-type0})
                           ~(with-meta `(~converter ~val-sym)
                                       {:tag arg-type1}))])

            form `(fn [~child-sym property# ~val-sym]
                    (case (name property#)
                      ~@(apply concat clauses)
                      (println "Unknown static property" property# "on" ~tp)))]

        (log form)
        (eval form)))))

(def get-getter
  (memoize
    (fn [tp]
      (let [^Class tp tp
            obj-sym (with-meta (gensym "obj")
                               {:tag (symbol (.getName tp))})
            clauses (for [m (.getMethods tp)
                          :when (= (.getParameterCount ^Method m) 0)
                          :when (.startsWith (.getName ^Method m) "get")
                          :when (not (contains? #{"getClass"} (.getName ^Method m)))
                          :let [prop-name (let [mn (subs (.getName ^Method m) 3)]
                                            (str (.toLowerCase (subs mn 0 1)) (subs mn 1)))]]
                      `[~(keyword prop-name)
                        (. ~obj-sym
                           ~(symbol (.getName ^Method m)))])
            form `(fn [~obj-sym property#]
                    (case property#
                      ~@(apply concat clauses)
                      (println "Unknown property" property#)))]
        (log form)
        (eval form)))))

(def ^:dynamic *handler-atom*)

(defn create-event-handler [{:keys [include event-properties] :as template}]
  (let [handler-atom *handler-atom*
        id-map *id-map*]
    (reify EventHandler
      (handle [this event]
        (let [getter (get-getter (class event))
              target (getter event :target)
              target-getter (get-getter (class target))
              ^Scene scene (target-getter target :scene)
              msg (reduce
                    (fn [acc include]
                      (if (vector? include)
                        (let [[id & properties] include
                              value (reduce
                                      (fn [nd k]
                                        ((get-getter (class nd)) nd k))
                                      (.get ^WeakHashMap id-map id)
                                      properties)]
                          (assoc acc include value))
                        (assoc acc include (getter event include))))
                    template
                    include)
              msg (reduce
                    (fn [acc prop]
                      (assoc acc prop (getter event prop)))
                    msg
                    event-properties)]
          (future (@handler-atom msg)))))))

(defn convert-observable-list [itms]
  (FXCollections/observableArrayList ^java.util.Collection (mapv create-component itms)))

(def ignore-properties #{:type :fn-fx/children :fn-fx/id})

(defn create-component [component]
  (let [ctor (get-ctor (:type component))
        instance (ctor component)
        setter (get-setter (type instance))]
    (reduce-kv
      (fn [_ k v]
        (when (and (not (ignore-properties k))
                   (not (namespace k)))
          (setter instance k v)))
      nil
      component)
    (reduce-kv
      (fn [_ k v]
        (when (and (not (ignore-properties k))
                   (namespace k))
          (let [tp (->> k namespace keyword (get @types))]
            (assert tp (str "No static setter found for " (namespace k)))
            ((get-static-setter tp) instance (name k) v))))
      nil
      component)
    (when-let [id (:fn-fx/id component)]
      (.put ^WeakHashMap *id-map* id instance))
    instance))

(defprotocol IRunUpdate
  (-run-update [command control])
  (-run-indexed-update [command lst idx control]))

(declare run-updates)

(extend-protocol IRunUpdate
  fn_fx.diff.SetProperty
  (-run-update [{:keys [property-name value]} control]
    (if (identical? property-name :fn-fx/id)
      (do (.put ^WeakHashMap *id-map* value control)
          control)
      (let [f (get-setter (type control))]
        (assert f)
        (f control property-name value)
        control)))
  (-run-indexed-update [command lst idx control]
    (-run-update command control)))

(extend-protocol IRunUpdate
  fn_fx.diff.Child
  (-run-update [{:keys [property-name updates]} control]
    (let [f (get-getter (type control))
          child (f control property-name)]
      (run-updates child updates))))

(extend-protocol IRunUpdate
  fn_fx.diff.ListDelete
  (-run-indexed-update [_ lst idx control]
    (.remove ^java.util.List lst (int idx))))

(extend-type fn_fx.diff.Create
  IRunUpdate
  (-run-indexed-update [{:keys [template]} lst idx control]
    (.add ^java.util.List lst (int idx) (create-component template))
    nil))


(extend-protocol IRunUpdate
  fn_fx.diff.ListChild
  (-run-update [{:keys [property-name updates]} control]
    (let [f (get-getter (type control))
          ^java.util.List child-list (f control property-name)]
      (reduce
        (fn [_ [idx commands]]
          (if (set? commands)
            (reduce
              (fn [_ command]
                (if (<= (count child-list) idx)
                  (-run-indexed-update command child-list idx nil)
                  (-run-indexed-update command child-list idx (.get child-list (int idx)))))
              nil
              commands)
            (if (<= (count child-list) idx)
              (-run-indexed-update commands child-list idx nil)
              (-run-indexed-update commands child-list idx (.get child-list (int idx))))))
        nil
        updates)))
  (-run-indexed-update [this lst idx control]
    (-run-update this control)))

(defn run-updates [root commands]
  (reduce
    (fn [root command]
      (assert root)
      (-run-update command root))
    root
    commands))

(defprotocol IRoot
  (update! [this new-state])
  (diff [this new-state])
  (update-handler! [this new-handler])
  (show! [this]))


(deftype Root [state handler-atom root ids]
  IRoot
  (diff [this new-state]
    (diff/diff @state new-state))
  (update! [this new-state]
    (locking this
      (let [changes (time (diff this new-state))
            _ (println changes)]
        (vreset! state new-state)
        (log changes)
        (util/run-later
          (binding [*handler-atom* handler-atom
                    *id-map* ids]
            (time (run-updates root changes))))))
    this)
  (update-handler! [this new-handler]
    (reset! handler-atom new-handler))
  (show! [this]
    (util/run-and-wait
      (.show ^Stage root))))

(defn create-root [state]
  (let [handler (atom nil)
        ids (WeakHashMap.)
        r (util/run-and-wait
            (binding [*handler-atom* handler
                      *id-map* ids]
              (create-component state)))]
    (Root. (volatile! state) handler r ids)))

