(ns fn-fx.fx-dom
  (:require [fn-fx.render-core :as render-core]
            [fn-fx.diff :refer [IDom diff component]]
            [fn-fx.util :refer [run-and-wait run-later]])
  (:import (java.util List)))

(defn unwrap-promise [v]
  (if (instance? clojure.lang.IBlockingDeref v)
    @v
    v))

(deftype FXDom [handler-fn]
  IDom
  (create-component! [this type]
    (let [p (promise)]
      (run-later
        (binding [render-core/*handler-fn* handler-fn]
          (p (render-core/construct-control type))))
      p))

  (set-property! [this node property value]
    (run-later
      (binding [render-core/*handler-fn* handler-fn]
        (render-core/set-property (unwrap-promise node) property (unwrap-promise value)))))

  (set-child! [this parent k child]
    (run-later
      (render-core/set-property (unwrap-promise parent) k (unwrap-promise child))))

  (set-indexed-child! [this parent k idx child]
    (run-later
     (let [^List lst (render-core/get-property (unwrap-promise parent) k)]
       (.add lst idx (unwrap-promise child)))))

  (delete-indexed-child! [this parent k idx child]
    (run-later
     (let [^List lst (render-core/get-property (unwrap-promise parent) k)]
       (.remove lst (unwrap-promise child)))))

  (replace-indexed-child! [this parent k idx child]
    (run-later
     (let [^List lst (render-core/get-property (unwrap-promise parent) k)]
       (doto lst
         (.remove ^int idx)
         (.add ^int idx (unwrap-promise child)))))))

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
