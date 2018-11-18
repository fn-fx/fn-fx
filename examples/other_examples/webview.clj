(ns other-examples.webview
  (:require
   [fn-fx.fx-dom :as dom]
   [fn-fx.controls :as ui]
   [fn-fx.diff :refer [component defui render should-update?]]
   [fn-fx.util :refer [run-later]]))

(def initial-state {:current-url nil})

(defonce app-state (atom initial-state))
(defonce app-ui-state (agent nil))

(defmulti handle-event (fn [state {:keys [event]}] event))

(defn load-url [url]
  (let [engine (.getEngine (.lookup (.getScene @(:root @app-ui-state)) "#web-browser"))]
    (run-later
     (.load engine url))))

(defmethod handle-event :load-url
  [state {:keys [fn-fx/includes]}]
  (let [new-url (get-in includes [:url-field :text])]
    (load-url new-url)
    (assoc-in state [:current-url] new-url)))

(defui WebBrowser
  (render [this args]
          (ui/v-box
           ;;:style "-fx-base: rgb(30, 30, 35);"           
           :padding (ui/insets
                     :top-right-bottom-left 25)
           :children [(ui/text-field
                       :id :url-field
                       :prompt-text "Url: "
                       :font (ui/font :family "Helvetica" :size 20)
                       :on-action {:event :load-url
                                   :fn-fx/include {:url-field #{:text}}})
                      (ui/web-view :id "web-browser")])))

(defui MainStage
  (render [this args]
          (ui/stage :title "Web Browser Main Stage"                    
                    :min-width 1024
                    :min-height 768
                    :shown true
                    :scene (ui/scene :root (web-browser args)))))

(defn ui-event-handler [event]
  (try
    (swap! app-state handle-event event)
    (catch Throwable ex
      (println (str "Error updating app data state! " ex)))))

(defn update-ui-state-agent [old-ui]
  (try    
    (dom/update-app old-ui (main-stage @app-state))
  (catch Throwable ex
    (println (str "Error updating app UI state! " ex)))))

(defn run-webview-app []
  (let [u (main-stage)
        ui-state (dom/app (main-stage @app-state) ui-event-handler)]
    (send app-ui-state (fn [old-state]  ui-state))
    (add-watch app-state :ui (fn [key atom old-state new-state]
                               (send app-ui-state #'update-ui-state-agent)))))
  
(defn -main [& args]
  (run-webview-app))
