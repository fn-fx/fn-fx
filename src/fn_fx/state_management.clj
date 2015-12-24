(ns fn-fx.state-management)


(defprotocol IQuery
  (query [this]))

(defprotocol IRenderable
  (render [this]))


(defn props [this])

(defmulti reader )

(defrecord Reconciler [state parser]
  )

(defrecord Button [props ])