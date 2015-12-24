(ns fn-fx.diff-test
  (:require [fn-fx.diff :refer :all]
            [clojure.test :refer :all]))


(deftest test-property-changes

  (testing "one property can change"
    (let [changes (diff {:id :my-id
                         :a  42}
                        {:id :my-id
                         :a  43})]
      (is (= changes #{(->SetProperty :a 43)}))))

  (testing "two properties can change"
    (let [changes (diff {:id :my-id
                         :a  42
                         :b  44}
                        {:id :my-id
                         :a  43
                         :b  42})]
      (is (= changes #{(->SetProperty :a 43)
                       (->SetProperty :b 42)}))))

  (testing "Removing a property triggers a UnSetProperty"
    (let [changes (diff {:id :my-id
                         :a  42}
                        {:id :my-id})]
      (is (= changes #{(->UnSetProperty :a)}))))

  (testing "Adding a new property triggers a SetProperty"
    (let [changes (diff {:id :my-id}
                        {:id :my-id
                         :a  42})]
      (is (= changes #{(->SetProperty :a 42)})))))

(deftest test-nested-changes
  (testing "nested children are diffed"
    (let [changes (diff {:id             :my-id
                         :fn-fx/children #{:a}
                         :a              {:id :child-id
                                          :a  1}}
                        {:id             :my-id
                         :fn-fx/children #{:a}
                         :a              {:id :child-id
                                          :a  2}})]
      (is (= changes #{(->Child :a #{(->SetProperty :a 2)})})))))

(deftest creation
  (testing "creation from nothing"
    (let [changes (diff nil
                        {:type :foo})]
      (is (= changes #{(->Create {:type :foo})}))))

  (testing "changing a child type"
    (let [changes (diff {:type           :a
                         :fn-fx/children #{:a}
                         :a              {:type :foo}}
                        {:type           :a
                         :fn-fx/children #{:a}
                         :a              {:type :bar}})]
      (is (= changes #{(->Child :a #{(->Create {:type :bar})})})))))

(deftest components-test
  (testing "can compare components with nil"
    (let [changes (diff nil
                        (->Component [] (fn [props]
                                          {:type :bar}) nil))]
      (is (= changes #{(->Create {:type :bar})}))))

  (testing "can compare two components"
    (let [changes (diff (->Component [] (fn [props]
                                          {:type :bar}) nil)
                        (->Component [] (fn [props]
                                          {:type :foo}) nil))]
      (is (= changes #{(->Create {:type :foo})})))

    (let [changes (diff (->Component [] (fn [props]
                                          {:type :foo
                                           :zing 42}) nil)
                        (->Component [] (fn [props]
                                          {:type :foo
                                           :zing 0}) nil))]
      (is (= changes #{(->SetProperty :zing 0)}))))

  (testing "un-modified components do not re-render"
    (let [a (atom false)
          f (fn [props]
              (assert (not @a) "Failure")
              (reset! a true)
              {:type :foo
               :zing 42})
          changes (diff (->Component [] f nil)
                        (->Component [] f nil))]
      (is (= changes nil)))))

(deftest list-nodes
  (testing "can compare lists"
    (let [changes (diff {:type           :a
                         :fn-fx/children #{:a}
                         :a              []}
                        {:type           :a
                         :fn-fx/children #{:a}
                         :a              [{:type :foo}]})]
      (is (= changes #{(->ListChild :a [[0 (->Create {:type :foo})]])}))))

  #_(testing "can update lists"
      (let [changes (diff {:type           :a
                           :fn-fx/children #{:a}
                           :a              [{:type :b
                                             :prop {:type :foo
                                                    :tag  42}}]}
                          {:type           :a
                           :fn-fx/children #{:a}
                           :a              [{:type :b
                                             :prop {:type :foo
                                                    :tag  43}}]})]
        (is (= changes 0)))))
