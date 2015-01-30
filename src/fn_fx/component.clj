(ns fn-fx.component
  (:require [clojure.core.memoize :as m]))

(def CACHE_SIZE (* 1024 10))

(defmacro defcomponent [nm args & body]
  `(def ~nm
     (m/fifo (fn ~args ~@body)
             :fifo/threshold ~CACHE_SIZE)))
