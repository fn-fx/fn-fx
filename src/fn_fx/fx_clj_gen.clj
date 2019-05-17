(ns fn-fx.fx-clj-gen
  (:require [clojure.string :as str])
  (:import (javafx.application Application)
           (java.lang.reflect Constructor Parameter)
           (org.reflections.scanners SubTypesScanner Scanner)
           (org.reflections Reflections)
           (javafx.util Builder)
           ))



(defn get-obj-attrs [^Class klass]
  (let [ctor-method (into #{}
                          (comp
                            (mapcat
                              (fn [^Constructor ctor]
                                (.getParameters ctor)))
                            (map (fn [^Parameter p]
                                   (.getName p))))
                          (.getConstructors klass))]
    ctor-method))

(import 'javafx.scene.control.Button)
(get-obj-attrs javafx.scene.control.Button)

(defn scan-all []
  (let [ref (Reflections. "javafx" nil)
        builders (.getSubTypesOf ref Builder)]
    (vec (for [^Class builder builders
               :when (not (.startsWith (.getName builder)
                                       "javafx.fxml."))]
           (Class/forName (subs (.getName builder) 0
                                (- (count (.getName builder))
                                   (count "Builder"))))))))


(->> (map #(Class/forName %) (.getAllTypes (Reflections. "javafx" (into-array Scanner [(SubTypesScanner. false)]))))
     (remove #(str/includes? (.getName %) "$"))
     (remove #(str/includes? (.getName %) "Builder"))
     (remove #(.isEnum %))
     (remove #(try (.getConstructor ^Class % (into-array Class []))
                   (catch NoSuchMethodException ex
                     false))))

