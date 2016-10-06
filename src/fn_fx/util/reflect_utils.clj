(ns fn-fx.util.reflect-utils
  (:gen-class)
  (:require [clojure.string :as str]
            [fn-fx.util :as util]
            [clojure.edn :as edn]
            [clojure.java.io :as io])
  (:import (javafx.embed.swing JFXPanel)
           (java.io FileInputStream File InputStream)
           (java.lang.reflect Method Executable)
           (javafx.embed.swing JFXPanel)
           (org.reflections Reflections)
           (java.lang.reflect Constructor Parameter Modifier ParameterizedType)
           (org.reflections.scanners SubTypesScanner Scanner)
           (javafx.beans.binding ObjectExpression BooleanExpression LongExpression DoubleExpression)
           (javafx.event EventHandler Event)))

(JFXPanel.)

(set! *warn-on-reflection* true)


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
(def all-javafx-types
  (->> (map #(Class/forName %) (.getAllTypes (Reflections. "javafx" (into-array Scanner [(SubTypesScanner. false)]))))
       (concat (.getSubTypesOf (Reflections. "javafx" (into-array Scanner [(SubTypesScanner. false)])) Enum))
       (remove #(.isInterface ^Class %))
       (remove #(str/includes? (.getName ^Class %) "$"))
       (remove #(str/ends-with? (.getName ^Class %) "Builder"))
       (remove #(str/ends-with? (.getName ^Class %) "Property"))
       (remove #(str/ends-with? (.getName ^Class %) "Base"))
       (remove #(Modifier/isAbstract (.getModifiers ^Class %)))
       (filter #(Modifier/isPublic (.getModifiers ^Class %)))
       set))


(def enum-classes
  (filter #(.isEnum ^Class %) all-javafx-types))

(defn class-properties
  "For a given class, return all the JavaFX observable properties"
  [^Class klass]
  (->> (conj (ancestors klass) klass)
       (mapcat
         (fn [t]
           (.getMethods ^Class t)))
       (filter
         (fn [p]
           (and (str/starts-with? (.getName ^Method p) "get")
                (< 3 (count (.getName ^Method p)))
                (not (Modifier/isStatic (.getModifiers ^Method p))))))
       (map
         (fn [^Method m]
           (let [name         (subs (.getName m) 3)
                 generic-type (.getGenericReturnType m)
                 prop-type (.getReturnType m)]
             {:wrap-type         (.getReturnType m)
              :generic-prop-type (let [p (if (instance? ParameterizedType generic-type)
                                           (let [p (first (.getActualTypeArguments ^ParameterizedType generic-type))]
                                             (if (instance? Class p)
                                               p
                                               prop-type))
                                           prop-type)]
                                   (condp #(.isAssignableFrom ^Class %1 %2) p
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
  (delay
    (->> all-javafx-types
         (filter
           (fn [t]
             (pos? (count (class-properties t)))))
         (filter
           (fn [^Class t]
             (->> (.getMethods t)
                  (filter
                    #(str/starts-with? (.getName ^Method %) "set"))
                  count
                  pos?)))
         set)))


;; All the JavaFX value types (classes with no properties)
(def value-types
  (delay
    (->> all-javafx-types
         (remove
           (fn [^Class t]
             (.isEnum t)))
         (remove
           (fn [^Class t]
             (->> (.getMethods t)
                  (filter
                    #(str/starts-with? (.getName ^Method %) "set"))
                  count
                  pos?)))
         set)))


(defn static-method-ctors [^Class k]
  (for [^Method m (.getMethods k)
        :when (Modifier/isStatic (.getModifiers m))
        :when (Modifier/isPublic (.getModifiers m))
        :when (pos? (count (.getParameters m)))
        :when (not (some #(.isAssignableFrom InputStream (.getType ^Parameter %)) (.getParameters m)))
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
  (for [{:keys [^Executable method] :as m} (concat (static-method-ctors k)
                                       (get-ctors k))
        :let [arg-names (get-arg-names method)
              prop-names (map util/camel->kabob arg-names)]]
    (assoc m
      :prop-names prop-names
      :prop-types      (mapv #(.getType ^Parameter %) (.getParameters method))
      :prop-names      (map symbol arg-names)
      :prop-names-kw  (mapv keyword prop-names)
      :prop-names-sym (mapv symbol prop-names))))



(defn get-control-ctor [^Class k]
  (->> (get-value-ctors k)
       (sort-by (comp count :prop-names))
       (filter :is-ctor?)
       first))

(defn get-static-control-properties [^Class k]
  (->> (get-value-ctors k)
       (mapcat :prop-names-kw)
       set
       (remove
         (fn [nm]
           (let [pnm (str "set" (util/kabob->class (name nm)))]
             (->> (.getMethods k)
                  (filter
                    (fn [^Method m]
                      (and (= (.getName m) pnm)
                           (= (.getParameterCount m) 1))))
                  first))))))
