(ns other-examples.todo
  (:require [fn-fx.fx-dom :as dom]
            [fn-fx.diff :refer [component defui render should-update?]]
            [fn-fx.controls :as ui]))

(def main-font (ui/font :family "Helvetica" :size 20))

(defui TodoItem
       (render [this {:keys [done? idx text]}]
         (ui/border-pane
           :padding (ui/insets
                      :top 10
                      :bottom 10
                      :left 0
                      :right 0)
           :left (ui/check-box
                      :font main-font
                      :text text
                      :selected done?
                      :on-action {:event :swap-status :idx idx})
           :right (ui/button :text "X"
                             :on-action {:event :delete-item :idx idx}))))

(defui MainWindow
       (render [this {:keys [todos]}]
          (ui/v-box
           :padding (ui/insets

                     :top-right-bottom-left 25)
           :children [(ui/text-field
                        :id ::new-item
                        :prompt-text "What needs to be done?"
                        :font main-font
                        :on-action {:event :add-item
                                    :fn-fx/include {::new-item #{:text}}})
                      (ui/v-box
                        :children (map-indexed
                                       (fn [idx todo]
                                         (todo-item (assoc todo :idx idx)))
                                       todos))])))


(defui Stage
       (render [this args]
         (ui/stage
           :title "ToDos"
           :min-height 600
           :shown true
           :scene (ui/scene
                    :root (main-window args)))))

(defmulti handle-event (fn [state event]
                          (:event event)))

(defmethod handle-event :swap-status
  [state {:keys [idx]}]
  (update-in state [:todos idx :done?] (fn [x]
                                         (not x))))

(defmethod handle-event :delete-item
  [state {:keys [idx]}]
  (update-in state [:todos] (fn [itms]
                              (println itms idx)
                              (vec (concat (take idx itms)
                                           (drop (inc idx) itms))))))

(defmethod handle-event :add-item
  [state {:keys [fn-fx/includes]}]
  (update-in state [:todos] conj {:done? false
                                  :text (get-in includes [::new-item :text])}))

(defn -main []
  (let [;; Data State holds the business logic of our app
        data-state (atom {:todos [{:done? false
                                   :text  "Take out trash"}]})

        ;; handler-fn handles events from the ui and updates the data state
        handler-fn (fn [event]
                     (try
                       (swap! data-state handle-event event)
                       (catch Throwable ex
                         (println ex))))

        ;; ui-state holds the most recent state of the ui
        ui-state   (agent (dom/app (stage @data-state) handler-fn))]

    ;; Every time the data-state changes, queue up an update of the UI
    (add-watch data-state :ui (fn [_ _ _ _]
                                (send ui-state
                                      (fn [old-ui]
                                        (try
                                          (dom/update-app old-ui (stage @data-state))
                                          (catch Throwable ex
                                            (println ex)))))))))

(comment
  (-main)

  )