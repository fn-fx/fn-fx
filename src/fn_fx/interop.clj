(ns fn-fx.interop
  (:import (java.lang.reflect Method)))

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

(defn constructor
  [tp]
  (let [smallest (apply min-key (memfn getParameterCount) (.getConstructors tp))
        ctor (symbol (.getName smallest))
        params (into {} (map-indexed (fn [idx anns]
                                       (let [ann (first (filter
                                                          (fn [ann] (= javafx.beans.NamedArg (.annotationType ann)))
                                                          anns))]
                                         [idx (keyword (.value ann))]))
                                     (.getParameterAnnotations smallest)))
        form `(fn [template#]
                (let [{:keys [~@(map (comp symbol name) (vals params))]} template#]
                  (assert (every? #(not (nil? %)) (vector ~@(map (comp symbol name) (vals params))))
                          "A required parameter is missing from the provided template.")
                  (new ~ctor ~@(map (comp symbol name) (vals params)))))]
    (eval form)))
