(ns other-examples.treeview
  (:require [fn-fx.fx-dom :as dom]
            [fn-fx.controls :as ui]
            [fn-fx.diff :refer [component defui render should-update?]])
  (:import (javafx.scene.layout Priority)))



;; We use a multimethod for our event handling, dispatching by the event type.
(defmulti handle-event (fn [state event]
                         (:event event)))


(defmethod handle-event :node-selected [state {:keys [fn-fx/includes]}]
  (let [val (get-in includes [:my-tree :selection-model.selected-item.value])]

    (println val "selected")

    ; the return value is the changed state, but only because we wrote -main like this
    (assoc state :selected-value val)))


(defmethod handle-event :default [state event]
  (println "Unknown event type " event))


;; The data for our tree
(def tree-data ["Animals"
                ["Vertebrates" ["Fish"] ["Amphibians"] ["Reptiles"] ["Birds"] ["Mammals"]]
                ["Invertebrates" ["Insects"] ["Worms"] ["Snails"] ["Crabs & Lobsters"]]])




(defn- build-tree [node]
  (let [val (first node)
        children (rest node)]

    (if (empty? children)

      (ui/tree-item :value val)

      (ui/tree-item :value val
                    :children (doall (map build-tree children))))))


(defui TreeView
       (render [this {:keys [current-value] :as state}]
               (do
                 (ui/v-box
                   :children [(ui/tree-view
                                :id :my-tree
                                :root (build-tree tree-data)
                                :v-box/vgrow Priority/ALWAYS

                                ;; In JavaFX TreeView listeners are added to the underlying SelectionModel, not to the TreeView instance directly.
                                ;; fn-fx uses the dot notation to access to the SelectionModel instance
                                ;; :listen/selection-model.selected-item is equivalent to
                                ;; treeView.getSelectionModel().selectedItemProperty().addListener(...)
                                :listen/selection-model.selected-item {:event         :node-selected

                                                                       ;; here we want to receive the value of treeView.getSelectionModel().getSelectedItem().getValue()
                                                                       ;; in the event handler
                                                                       :fn-fx/include {:my-tree [:selection-model.selected-item.value]}}
                                )]))))



(defui MainWindow
       (render [this {:keys [current-value] :as state}]
               (ui/split-pane
                 :divider-positions (double-array [0.2])
                 :items [(tree-view state)])))


(defui MainStage
       (render [this args]
               (ui/stage
                 :title "TreeView Test"
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




