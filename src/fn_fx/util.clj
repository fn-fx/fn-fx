(ns fn-fx.util
  (:require [clojure.string :as string])
  (:import (javafx.application Platform)))

(Platform/setImplicitExit false)

(defn -run-later [^java.lang.Runnable fn]
  (Platform/runLater fn))

(defmacro run-later [& body]
  `(-run-later (fn [] ~@body)))

(defrecord WrappedException [ex])
(defn wrapped-exception? [o]
  (instance? WrappedException o))

(defmacro run-and-wait [& body]
  `(let [p# (promise)]
     (-run-later (fn []
                   (do (try
                         ~@(butlast body)
                         (deliver p# ~(last body))
                         (catch Throwable ex#
                           (deliver p# (->WrappedException ex#)))

                         )
                       (println "Done"))))
     (let [v# @p#]
       (if (wrapped-exception? v#)
         (throw (:ex v#))
         v#))))


(defn kabob->camel [from]
  (let [s (string/split (name from) #"\-")]
    (apply str (first s) (map string/capitalize (next s)))))

(alter-var-root #'kabob->camel memoize)

(defn kabob->class [from]
  (let [s (string/split (name from) #"\-")]
    (apply str (map string/capitalize s))))

(alter-var-root #'kabob->class memoize)


(defn camel->kabob [from]
  (let [s (string/split (name from) #"(?=[A-Z])" )]
    (apply str (interpose "-" (map string/lower-case s)))))

(alter-var-root #'camel->kabob memoize)
