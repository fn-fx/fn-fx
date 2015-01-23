(ns fn-fx.util
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
