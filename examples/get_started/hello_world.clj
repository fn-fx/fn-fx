(ns getting_started.hello_world
    (:require [fn-fx.render :as render]))


(def init-state
  {:type           :Stage
   :fn-fx/children #{:root}
   :title          "Hello World!"
   :scene          {:type           :Scene
                    :width          300
                    :height         250
                    :fn-fx/children #{:root}
                    :root           {:type           :StackPane
                                     :fn-fx/children #{:children}
                                     :children       [{:type     :Button
                                                       :onAction {:tag :say-hello}
                                                       :text     "Say Hello World"}]}}})


(let [c (render/create-root init-state)]
  (render/update-handler! c (fn [evt]
                              (println "Hello world! : " evt)))
  (render/show! c))