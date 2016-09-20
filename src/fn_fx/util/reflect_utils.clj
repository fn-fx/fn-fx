(ns fn-fx.util.reflect-utils
  (:gen-class)
  (:require [clojure.string :as str]
            [fn-fx.util :as util]
            [clojure.edn :as edn]
            [clojure.java.io :as io])
  (:import (javafx.embed.swing JFXPanel)
           (javax.swing JFrame)
           (javafx.application Application)
           (java.io FileInputStream File InputStream)
           (java.lang.reflect Method Executable)
           (javafx.embed.swing JFXPanel)
           (org.reflections Reflections)
           (java.lang.reflect Constructor Parameter Modifier ParameterizedType)
           (org.reflections.scanners SubTypesScanner Scanner)
           (javafx.beans Observable)
           (javafx.beans.value WritableValue)
           (javafx.beans.binding ObjectExpression BooleanExpression LongExpression DoubleExpression)
           (javafx.event EventHandler Event)))

(JFXPanel.)


(def method-args (delay (edn/read-string (slurp (io/resource "method-arg-info.edn")))))

(defn unqualified-name [s]
  (last (str/split s #"\.")))

(defn get-arg-names [^Executable method]
  (let [class-name (unqualified-name (.getName (.getDeclaringClass method)))
        method-name (unqualified-name (.getName method))
        params (map #(-> ^Parameter % .getType .getName unqualified-name) (.getParameters method))]
    (@method-args [class-name method-name params])))

;; All the JavaFX types we may want to use. These will not be
;; inner classes, interface, builders, properties, or base classes
(time (def all-javafx-types
        (->> (map #(Class/forName %) (.getAllTypes (Reflections. "javafx" (into-array Scanner [(SubTypesScanner. false)]))))
             (concat (.getSubTypesOf (Reflections. "javafx" (into-array Scanner [(SubTypesScanner. false)])) Enum))
             (remove #(.isInterface ^Class %))
             (remove #(str/includes? (.getName ^Class %) "$"))
             (remove #(str/ends-with? (.getName ^Class %) "Builder"))
             (remove #(str/ends-with? (.getName ^Class %) "Property"))
             (remove #(str/ends-with? (.getName ^Class %) "Base"))
             (remove #(Modifier/isAbstract (.getModifiers ^Class %)))
             (filter #(Modifier/isPublic (.getModifiers ^Class %)))
             set)))


(def enum-classes
  (filter #(.isEnum %) all-javafx-types))

(defn class-properties
  "For a given class, return all the JavaFX observable properties"
  [^Class klass]
  (->> (conj (ancestors klass) klass)
       (mapcat
         (fn [t]
           (.getMethods t)))
       (filter
         (fn [p]
           (and (str/starts-with? (.getName p) "get")
                (< 3 (count (.getName p)))
                (not (Modifier/isStatic (.getModifiers p))))))
       (map
         (fn [m]
           (let [name         (subs (.getName m) 3)
                 generic-type (.getGenericReturnType m)
                 prop-type (.getReturnType m)]
             {:wrap-type         (.getReturnType m)
              :generic-prop-type (let [p (if (instance? ParameterizedType generic-type)
                                           (let [p (first (.getActualTypeArguments generic-type))]
                                             (if (instance? Class p)
                                               p
                                               prop-type))
                                           prop-type)]
                                   (condp #(.isAssignableFrom %1 %2) p
                                     DoubleExpression Double
                                     LongExpression Long
                                     BooleanExpression Boolean
                                     p))
              :method            m
              :name              name
              :accessor-name     (symbol (.getName m))
              :kw-name           (keyword (util/camel->kabob name))

              :prop-type         prop-type})))))



;; All the JavaFX components (classes with properties)
(def control-types
  (->> all-javafx-types
       (filter
         (fn [t]
           (pos? (count (class-properties t)))))
       (filter
         (fn [t]
           (->> (.getMethods t)
                (filter
                  #(str/starts-with? (.getName %) "set"))
                count
                pos?)))
       set))


;; All the JavaFX value types (classes with no properties)
(def value-types
  (->> all-javafx-types
       (remove
         (fn [t]
           (.isEnum t)))
       (remove
         (fn [t]
           (->> (.getMethods t)
                (filter
                  #(str/starts-with? (.getName %) "set"))
                count
                pos?)))
       set))


(defn static-method-ctors [^Class k]
  (for [m (.getMethods k)
        :when (Modifier/isStatic (.getModifiers m))
        :when (Modifier/isPublic (.getModifiers m))
        :when (pos? (count (.getParameters m)))
        :when (not (some #(.isAssignableFrom InputStream (.getType %)) (.getParameters m)))
        :when (= k (.getReturnType m))]
    {:method         m
     :is-ctor?       false
     :method-name    (symbol (.getName m))}))

(defn get-ctors [^Class k]
  (for [m (.getConstructors k)]
    {:method m
     :is-ctor? true
     :method-name (symbol (.getName k))}))

(defn get-value-ctors [^Class k]
  (for [{:keys [method] :as m} (concat (static-method-ctors k)
                                       (get-ctors k))
        :let [arg-names (get-arg-names method)
              prop-names (map util/camel->kabob arg-names)]]
    (assoc m
      :prop-names prop-names
      :prop-types      (mapv #(.getType %) (.getParameters method))
      :prop-names      (map symbol arg-names)
      :prop-names-kw  (mapv keyword prop-names)
      :prop-names-sym (mapv symbol prop-names))))



(defn get-control-ctor [^Class k]
  (->> (get-value-ctors k)
       (sort-by (comp count :prop-names))
       (filter :is-ctor?)
       first))
