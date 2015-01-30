(ns get-started.form
  (:require [fn-fx.render :as render]))


(def init-state
  {:type           :Stage
   :fn-fx/children #{:root}
   :title          "Login"
   :scene          {:type           :Scene
                    :width          300
                    :height         275
                    :fn-fx/children #{:root}
                    :root           {:type :GridPane
                                     :fn-fx/children #{:children}
                                     :alignment :center
                                     :hgap 10
                                     :vgap 10
                                     :padding {:type :Insets
                                               :left 25
                                               :right 25
                                               :top 25
                                               :bottom 25}
                                     :children [{:type :Text
                                                 :text "Welcome"
                                                 :font {:type :Font
                                                        :name "Tahoma"
                                                        :size 20}
                                                 :GridPane/columnIndex 0
                                                 :GridPane/rowIndex 0
                                                 :GridPane/columnSpan 2
                                                 :GridPane/rowSpan 1
                                                 }]}}})

(let [c (render/create-root init-state)]
  (render/update-handler! c (fn [evt]
                              (println "Hello world! : " evt)))
  (render/show! c))