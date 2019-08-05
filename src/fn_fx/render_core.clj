(ns fn-fx.render-core
  (:require [fn-fx.diff :as diff]
            [fn-fx.util :as util]
            [fn-fx.util.reflect-utils :as ru]
            [fn-fx.fx-tree-search :as tree-search]
            [clojure.string :as str])
  (:import (java.lang.reflect Constructor Method Parameter Modifier Field)
           (javafx.scene.layout StackPane VBox)
           (javafx.event EventHandler Event)
           (java.io Writer)
           (javafx.beans.value ObservableValue)
           (java.util WeakHashMap)
           (javafx.beans.value ChangeListener)
           (javafx.stage Window)))

(set! *warn-on-reflection* true)


(declare ctor-fn)
(declare get-setter)
(declare get-getter)
(declare get-static-setter)
(declare get-add-listener)

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
                 (if (= (namespace prop) "listen")
                   (get-add-listener (type this) prop)
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
                [(hash-map (interleave prop-names-kw prop-types))
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


(defn value-type-impl
  "t is type of value, args is map of properties"
  [t args]
  ;; to-do: We want to match constuctor using both (keys args)
  ;; and the type of the arg values but we have to try to convert the values
  ;; first since that is what the constructor will do. Let's not get too fancy,
  ;; just match on the first constuctor that can have it's values converted
  (let [ctors    (get-value-ctors t)
        args-set (set (keys args))
        selected (->> ctors
                      (keep
                        (fn [[k fn]]
                          (when (= (set k) args-set)
                            `(->Value ~t ~(mapv args k) ((get-value-ctors ~t) ~k))))))]

    ;; instead of above, selected should return several constructors matching the args-set
    ;; find the first map entry that has convert-value of arg-values matching prop-types.
    (assert selected (str "No constructor found for " (set (keys args))))
    ;; filter seleted to give first good constructor
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



(defn- get-prop-methods [^Class class prop]
  "Returns a vector of methods that need to be composed in order to get to the desired property.
  For example, for :selection-model.selected-item the methods \"getSelectionModel\" and \"selectedItemProperty\" will be returned (for the given class)."
  (let [empty-array (make-array Class 0)
        method-names (str/split (name prop) #"\.")
        last-index (dec (count method-names))
        method-names (map-indexed (fn [i method-name]
                                    (let [^String mn (util/kabob->camel method-name)]
                                      (if (= last-index i)
                                        (str mn "Property")
                                        (str "get" (Character/toUpperCase (.charAt mn 0)) (.substring mn 1)))))
                                  method-names)]

    (loop [class class
           method-names method-names
           result []]
      (if (empty? method-names)
        result
        (let [m (.getMethod class (first method-names) empty-array)]
          (recur (.getReturnType m) (rest method-names) (conj result m))))
      )))



(defn- invoke-comp [methods inst]
  (if (empty? methods)
    inst
    (invoke-comp (rest methods)
                 (.invoke ^Method (first methods) inst (make-array Class 0)))))



(defn get-add-listener [^Class class prop]
  (let [;prop-name   (str (util/kabob->camel (name prop)) "Property")
        ;empty-array (make-array Class 0)
        ;prop        (.getMethod class prop-name empty-array)
        methods (get-prop-methods class prop)]
    (fn [inst val]
      (let [^ObservableValue ob (invoke-comp methods inst)                    ;(.invoke ^Method prop inst empty-array)
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

(defn prop->getter-name [prop]
  (if (.endsWith (name prop) "?")
    (str "is" (util/kabob->class (str/replace (name prop) "?" "")))
    (str "get" (util/kabob->class (name prop)))))

(defn get-getter [^Class klass prop]
  (let [prop-name      (prop->getter-name prop)
        ^Method method (->> (.getMethods klass)
                            (filter #(= prop-name (.getName ^Method %)))
                            (filter #(zero? (count (.getParameters ^Method %))))
                            first)
        arr            (make-array Object 0)]
    (.setAccessible method true)
    (fn [inst]
      (.invoke method inst arr))))

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
