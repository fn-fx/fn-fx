(ns fn-fx.util
  (:require [clojure.string :as string])
  (:import (javafx.application Platform)))

(Platform/setImplicitExit false)

(defn -run-later [^java.lang.Runnable fn]
  (Platform/runLater fn))

(defmacro run-later [& body]
  `(-run-later
     (fn []
       (try ~@body
            (catch Throwable ex#
              (println ex#))))))

(defrecord WrappedException [ex])
(defn wrapped-exception? [o]
  (instance? WrappedException o))

(defmacro run-and-wait [& body]
  `(let [p# (promise)]
     (-run-later (fn []
                   (try (unquote-splicing (butlast body)) (deliver p# (unquote (last body))) (catch Throwable ex# (deliver p# (->WrappedException ex#))))))
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
    (clojure.string/join (map string/capitalize s))))

(alter-var-root #'kabob->class memoize)


(defn camel->kabob [from]
  (let [s (string/split (name from) #"(?=[A-Z])" )]
    (clojure.string/join "-" (map string/lower-case s))))

(alter-var-root #'camel->kabob memoize)

(defn upper->kabob [from]
  (let [s (string/split (name from) #"\_")]
    (clojure.string/join "-" (map string/lower-case s))))
