(ns other-examples.table-example
  (:require [fn-fx.fx-dom :as dom]
            [fn-fx.controls :as ui]
            [fn-fx.diff :refer [component defui render should-update?]])
  (:import (javafx.scene.layout Priority)))



;; We use a multimethod for our event handling, dispatching by the event type.
(defmulti handle-event (fn [state event]
                         (:event event)))



(defmethod handle-event :default [state event]
  (println "Unknown event type " event))





(defn cell-value-factory [f]
  (reify javafx.util.Callback
    (call [this entity]
      (javafx.beans.property.ReadOnlyObjectWrapper. (f (.getValue entity))))))



(defui MyTable
       (render [this _]
               (ui/table-view
                 :items [{:name "foo" :age 10}
                         {:name "bar" :age 56}]
                 :columns [(ui/table-column
                             :text "Name"
                             :cell-value-factory (cell-value-factory #(:name %)))
                           (ui/table-column
                             :text "Age"
                             :cell-value-factory (cell-value-factory #(:age %)))])))


(defui MainWindow
       (render [this {:keys [current-value] :as state}]
               (my-table state)))


(defui MainStage
       (render [this args]
               (ui/stage
                 :title "Table Test"
                 :shown true
                 :scene (ui/scene
                          :root (main-window args)))))


(defn -main []
  (let [state (atom {})
        handler-fn (fn [event]
                     (try
                       (swap! state handle-event event)
                       (catch Exception e (.printStackTrace e))))
        ui-state (atom (dom/app (main-stage @state) handler-fn))]

    (add-watch state :ui (fn [k r os ns]
                           (swap! ui-state
                                  (fn [old-ui]
                                    (dom/update-app old-ui (main-stage ns))))))))




