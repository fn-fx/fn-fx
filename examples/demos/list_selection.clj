(ns demos.list-selection
  (:require [fn-fx.render :as render]
            [fn-fx.component :refer [defcomponent]]))

;; Data Model

(def data (atom {:list-a (mapv (partial str "Item ") (range 10))
                 :list-b []}))


(defn assign-right [state idx]
  (let [src (:list-a state)
        value (nth src idx)
        new-lst (vec (concat (subvec src 0 idx)
                             (subvec src (inc idx) (count src))))]
    (println "Moving" value new-lst)
    (-> state
        (assoc :list-a new-lst)
        (update-in [:list-b] conj value))))

(defn assign-left [state idx]
  (let [src (:list-b state)
        value (nth src idx)
        new-lst (vec (concat (subvec src 0 idx)
                             (subvec src (inc idx) (count src))))]
    (println "Moving" value new-lst)
    (-> state
        (assoc :list-b new-lst)
        (update-in [:list-a] conj value))))

;; End Data Model

;; UI Views

(defcomponent list-item [list-id idx item]
  {:type :Label
   :maxWidth Double/MAX_VALUE
   :maxHeight Double/MAX_VALUE
   :onMouseClicked {:tag :item-click
                    :idx idx
                    :list-id list-id
                    :event-properties #{:clickCount}}
   :text item})

(defcomponent list-component [list id]
  {:type           :ListView
   :fn-fx/children #{:items :selectionModel}
   :fn-fx/id       id
   :items          (vec (map-indexed (partial list-item id) list))})

(defcomponent assignment-buttons []
  {:type :VBox
   :fn-fx/children #{:children}
   :children [{:type :Button
               :text "<"
               :onAction {:tag :assign-left
                          :include #{[:list-b :selectionModel :selectedIndex]}}}
              {:type :Button
               :text ">"
               :onAction {:tag :assign-right
                          :include #{[:list-a :selectionModel :selectedIndex]}}}]})

(defcomponent main [data]
  {:type           :Stage
   :fn-fx/children #{:scene}
   :title          "Item Assignments"
   :scene          {:type           :Scene
                    :width          300
                    :height         275
                    :fn-fx/children #{:root}
                    :root           {:type :HBox
                                     :fn-fx/children #{:children}
                                     :children [(list-component (:list-a data) :list-a)
                                                (assignment-buttons)
                                                (list-component (:list-b data) :list-b)]}}})

;; End UI Views

;; App Logic
(let [c (render/create-root (main @data))
      re-render #(render/update! c (main @data))]
  (render/update-handler! c (fn [evt]
                              (try
                                (condp identical? (:tag evt)
                                  :assign-right (swap! data assign-right (get evt [:list-a :selectionModel :selectedIndex]))
                                  :assign-left (swap! data assign-left (get evt [:list-b :selectionModel :selectedIndex]))
                                  :item-click (when (= 2 (:clickCount evt))
                                                (if (= (:list-id evt) :list-a)
                                                  (swap! data assign-right (:idx evt))
                                                  (swap! data assign-left (:idx evt)))))
                                (re-render)
                                (catch Exception ex
                                  (println ex)))))
  (render/show! c))