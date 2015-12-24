(ns fn-fx.fx-dom
  (:require [fn-fx.render :as render]
            [fn-fx.diff2 :refer [IDom diff component]]
            [fn-fx.util :refer [run-and-wait run-later]]))



(deftype FXDom []
  IDom
  (create-component! [this type spec]
    (run-and-wait
      (render/create-component (assoc spec :type type))))

  (set-property! [this node property value]
    (let [setter (render/get-setter (type node))]
      (run-later
        (setter node property value)))))





(defrecord App [prev-state dom root])

(defn app [init-state]
  (let [dom (->FXDom)
        root (:node (diff dom nil init-state))]
    (run-and-wait
      (.show root))
    (->App init-state dom root)))

(defn update-app [{:keys [prev-state dom root]} new-state]
  (let [new-node (:node (time (diff dom prev-state new-state)))]
    (->App new-state dom new-node)))


