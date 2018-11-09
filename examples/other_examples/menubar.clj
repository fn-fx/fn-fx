(ns other-examples.menubar
  (:require [fn-fx.fx-dom :as dom]
            [fn-fx.controls :as ui]
            [fn-fx.diff :refer [component defui render should-update?]]))




(defui TheMenu
       (render [this _]
               (ui/menu-bar
                 :menus [(ui/menu :text "File"
                                  :items [(ui/menu-item :text "New"
                                                        :on-action {:event :file-new})
                                          (ui/menu-item :text "Open"
                                                        :on-action {:event :file-open})
                                          (ui/menu-item :text "Exit"
                                                        :on-action {:event :file-exit})])
                         (ui/menu :text "Help"
                                  :items [(ui/menu-item :text "About"
                                                        :on-action {:event :help-exit})])])))


(defui MainWindow
       (render [this state]
               (ui/v-box
                 :min-width 640
                 :min-height 480
                 :children [(the-menu state)])))


(defui MainStage
       (render [this args]
               (ui/stage
                 :title "Menu Test"
                 :shown true
                 :scene (ui/scene
                          :root (main-window args)))))


(defn -main []
  (let [state (atom {})
        handler-fn (fn [event]
                     (println event))
        ui-state (atom (dom/app (main-stage @state) handler-fn))]

    (add-watch state :ui (fn [k r os ns]
                           (swap! ui-state
                                  (fn [old-ui]
                                    (dom/update-app old-ui (main-stage ns))))))))




