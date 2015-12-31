(ns fn-fx.fx-dom-test
  (:require [fn-fx.fx-dom :as dom]
            [fn-fx.diff :refer [component]]
            [fn-fx.render :refer [get-getter ui]]
            [clojure.test :refer :all])
  (:import (javafx.scene.layout GridPane)))

(defn get-prop [comp k]
  {:pre [comp k]}
  (let [getter (get-getter (type comp))]
    (assert getter)
    (getter comp k)))

(deftest test-basic-component-properties
  (let [{:keys [root]} (dom/app (component :stage {:title "Hello"}))]
    (is (= (get-prop root :title) "Hello"))))



(defn gen-list [cnt]
  (let [items (vec (for [x (range cnt)]
                     (component :button
                                {:text (str "Hello" x)})))]
    (component :stage {:title "Test"}
               {:scene (component :scene {}
                                  {:root (component :list-view {}
                                                    {:items items})})})))

(deftest list-properties-test
  (let [{:keys [root] :as prev} (dom/app (gen-list 1))]
    (is root)
    (is (= (-> root
               (get-prop :scene)
               (get-prop :root)
               (get-prop :items)
               first
               (get-prop :text))
           "Hello0"))

    (let [{:keys [root] :as nxt} (dom/update-app prev (gen-list 2))]
      (is root)

      (testing "Items can be added to lists"
        (is (= (-> root
                   (get-prop :scene)
                   (get-prop :root)
                   (get-prop :items)
                   second
                   (get-prop :text))
               "Hello1")))

      (testing "Observable list did not change"
        (is (identical? (-> prev
                            :root
                            (get-prop :scene)
                            (get-prop :root)
                            (get-prop :items))
                        (-> root
                            (get-prop :scene)
                            (get-prop :root)
                            (get-prop :items)
                            ))))

      (let [{:keys [root]} (dom/update-app nxt (gen-list 1))]
        (is (= (-> root
                   (get-prop :scene)
                   (get-prop :root)
                   (get-prop :items)
                   first
                   (get-prop :text))
               "Hello0"))))))


(deftest static-member-tests
  (let [spec (ui :stage :title "Login"
                 :scene (ui :scene
                            :root (ui :grid-pane
                                      :children [(ui :text
                                                     :text "Welcome"
                                                     :grid-pane/columnIndex 0
                                                     :grid-pane/rowIndex 0
                                                     :grid-pane/columnSpan 2
                                                     :grid-pane/rowSpan 1)]
                                      )))
        {:keys [root] :as app} (dom/app spec)]
    (is root)

    (let [children (-> root
                       (get-prop :scene)
                       (get-prop :root)
                       (get-prop :children))]
      (is (= (count children) 1))
      (is (= (GridPane/getColumnSpan (first children)) 2)))))





