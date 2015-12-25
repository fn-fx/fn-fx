(ns fn-fx.fx-dom
  (:require [fn-fx.render :as render]
            [fn-fx.diff2 :refer [IDom diff component]]
            [fn-fx.util :refer [run-and-wait run-later]])
  (:import (java.util List)))



(deftype FXDom []
  IDom
  (create-component! [this type spec]
    (println "Create " type spec)
    (run-and-wait
      (render/create-component (assoc spec :type type))))

  (set-property! [this node property value]
    (println "Property " node property value)
    (let [setter (render/get-setter (type node))]
      (run-and-wait
        (setter node property value))))

  (set-child! [this parent k child]
    (println "Child " parent k child)
    (let [setter (render/get-setter (type parent))]
      (run-and-wait
        (setter parent k child))))

  (set-indexed-child! [this parent k idx child]
    (let [getter (render/get-getter (type parent))]
      (run-and-wait
        (println "Indexed Child" parent k idx child)
        (let [^List lst (getter parent k)]
          (assert (= idx (count lst)) "TODO: Implement this")
          (.add lst child)
          (println "done i"))))))





(defrecord App [prev-state dom root])

(defn app [init-state]
  (let [dom (->FXDom)
        root (:node (diff dom nil init-state))]

    (->App init-state dom root)))

(defn update-app [{:keys [prev-state dom root]} new-state]
  (let [new-node (:node (time (diff dom prev-state new-state)))]
    (->App new-state dom new-node)))


