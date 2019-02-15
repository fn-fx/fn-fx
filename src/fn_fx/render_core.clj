(ns fn-fx.render-core
    (:require [fn-fx.diff :as diff]
            [fn-fx.util :as util]
            [fn-fx.util.reflect-utils :as ru]
            [fn-fx.fx-tree-search :as tree-search]
            [clojure.string :as str])
    (:import (javafx.embed.swing JFXPanel)
      (javax.swing JFrame)
      (java.lang.reflect Constructor Method Parameter Modifier Field)
      (javafx.scene.layout StackPane VBox)
      (javafx.event EventHandler Event)
      (java.io Writer)
      (javafx.beans.value ObservableValue)
      (java.util WeakHashMap)
      (javafx.beans.value ChangeListener)
      (javafx.stage Window) (javafx.beans.property Property)))

(set! *warn-on-reflection* true)

(JFXPanel.)

(declare ctor-fn)
(declare get-setter)
(declare get-getter)
(declare get-static-setter)
(declare get-add-listener)
(declare get-bind-fn)

(defmulti set-property (fn [control prop val]
                         [(type control) prop]))

(defmulti convert-value (fn [from to-type]
                          [(type from) to-type]))


(deftype DefaultValue [])
(def default-value (->DefaultValue))

(def ^:dynamic *handler-fn*)
(def construct-control nil)
(defmulti construct-control (fn [type]
                              (if (keyword? type)
                                type
                                (let [[tp args] type]
                                  [tp args]))))

(defmethod construct-control :default
  [tp-kw]
  (if (keyword? tp-kw)
    (let [class (Class/forName (name tp-kw))
          f     (ctor-fn class)]
      (defmethod construct-control tp-kw
        [_]
        (f))
      (f))
    (let [[tp arg-names arg-vals] tp-kw
          class       (Class/forName (name tp))
          {:keys [^Constructor method]} (->> (ru/get-value-ctors class)
                                             (filter
                                               (fn [{:keys [prop-names-kw is-ctor?
                                                            prop-types]}]
                                                 (and (= prop-names-kw arg-names)
                                                      is-ctor?
                                                      (= (count prop-types)
                                                         (count arg-vals)))))
                                             first)
          _           (assert method (str "No Ctor found for " tp " " arg-names))
          param-types (map #(.getType ^Parameter %)
                           (.getParameters method))]
      (defmethod construct-control [tp arg-names]
        [[_ _ vals]]
        (try
          (let [arr (to-array (map convert-value vals param-types))]
            (.newInstance method arr))
          (catch Throwable ex
            (throw (ex-info "Error constructing control"
                            {:ex        ex
                             :type      tp
                             :arg-names arg-names
                             :vals      vals})))))
      (construct-control tp-kw))))

(defn set-properties [object properties]
  (reduce-kv
    (fn [obj attr val]
      (when (not (identical? attr :type))
        (set-property obj attr val))
      obj)
    object
    properties))


(defmethod set-property :default
  [this prop val]
  (let [set-fn (if (namespace prop)
                 (case (namespace prop)
                       "listen" (get-add-listener (type this) prop)
                       "bind" (get-bind-fn (type this) prop)
                       (get-static-setter prop))
                 (get-setter (type this) prop))]

    (defmethod set-property [(type this) prop]
      [this _ val]
      (set-fn this val))

    (set-fn this val)))

(defn get-property [this kw]
  ((get-getter (type this) kw) this))


(defn register-keyword-conv [^Class tp]
  (let [values (->> (for [^Field f (.getDeclaredFields tp)
                          :when (Modifier/isPublic (.getModifiers f))
                          :when (Modifier/isStatic (.getModifiers f))
                          :when (= tp (.getType f))]
                      [(keyword (util/upper->kabob (.getName f))) (.get f nil)])
                    (into {}))]
    (defmethod convert-value [clojure.lang.Keyword tp]
      [val _]
      (let [r (get values val ::not-found)]
        (assert (not= r ::not-found)
                (str "No converter for keyword " val " to type " tp))
        r))
    values))

(defmethod convert-value :default
  [value ^Class tp]
  (if (not (.isAssignableFrom tp (type value)))
    (if (keyword? value)
      (do (register-keyword-conv tp)
          (convert-value value tp))
      (assert (.isAssignableFrom tp (type value)) (str "Can't convert " (pr-str value) " of type " (type value) " to " tp))))
  value)

(defmethod convert-value
  [java.lang.Long Double/TYPE]
  [value _]
  (double value))

(def child-properties (atom {}))
(def non-child-properties (atom {}))

(defn class-for-name [^String nm]
  (Class/forName nm))

(alter-var-root #'class-for-name memoize)

(defn component-impl [type args static-props]
  (let [p                (select-keys args static-props)
        unsorted-kw-args (set (keys p))
        arg-kws          (->> (ru/get-value-ctors (Class/forName (name type)))
                              (sort-by #(count (:prop-names-kw %)))
                              (filter
                                (fn [{:keys [prop-names-kw]}]
                                  (every? (set prop-names-kw) unsorted-kw-args)))
                              (map :prop-names-kw)
                              first)
        _                (when (seq p)
                           (assert arg-kws (str "No constructor with static args for " unsorted-kw-args)))]

    `(diff/component
       ~(if (seq p)
          [type arg-kws (mapv (fn [v]
                                (get p v `default-value)) arg-kws)]
          type)
       ~(not-empty (apply dissoc args unsorted-kw-args)))))


(defrecord Value [class-name args f])
(defmethod print-method Value
  [^Value v ^Writer w]
  (.write w (str "Value [" (:class-name v) " " (pr-str (:args v)) "]")))

(defn get-value-ctors [^Class klass]
  (let [ctors (for [{:keys [is-ctor? ^Executable method prop-names-kw prop-types]} (ru/get-value-ctors klass)]
                [prop-names-kw
                 (if is-ctor?
                   (fn [args]
                     (let [^objects casted (into-array Object (map convert-value
                                                                   args
                                                                   prop-types))]
                       (.newInstance ^Constructor method casted)))
                   (fn [args]
                     (let [^objects casted (into-array Object (map convert-value
                                                                   args
                                                                   prop-types))]
                       (.invoke ^Method method nil casted)))
                   )])]

    (defmethod convert-value
      [Value klass]
      [{:keys [args f]} _]
      (f args))

    (into {} ctors)))

(alter-var-root #'get-value-ctors memoize)


(defn value-type-impl [type args]
  (let [ctors    (get-value-ctors type)
        args-set (set (keys args))
        selected (->> ctors
                      (keep
                        (fn [[k fn]]
                          (when (= (set k) args-set)
                            `(->Value ~type ~(mapv args k) ((get-value-ctors ~type) ~k)))))
                      first)]
    (assert selected (str "No constructor found for " (set (keys args))))
    selected))



(defn ctor-fn [^Class k]
  (let [^Constructor ctor (->> (.getDeclaredConstructors k)
                               (sort-by #(count (.getParameters ^Constructor %)))
                               first)
        _                 (assert ctor (str "No ctor for class " k))]
    (.setAccessible ctor true)
    (fn []
      (let [^objects arr (into-array Object (map
                                              (fn [^Parameter p]
                                                (convert-value default-value (.getType p)))
                                              (.getParameters ctor)))]
        (.newInstance ctor arr)))))


(defn get-setter [^Class klass prop]
  (let [prop-name      (str "set" (util/kabob->class (str/replace (name prop) "?" "")))
        ^Method method (->> (.getMethods klass)
                            (filter #(= prop-name (.getName ^Method %)))
                            (filter #(= 1 (count (.getParameters ^Method %))))
                            first)
        _              (assert method (str "No property " prop " on type " klass))
        to-type        (.getType ^Parameter (first (.getParameters method)))]
    (.setAccessible method true)
    (fn [inst val]
      (let [^objects arr (make-array Object 1)]
        (aset arr 0 (convert-value val to-type))
        (.invoke method inst arr)))))

(defn get-static-setter [prop]
  (let [^Class klass   (->> ru/all-javafx-types
                            (filter
                              (fn [^Class klass]
                                (str/ends-with? (.getName klass) (str "." (util/kabob->class (namespace prop))))))
                            first)
        _              (assert klass (str "Couldn't find class for static property " prop))
        ^Method method (->> (.getMethods klass)
                            (filter
                              (fn [^Method m]
                                (and (= (.getName m) (str "set" (util/kabob->class (name prop))))
                                     (Modifier/isStatic (.getModifiers m))
                                     (= (.getParameterCount m) 2))))
                            first)
        to-type        (.getType ^Parameter (second (.getParameters method)))]
    (.setAccessible method true)
    (fn [inst val]
      (let [^objects arr (make-array Object 2)]
        (aset arr 0 inst)
        (aset arr 1 (convert-value val to-type))
        (.invoke method inst arr)))))

(def ^WeakHashMap listener-map (WeakHashMap.))

(defn get-listeners [^WeakHashMap mp inst]
  (if (.containsKey mp inst)
    (.get mp inst)
    (let [listeners (volatile! {})]
      (.put mp inst listeners)
      listeners)))

(defn gather-event-data
  "Given an instance object and and event, gather information from the template's
  include specs and add them to the template."
  [inst event {:keys [fn-fx/include] :as template}]
  (assoc template
    :fn-fx/includes
    (reduce-kv
      (fn [acc id props]
        (if-let [node (if (= id :fn-fx/event)
                        event
                        (tree-search/find-nearest-by-id inst (str id)))]
          (assoc acc id
                     (reduce
                       (fn [acc prop]
                         (assoc acc prop (get-property node prop)))
                       {}
                       props))
          acc))
      {}
      include)))


;; This is the first attempt to implement chaining method calls, which we need for listeners.
;; There is problem here. Since we use reflection each method is taken from the return type of the previous method.
;; But because of generics, we sometimes get Object as return type, instead of the actual class.
;; For instance the following works:
;;     (get-methods javafx.scene.control.TreeView ["getSelectionModel" "getSelectedItem"])
;; but this does not:
;;     (get-methods javafx.scene.control.TreeView ["getSelectionModel" "getSelectedItem" "getValue"])
;; Therefore we use direct dynamic invocation (see invoke-comp)
;(defn- get-methods [^Class class method-names]
;  "Returns a vector of methods that need to be composed in order to get to the desired property."
;  (let [empty-array (make-array Class 0)]
;
;    (loop [class class
;           method-names method-names
;           result []]
;      (if (empty? method-names)
;        result
;        (let [m (.getMethod class (first method-names) empty-array)
;              return-type (.getGenericReturnType m)]
;          (println "return-type" return-type)
;          (recur (.getReturnType m) (rest method-names) (conj result m))))
;      )))

(def ^:private empty-class-array (make-array Class 0))
(def ^:private empty-object-array (make-array Object 0))

;; TODO This could be optimized via a macro, if the keyword from which the method names come are literals
(defn invoke-comp [method-names obj]
  "Takes a sequence of methods and an instance and invokes a composition of the methods
  (each method is invoked on the result of the previous method."
  (loop [method-names method-names
         ^Object obj obj]
    (let [^Class klass (.getClass obj)
          ^Method method (.getMethod klass (first method-names) empty-class-array)
          result (.invoke method obj empty-object-array)
          remaining (rest method-names)]
      (if (empty? remaining)
        result
        (recur remaining result)))))




(defn- get-property-fn [^Class class prop]

       "Returns a function which when invoked with a node will return the property corresponding to prop."
       (let [method-names  (str/split (name prop) #"\.")
             last-index (dec (count method-names))
             method-names  (map-indexed (fn [i method-name]
                                            (let [^String mn (util/kabob->camel method-name)]
                                                 (if (= last-index i)
                                                   (str mn "Property")
                                                   (str "get" (Character/toUpperCase (.charAt mn 0)) (.substring mn 1)))))
                                        method-names)]
            #(invoke-comp method-names %)))



(defn get-add-listener [^Class class prop]
  (let [;prop-name   (str (util/kabob->camel (name prop)) "Property")
        ;empty-array (make-array Class 0)
        ;prop        (.getMethod class prop-name empty-array)
        ;methods       (get-methods class method-names)
        get-prop (get-property-fn class prop)]

    (fn [inst val]
      (let [^ObservableValue ob (get-prop inst)                    ;(.invoke ^Method prop inst empty-array)
            listeners           (get-listeners listener-map inst)
            handler-fn          *handler-fn*
            listener            (reify ChangeListener
                                  (^void changed [this ^ObservableValue ob old new]
                                    (let [val (gather-event-data inst ob val)]
                                      (handler-fn (assoc val :fn-fx.listen/new new
                                                             :fn-fx.listen/old old)))))]
        (when-let [old (get @listeners prop)]
          (.removeListener ob ^ChangeListener old))
        (vswap! listeners assoc prop listener)
        (.addListener ob listener)))))





(defn get-bind-fn [^Class clazz prop]

      "Returns a function which when invoked binds a property to another property "

      (let [;prop-name   (str (util/kabob->camel (name prop)) "Property")
            ;empty-array (make-array Class 0)
            ;prop        (.getMethod class prop-name empty-array)
            ;methods       (get-methods class method-names)
            get-prop (get-property-fn clazz prop)]

           (fn [inst val]
               (let [^Property src        (get-prop inst)
                     [target-id target-prop] val
                     target-inst (or
                                   (tree-search/find-nearest-by-id inst (str target-id))
                                   (throw (Exception. (str "node with id " target-id " not found"))))
                     get-target-prop (get-property-fn (class target-inst) target-prop)
                     ^ObservableValue target     (get-target-prop target-inst)]

                    (.bind src target)))))




(defn prop->getter-name [prop]
  (if (.endsWith (name prop) "?")
    (str "is" (util/kabob->class (str/replace (name prop) "?" "")))
    (str "get" (util/kabob->class (name prop)))))



(defn get-getter [^Class klass prop]

  "Takes a class and a keyword representing a property and returns a function which takes an instance of that class and returns its property value.
  Nested properties are supported via the dot notation, e.g. :selection-model.selected-item will result in getSelectionModel().getSelectedItem() beeing called on the instance."

  (let [class-arr       (make-array Class 0)
        props           (str/split (name prop) #"\.")
        method-names      (map prop->getter-name props)

        ;methods         (get-methods klass method-names)

        ;^Method method (->> (.getMethods klass)
        ;                    (filter #(= prop-name (.getName ^Method %)))
        ;                    (filter #(zero? (count (.getParameters ^Method %))))
        ;                    first)
        ]

    ;not sure why we need to make it accessible
    ;(.setAccessible method true)

    (fn [inst]
      (invoke-comp method-names inst))))

(defn register-enum-converter [^Class klass]
  (let [vals (into {}
                   (map (fn [o]
                          [(keyword (str/replace (.toLowerCase ^String (str o))
                                                 #"\_" "-"))
                           o]))
                   (.getEnumConstants klass))]
    (defmethod convert-value [clojure.lang.Keyword klass]
      [kw _]
      (if-some [result (vals kw)]
        result
        (throw (ex-info "Invalid Enum Value" {:class klass :value kw :supported vals}))))))

(doseq [enum ru/enum-classes]
  (register-enum-converter enum))

(defn register-value-converter [^Class klass]
  (doseq [c (conj (ancestors klass) klass)]
    (defmethod convert-value [Value c]
      [{:keys [args f]} _]
      (f args))))

;; Helpers

(let [^Method show-method (->> (.getDeclaredMethods Window)
                               (filter #(= "show" (.getName ^Method %)))
                               first)
      empty-array         (make-array Object 0)]
  ;; Reflection here, but this method is protected so what can we do?
  (.setAccessible show-method true)
  (defmethod set-property [javafx.stage.Window :shown]
    [^javafx.stage.Window w _ val]
    (if val
      (.invoke show-method w empty-array)
      (.hide w))))

;; Value Converters

(defmethod convert-value [DefaultValue javafx.scene.Parent]
  [_ _]
  (javafx.scene.layout.VBox.))

(defmethod convert-value [DefaultValue Double/TYPE]
  [_ _]
  0.0)



(defmethod convert-value [clojure.lang.ILookup EventHandler]
  [template _]
  (let [handler-fn *handler-fn*]
    (reify EventHandler
      (^void handle [this ^Event event]
        (future
          (handler-fn (gather-event-data (.getTarget event) event template))
          nil)))))

(defmethod convert-value [Long Integer]
  [v _]
  (int v))

(defmethod convert-value [Double Double/TYPE]
  [v _]
  (double v))

(defmethod convert-value [clojure.lang.Keyword java.lang.String]
  [v _]
  (str v))

(defmethod convert-value [Double Integer/TYPE]
  [v _]
  (int v))

(defmethod convert-value [Boolean Boolean/TYPE]
  [v _]
  (boolean v))
