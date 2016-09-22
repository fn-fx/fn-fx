(ns fn-fx.fx-dom
  (:require [fn-fx.render-core :as render-core]
            [fn-fx.diff :refer [IDom diff component]]
            [fn-fx.util :refer [run-and-wait run-later]])
  (:import (java.util List)))



(deftype FXDom [handler-fn]
  IDom
  (create-component! [this type]
    (run-and-wait
      (binding [render-core/*handler-fn* handler-fn]
        (render-core/construct-control type))))

  (set-property! [this node property value]
    (println "SET PROPERTY " node property value)
    (run-and-wait
      (binding [render-core/*handler-fn* handler-fn]
        (render-core/set-property node property value))))

  (set-child! [this parent k child]
    (println "Child " parent k child)
    (run-and-wait
      (render-core/set-property parent k child)))

  (set-indexed-child! [this parent k idx child]
    (run-and-wait
      (println "Set child")
      (let [^List lst (render-core/get-property parent k)]
        (assert (= idx (count lst)) "TODO: Implement this")
        (.add lst child))))

  (delete-indexed-child! [this parent k idx child]
    (run-and-wait
      (let [^List lst (render-core/get-property parent k)]
        (println "Removing " parent k idx)
        (println "LST " k parent (ancestors (type lst)) (count lst) idx)
        (assert (= idx (dec (count lst))) "TODO: Implement this")
        (.remove lst ^int idx)
        (println "AFTER" (count lst)))))
  (delete-component! [this node]
    nil))




(defrecord App [prev-state dom root handler-fn])

(defn default-handler-fn [data]
  (println "Unhandled event " data))

(defn app
  ([init-state]
    (app init-state default-handler-fn))
  ([init-state default-handler-fn]
   (let [dom  (->FXDom default-handler-fn)
         root (:node (diff dom nil init-state))]

     (->App init-state dom root default-handler-fn))))

(defn update-app [{:keys [prev-state dom root handler-fn]} new-state]
  (let [new-node (:node (time (diff dom prev-state new-state)))]
    (->App new-state dom new-node handler-fn)))


