(ns fn-fx.fx-dom-test
  (:require [fn-fx.fx-dom :as dom]
            [fn-fx.diff2 :refer [component]]
            [fn-fx.render :refer [get-getter]]
            [clojure.test :refer :all]))

(defn get-prop [comp k]
  {:pre [comp k]}
  (println (type comp))

  (let [getter (get-getter (type comp))]
    (assert getter)
    (getter comp k)))

(deftest test-basic-component-properties
    (let [{:keys [root]} (dom/app (component :Stage {:title "Hello" :shown true}))]
      (is (= (get-prop root :title) "Hello"))))


(deftest list-properties-test
  (let [{:keys [root]} (dom/app (component :Stage {:title "Test"
                                                   :shown true}
                                           {:scene (component :Scene {}
                                                              {:root (component :ListView {}
                                                                                {:items [(component :Button
                                                                                                    {:text "Hello"})]})})}))]
    (is root)
    (is (= (-> root
               (get-prop :scene)
               (get-prop :root)
               (get-prop :items)
               first
               (get-prop :text))
           "Hello"))))




