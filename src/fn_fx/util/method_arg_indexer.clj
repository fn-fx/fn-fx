(ns fn-fx.util.method-arg-indexer
  (:gen-class)
  (:require [clojure.string :as str]
            [fn-fx.util.reflect-utils :as ru])
  (:import (java.io FileInputStream File)
           (com.github.javaparser JavaParser)
           (com.github.javaparser.ast.body MethodDeclaration ConstructorDeclaration)
           (com.github.javaparser.ast.type PrimitiveType ReferenceType)))

;; This file generates the resources method-arg-fino.edn file needed
;; during the generation of controls.clj, It basically logs the argument
;; names of values




(defprotocol IGetType
  (get-type [this]))

(extend-protocol IGetType
  PrimitiveType
  (get-type [this]
    (str this))

  ReferenceType
  (get-type [this]
    (str this)))


(defn index-method-args []
  (println "Indexing JavaFX Source...")
  (println "WARNING! THIS SHOULD NOT HAPPEN DURING NORMAL FN(FX) USAGE.")
  (->> (file-seq (File. "/Users/tim/tmp/javafx"))
       (filter (fn [^File f]
                 (and (.isFile f)
                      (str/ends-with? (.getName f) ".java"))))
       (mapcat
         (fn [file]
           (with-open [fis (FileInputStream. file)]
             (->>
               (.getTypes (JavaParser/parse fis))
               (mapcat
                 (fn [ft]
                   (for [mem (.getMembers ft)]
                     #_(println mem)
                     (when (and (or (instance? MethodDeclaration mem)
                                    (instance? ConstructorDeclaration mem)))
                       {:type   (.getName ft)
                        :method (.getName mem)
                        :params (for [param (.getParameters mem)]
                                  {:param-name (.getName param)
                                   :param-type (let [tp (get-type (.getType param))]
                                                 (if (= tp "T")
                                                   "Object"
                                                   tp))})}))))
               (remove nil?)
               vec))))
       (reduce
         (fn [acc {:keys [type method params]}]
           (assoc acc [type method (mapv :param-type params)] (mapv :param-name params)))
         {})))

(defn -main []
  (spit "resources/method-arg-info.edn"
        (pr-str (index-method-args))))

(comment

  (-main)

  )