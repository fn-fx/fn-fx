(ns other-examples.tabpane
  (:require [fn-fx.fx-dom :as dom]
            [fn-fx.controls :as ui]
            [fn-fx.diff :refer [component defui render should-update?]]))


(defn handle-event [{:keys [selection-count] :as state} event]
  (let [tab-text (-> event :fn-fx/includes :my-tabpane :selection-model.selected-item.text)]
    (println "Tab selected: " tab-text "Selection count:" selection-count)
    (assoc state :selection-count (inc selection-count))))


(defui MainWindow
       (render [this {:keys [current-value] :as state}]
               (ui/tab-pane
                 :id :my-tabpane

                 ;; In JavaFX TabPane listeners are added to the underlying SelectionModel, not to the TabPane instance directly.
                 ;; fn-fx uses the dot notation to access to the SelectionModel instance
                 ;; :listen/selection-model.selected-item is equivalent to
                 ;; tabPane.getSelectionModel().selectedItemProperty().addListener(...)
                 :listen/selection-model.selected-item {:event         :tab-selected

                                                        ;; we can use the dot notation also for data delivered to the event handler
                                                        :fn-fx/include {:my-tabpane [:selection-model.selected-item.text]}}
                 :tabs [(ui/tab :text "Tab 1"
                                :content (ui/pane :min-width 640 :min-height 480))
                        (ui/tab :text "Tab 2"
                                :content (ui/pane :min-width 640 :min-height 480))
                        (ui/tab :text "Tab 3"
                                :content (ui/pane :min-width 640 :min-height 480))])))


(defui MainStage
       (render [this args]
               (ui/stage
                 :title "Tabpane Test"
                 :shown true
                 :scene (ui/scene
                          :root (main-window args)))))

(defn -main []
  (let [state (atom {:selection-count 0})
        handler-fn (fn [event]
                     (try
                       (swap! state handle-event event)
                       (catch Exception e (.printStackTrace e))))
        ui-state (atom (dom/app (main-stage @state) handler-fn))]

    (add-watch state :ui (fn [k r os ns]
                           (swap! ui-state
                                  (fn [old-ui]
                                    (dom/update-app old-ui (main-stage ns))))))))


