(ns getting-started.01-hello-word
  (:require [fn-fx.fx-dom :as dom]
            [fn-fx.diff :refer [component defui render should-update?]]
            [fn-fx.render :refer [ui]]))



(defn -main []
  (let [u (ui :stage
            :title "Hello World!"
            :shown true
            :scene (ui :scene
                     :width 300
                     :height 250
                     :root (ui :stack-pane
                             :children [(ui :button
                                          :text "Say 'Hello World'"
                                          :on-action {:say "Hello World!"})])))
        handler-fn (fn [evt]
                     (println "Received Event: " evt))]
    (dom/app u handler-fn)))
