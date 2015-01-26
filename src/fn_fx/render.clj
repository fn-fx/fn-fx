(ns fn-fx.render
  (:require [fn-fx.util :as util]
            [fn-fx.diff :as diff])
  (:import (javax.swing JFrame)
           (javafx.application Application)
           (javafx.scene.layout StackPaneBuilder VBox VBoxBuilder)
           (javafx.stage Stage StageBuilder)
           (javafx.scene Scene SceneBuilder Parent)
           (javafx.scene.control Button ButtonBuilder)
           (javafx.embed.swing JFXPanel)
           (java.lang.reflect Method)
           (javafx.collections ObservableList)
           (fn_fx.diff Create Child ListChild)))

(set! *warn-on-reflection* true)

(JFXPanel. )

(def constructors {:Stage StageBuilder
                   :Scene SceneBuilder
                   :Button ButtonBuilder
                   :StackPane StackPaneBuilder
                   :VBox VBoxBuilder})

(def types {:Stage Stage
            :Scene Scene
            :Button Button
            :StackPane StackPane
            :VBox VBox})

(declare create-component)
(declare convert-observable-list)


(def converter-code (partition-all 2 [Integer/TYPE `int
                                      Double/TYPE `double
                                      String `str
                                      Scene `create-component
                                      Parent `create-component
                                      ObservableList `convert-observable-list]))

(def get-converter
  (memoize (fn [^Class to-tp]
             (first (keep (fn [[^Class klass converter]]
                            (when (.isAssignableFrom to-tp klass)
                              converter))
                          converter-code)))))

(def get-builder
  (memoize
    (fn [nm]
      (let [form `(fn [] (. ~(constructors nm) create))]
        (println form)
        (eval form)))))



(def get-builder-setter
  (memoize
    (fn [tp]
      (let [^Class ctor (constructors tp)
            builder-sym (with-meta (gensym "builder")
                                   {:tag (symbol (.getName ctor))})
            val-sym (gensym "val")
            clauses (for [m (.getMethods ctor)
                          :when (= (.getParameterCount ^Method m) 1)
                          :when (not (#{"applyTo"} (.getName ^Method m)))
                          :let [arg-type (aget (.getParameterTypes ^Method m) 0)]
                          :let [converter (get-converter arg-type)]
                          :when converter]
                      `[~(keyword (.getName ^Method m))
                        (. ~builder-sym
                           ~(symbol (.getName ^Method m))
                           ~(with-meta `(~converter ~val-sym)
                                       {:tag arg-type}))])
            form `(fn [~builder-sym property# ~val-sym]
                    (println "Setting " property#)
                    (case property#
                      ~@(apply concat clauses)
                      (println "Unknown property" property#)
                      #_(assert false (str "Unknown property" {:property-name property#}))))]
        (binding [*print-meta* true]
          (println form))
        (eval form)))))

(def get-setter
  (memoize
    (fn [tp]
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
                    (println "Setting " property#)
                    (case property#
                      ~@(apply concat clauses)
                      (println "Unknown property" property#)))]
        (binding [*print-meta* true]
          (println form))
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
                    (println "Setting " property#)
                    (case property#
                      ~@(apply concat clauses)
                      (println "Unknown property" property#)))]
        (binding [*print-meta* true]
          (println form))
        (eval form)))))

(get-getter Stage)

(def get-builder-build-fn
  (memoize
    (fn [tp]
      (let [^Class ctor (constructors tp)
            builder-sym (with-meta (gensym "builder")
                                   {:tag (symbol (.getName ctor))})
            form `(fn [~builder-sym]
                    (.build ~builder-sym))]
        (println form)
        (eval form)))))

(get-builder-setter :Scene)

(defn convert-observable-list [itms]
  (println "items..." itms)
  (map create-component itms))

(defn create-component [component]
  (let [tp (:type component)
        builder ((get-builder tp))
        setter (get-builder-setter tp)]
    (reduce-kv
      (fn [builder k v]
        (if (= k :type)
          builder
          (do (setter builder k v)
              builder)))
      builder
      component)
    ((get-builder-build-fn tp) builder)))




(defprotocol IRunUpdate
  (-run-update [command control])
  (-run-indexed-update [command lst idx control]))

(declare run-updates)

(extend-protocol IRunUpdate
  (type (diff/->SetProperty nil nil))
  (-run-update [{:keys [property-name value]} control]
    (let [f (get-setter (type control))]
      (assert f)
      (f control property-name value)
      control))
)

(extend-protocol IRunUpdate

  (type (diff/->Child nil nil))
  (-run-update [{:keys [property-name updates]} control]
    (let [f (get-getter (type control))
          child (f control property-name)]
      (run-updates child updates))))

(extend-type fn_fx.diff.Create
  IRunUpdate
  (-run-indexed-update [{:keys [template]} lst idx control]
    (.add ^java.util.List lst (int idx) (create-component template))
    nil))


(extend-protocol IRunUpdate

  (type (diff/->ListChild nil nil))
  (-run-update [{:keys [property-name updates]} control]
    (let [f (get-getter (type control))
          ^java.util.List child-list (f control property-name)]
      (reduce
        (fn [_ [idx commands]]
          (if (set? commands)
            (assert false)
            (if (<= (count child-list) idx)
              (-run-indexed-update commands child-list idx nil)
              (-run-indexed-update commands child-list idx (.get child-list (int idx))))))
        nil
        updates))))

(defn run-updates [root commands]
  (reduce
    (fn [root command]
      (assert root)
      (println root command)
      (-run-update command root))
    root
    commands))

(util/run-and-wait (assert false)
                   )

#_(util/run-and-wait
  (type (create-component {:type :Scene
                           :root {:type :Button
                                  :text "Hello World"}})))

(let [data {:type      :Stage
            :fn-fx/children #{:scene}
            :scene     {:type :Scene
                        :fn-fx/children #{:root}
                        :root {:type     :VBox
                               :fn-fx/children #{:children}
                               :children [
                                          ]}}
            :title     "Hello World"
            :minWidth  200
            :minHeight 200}
      data2 (update-in data [:scene :root :children] conj {:type :Button
                                                           :text "Hello World"})
      data2 (update-in data2 [:scene :root :children] conj {:type :Button
                                                           :text "Hello World"})
      _ (clojure.pprint/pprint data2)


      r (util/run-and-wait
          (create-component data))
      _ (util/run-and-wait
          (.show r))

      commands (diff/diff data data2)]
  (clojure.pprint/pprint commands )
  (util/run-and-wait
    (run-updates r commands))
  )


(comment
  (util/run-and-wait (println "starting"))




  (new javafx.scene.SceneBuilder))