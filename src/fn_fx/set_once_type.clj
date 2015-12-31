(ns fn-fx.set-once-type
  (:import (clojure.lang ILookup)))


(defprotocol ISetOnce
  (set-once! [this k v]))

(defmacro defquasitype [tp-name fields & protos]
  {:pre [(symbol? tp-name)
         (every? symbol? fields)]}
  (let [fields (mapv (fn [sym]
                       (with-meta sym
                                  (assoc (meta sym) :volatile-mutable true)))
                     fields)
        v-sym  (gensym "v")]
    (println fields)
    `(deftype ~tp-name ~fields
       ISetOnce
       (set-once! [this# k# ~v-sym]
         (assert (not (k# this#)) (str k# " is already set, can only set values once"))
         (case k#
           ~@(mapcat
               (fn [x]
                 (println x (type x) (name x))
                 `[~(keyword (name x))
                   (~'set! ~x ~v-sym)])
               fields))
         this#)

       ILookup
       (~'valAt [this# k# default#]
         (case k#
           ~@(mapcat
               (fn [x]
                 `[~(keyword (name x))
                   ~x])
               fields)
           default#))
       (~'valAt [this# k#]
         (.valAt this# k# nil))
       ~@protos)))
