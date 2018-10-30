(ns getting-started.01-hello-word
  (:require [fn-fx.fx-dom :as dom]
            [fn-fx.controls :as ui]
            [fn-fx.diff :refer [component defui render should-update?]]))



(defn -main []
  (let [u (ui/stage
            :title "Hello World!"
            :shown true
            :min-width 300
            :min-height 300
            :scene (ui/scene
                     :root (ui/stack-pane
                             :children [(ui/button
                                          :text "Say 'Hello World'"
                                          :on-action {:say "Hello World!"}
                                          )])))
        handler-fn (fn [evt]
                     (println "Received Event: " evt))]
    (dom/app u handler-fn)))


(comment
  (-main)
  )
