(ns fn-fx.render-core
  (:require [fn-fx.diff :as diff]
            [fn-fx.util :as util]
            [fn-fx.util.reflect-utils :as ru]
            [clojure.string :as str])
  (:import (javafx.embed.swing JFXPanel)
           (javax.swing JFrame)
           (java.lang.reflect Constructor Method Parameter Modifier)
           (javafx.scene.layout StackPane VBox)
           (javafx.event EventHandler)
           (java.io Writer)))

(JFXPanel.)

(declare ctor-fn)
(declare get-setter)
(declare get-getter)
(declare get-static-setter)

(defmulti set-property (fn [control prop val]
                         [(type control) prop]))


(deftype DefaultValue [])
(def default-value (->DefaultValue))

(def ^:dynamic *handler-fn*)

(defmulti construct-control identity)

(defmethod construct-control :default
  [tp-kw]
  (let [class (Class/forName (name tp-kw))
        f (ctor-fn class)]
    (defmethod construct-control tp-kw
      [_]
      (f))
    (f)))

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
                 (get-static-setter prop)
                 (get-setter (type this) prop))]

    (defmethod set-property [(type this) prop]
      [this _ val]
      (set-fn this val))

    (set-fn this val)))

(defn get-property [this kw]
  ((get-getter (type this) kw) this))

(defmulti convert-value (fn [from to-type]
                          [(type from) to-type]))

(defmethod convert-value :default
  [value ^Class tp]
  (assert (.isAssignableFrom tp (type value)) (str "Can't convert " (pr-str value) " of type " (type value)  " to " tp))
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

(defn component-impl [type args]
  `(diff/component
     ~type
     ~args))


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
  (let [ctors (get-value-ctors type)
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
        _ (assert ctor (str "No ctor for class " k))]
    (.setAccessible ctor true)
    (fn []
      (let [^objects arr (into-array Object (map
                                              (fn [^Parameter p]
                                                (convert-value default-value (.getType p)))
                                              (.getParameters ctor)))]
        (.newInstance ctor arr)))))

(defn get-setter [^Class klass prop]
  (let [prop-name      (str "set" (util/kabob->class (name prop)))
        ^Method method (->> (.getMethods klass)
                            (filter #(= prop-name (.getName ^Method %)))
                            (filter #(= 1 (count (.getParameters ^Method %))))
                            first)
        _ (assert method (str "No property " prop " on type " klass))
        to-type        (.getType ^Parameter (first (.getParameters method)))]
    (.setAccessible method true)
    (fn [inst val]
      (let [^objects arr (make-array Object 1)]
        (aset arr 0 (convert-value val to-type))
        (.invoke method inst arr)))))

(defn get-static-setter [prop]
  (let [^Class klass (->> ru/all-javafx-types
                  (filter
                    (fn [^Class klass]
                      (str/ends-with? (.getName klass) (str "." (util/kabob->class (namespace prop))))))
                  first)
        ^Method method (->> (.getMethods klass)
                    (filter
                      (fn [^Method m]
                        (and (= (.getName m) (str "set" (util/kabob->class (name prop))))
                             (Modifier/isStatic (.getModifiers m))
                             (= (.getParameterCount m) 2))))
                    first)
        to-type (.getType ^Parameter (second (.getParameters method)))]
    (.setAccessible method true)
    (fn [inst val]
      (let [^objects arr (make-array Object 2)]
        (aset arr 0 inst)
        (aset arr 1 (convert-value val to-type))
        (.invoke method inst arr)))))

(defn get-getter [^Class klass prop]
  (let [prop-name      (str "get" (util/kabob->class (name prop)))
        ^Method method (->> (.getMethods klass)
                            (filter #(= prop-name (.getName ^Method %)))
                            (filter #(zero?  (count (.getParameters ^Method %))))
                            first)
        to-type        (.getReturnType method)
        arr (make-array Object 0)]
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

(defmethod set-property [javafx.stage.Window :shown]
  [^javafx.stage.Window w _ val]
  (println "WHO")
  (if val
    (.show w)
    (.hide w)))

;; Value Converters

(defmethod convert-value [DefaultValue javafx.scene.Parent]
  [_ _]
  (javafx.scene.layout.VBox.))

(defmethod convert-value [clojure.lang.ILookup EventHandler]
  [template _]
  (let [handler-fn *handler-fn*]
    (reify EventHandler
      (handle [this event]
        (future
          (handler-fn template))))))

(defmethod convert-value [Long Integer]
  [v _]
  (int v))

(defmethod convert-value [Double Double/TYPE]
  [v _]
  (double v))