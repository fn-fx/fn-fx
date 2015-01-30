(ns demos.list-view
  (:require [fn-fx.render :as render]
            [fn-fx.component :refer [defcomponent]]))


(defcomponent list-item [c]
  {:type :HBox
   :fn-fx/children #{:children}
   :children [{:type :Button
               :onAction {:action :remove
                          :item c}
               :text "-"}
              {:type :Text
               :text c}]})

(defcomponent toplevel [data]
  {:type           :Stage
   :fn-fx/children #{:scene}
   :title          "Login"
   :scene          {:type           :Scene
                    :width          300
                    :height         275
                    :fn-fx/children #{:root}
                    :root           {:type           :ListView
                                     :fn-fx/children #{:items}
                                     :items          (mapv list-item data)}}})

(let [data (atom (seq "ABCDEFGHIJKLMNOPQRSTUVWXYZ"))
      c (render/create-root (toplevel @data))
      re-render #(render/update! c (toplevel @data))]
  (render/update-handler! c (fn [evt]
                              (println "Removing item : " evt)
                              (swap! data (partial remove #{(:item evt)}))
                              (re-render)))
  (render/show! c))