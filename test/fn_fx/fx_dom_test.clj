(ns fn-fx.fx-dom-test
  (:require [fn-fx.fx-dom :as dom]
            [fn-fx.diff :refer [component defui render should-update?]]
            [fn-fx.render-core :refer [get-getter]]
            [fn-fx.controls :as ui]
            [clojure.test :refer :all]
            [fn-fx.util :as util])
  (:import (javafx.scene.layout GridPane)))

(defn get-prop [comp k]
  {:pre [comp k]}
  (let [getter (get-getter (type comp) k)]
    (assert getter)
    (let [v (getter comp)]
      (if (delay? v)
        @v
        v))))

(deftest test-basic-component-properties
  (let [{:keys [root]} (dom/app (component :javafx.stage.Stage
                                           {:title "Hello"}))]
    (Thread/sleep 100)
    (is (= (get-prop @root :title) "Hello"))))

(deftest test-primitive-type-conversions
  (let [default-count (-> :javafx.animation.TranslateTransition
                          (component {}) dom/app :root)
        long-count (-> :javafx.animation.TranslateTransition
                       (component {:cycle-count 22}) dom/app :root)
        integer-count
        (-> :javafx.animation.TranslateTransition
            (component {:cycle-count javafx.animation.Animation/INDEFINITE})
            dom/app :root)]
    (Thread/sleep 100)
    (is (= 1 (get-prop @default-count :cycle-count)))
    (is (= 22 (get-prop @long-count :cycle-count)))
    (is (= -1 (get-prop @integer-count :cycle-count)))))

(defn gen-list [cnt]
  (let [items (vec (for [x (range cnt)]
                     (ui/button
                       :text (str "Hello" x))))]
    (ui/stage
              :title "Test"
              :scene (ui/scene
                       :root (ui/list-view
                               :items items)))))

(comment
  (def root1 (dom/app (gen-list 1)))
  (def root2 (dom/update-app root1 (gen-list 2)))
  (get-prop  @(:root root1) :scene)
  (-> root (get-prop :scene)
           (get-prop :root)
           (get-prop :items)
      first
      (get-prop :text))
  root

  )

(deftest list-properties-test
  (let [{:keys [root] :as prev} (dom/app (gen-list 1))]
    (is @root)
    (Thread/sleep 100)
    (is (= (-> @root
               (get-prop :scene)
               (get-prop :root)
               (get-prop :items)
               first
               (get-prop :text))
           "Hello0"))

    (let [{:keys [root] :as nxt} (dom/update-app prev (gen-list 2))]
      (is @root)
      (Thread/sleep 100)

      (testing "Items can be added to lists"
        (is (= (-> @root
                   (get-prop :scene)
                   (get-prop :root)
                   (get-prop :items)
                   second
                   (get-prop :text))
               "Hello1")))

      (testing "Observable list did not change"
        (is (identical? (-> prev
                            :root
                            deref
                            (get-prop :scene)
                            (get-prop :root)
                            (get-prop :items))
                        (-> @root
                            (get-prop :scene)
                            (get-prop :root)
                            (get-prop :items)
                            ))))

      (let [{:keys [root]} (dom/update-app nxt (gen-list 1))]
        (Thread/sleep 100)
        (is (= (-> @root
                   (get-prop :scene)
                   (get-prop :root)
                   (get-prop :items)
                   first
                   (get-prop :text))
               "Hello0"))))))


(deftest static-member-tests
  (let [spec (ui/stage :title "Login"
               :scene (ui/scene
                        :root (ui/grid-pane
                                :children [(ui/text
                                             :text "Welcome"
                                             :grid-pane/column-index 0
                                             :grid-pane/row-index 0
                                             :grid-pane/column-span 2
                                             :grid-pane/row-span 1)]
                                )))
        {:keys [root] :as app} (dom/app spec)]
    (Thread/sleep 300)
    (is @root)

    (let [children (-> @root
                       (get-prop :scene)
                       (get-prop :root)
                       (get-prop :children))]
      (is (= (count children) 1))
      (is (= (GridPane/getColumnSpan (first children)) 2)))))


(deftest component-test
  (defui LabeledButton
    (render [this {:keys [value]}]
      (ui/button :text (str "Value " value))))

  (defui MainArea
    (render [this {:keys [value]}]
      (ui/stage :title "Hello"
          :scene (ui/scene
                     :root (labeled-button :value value)))))

  (let [{:keys [root] :as prev} (dom/app (main-area :value 42))]
    (Thread/sleep 100)
    (is @root)

    (is (= (-> @root
               (get-prop :scene)
               (get-prop :root)
               (get-prop :text))
           "Value 42"))


    (let [{:keys [root] :as nxt} (dom/update-app prev (main-area :value 43))]
      (Thread/sleep 100)
      (is @root)

      (is (= (-> @root
                 (get-prop :scene)
                 (get-prop :root)
                 (get-prop :text))
             "Value 43")))



    )

  )


(deftest callback-test
  (let [events (atom [])
        {:keys [root] :as prev} (dom/app (ui/button :text "Foo" :on-action {:event :click})
                                         (fn [evt]
                                           (swap! events conj evt)))]
    (util/run-and-wait
      (.fire @root))
    (Thread/sleep 100)

    (is (= @events
           [{:event :click
             :fn-fx/includes {}}]))))




