(ns fn-fx.fx-dom
  (:require [fn-fx.render :as render]
            [fn-fx.diff :refer [IDom diff component]]
            [fn-fx.util :refer [run-and-wait run-later]])
  (:import (java.util List)))



(deftype FXDom [handler-fn]
  IDom
  (create-component! [this type spec]
    (run-and-wait
      (binding [render/*handler-fn* handler-fn]
        (render/create-component type spec))))

  (set-property! [this node property value]
    (let [setter (render/get-setter (type node))]
      (run-and-wait
        (binding [render/*handler-fn* handler-fn]
          (setter node property value)))))

  (set-child! [this parent k child]
    (let [setter (render/get-setter (type parent))]
      (run-and-wait
        (setter parent k child))))

  (set-indexed-child! [this parent k idx child]
    (let [getter (render/get-getter (type parent))]
      (run-and-wait
        (let [^List lst (getter parent k)]
          (assert (= idx (count lst)) "TODO: Implement this")
          (.add lst child)))))

  (delete-indexed-child! [this parent k idx child]
    (let [getter (render/get-getter (type parent))]
      (run-and-wait
        (let [^List lst (getter parent k)]
          (assert (= idx (dec (count lst))) "TODO: Implement this")
          (.remove lst idx))))))





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


