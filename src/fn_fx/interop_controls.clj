(ns fn-fx.interop-controls
  (:require [fn-fx.fx-dom :as dom]
            [fn-fx.render-core :as render-core])
  (:import (javafx.util Callback)
           (javafx.scene.control TableColumn TableCell)))


(defn cell-factory [ui]
  (reify Callback
    (call [this val]
      (let [ctl-fn (let [old-state (volatile! nil)]
                     (fn [state]
                       (if @old-state
                         (do (vreset! old-state (dom/update-app @old-state (ui state)))
                             @(:root @old-state))
                         (do (vreset! old-state (dom/app (ui state) render-core/*handler-fn*))
                             @(:root @old-state)))))]
        (proxy [TableCell] []
          (updateItem [item empty?]
            (proxy-super updateItem item empty?)
            (if empty?
              (do (.setGraphic this nil)
                  (.setText this nil))
              (.setGraphic this (ctl-fn item)))))))))
