(ns fn-fx.diff-test
  (:require [fn-fx.diff :refer :all]
            [clojure.test :refer :all]))

(extend-type clojure.lang.Atom
  IDom
  (create-component! [this type spec]
    (let [id (:id (swap! this update-in [:id] inc))]
      (swap! this update-in [:log] conj [:create id type spec])
      id))

  (delete-component! [this node]
    (swap! this update-in [:log] conj [:delete node]))

  (set-child! [this parent id child]
    (swap! this update-in [:log] conj [:set-child parent id child]))

  (set-property! [this node property value]
    (swap! this update-in [:log] conj [:set-property node property value]))

  (set-indexed-child! [this parent k idx child]
    (swap! this update-in [:log] conj [:set-indexed-child parent k idx child]))

  (delete-indexed-child! [this parent k idx child]
    (swap! this update-in [:log] conj [:delete-indexed-child parent k idx child]))
  )


(defn log []
  (atom {:id 0 :log []}))

(deftest component-creation
  (testing "from nil to component"
    (let [log       (log)
          component (component :button {:text "Hello World"})]
      (is (= (->Created 1) (diff log nil component)))
      (is (= (:dom-node component) 1))
      (is (= @log {:id  1
                   :log [[:create 1 :button {:text "Hello World"}]]}))))

  (testing "from nil to nested component"
    (let [log       (log)
          component (component :stage {:text "Hey"}
                               {:root (component :button {:text "Hello World"})})]
      (is (= (->Created 1) (diff log nil component)))
      (is (= (:dom-node component) 1))
      (is (= @log {:id  2
                   :log [[:create 1 :stage {:text "Hey"}]
                         [:create 2 :button {:text "Hello World"}]
                         [:set-child 1 :root 2]]}))))

  (testing "from nil to user component"
    (let [log       (log)
          component (user-component
                      :foo
                      (fn [props]
                        (component :button {:text "Hello World"})))]

      (is (= (->Created 1) (diff log nil component)))
      (is (= @log {:id  1
                   :log [[:create 1 :button {:text "Hello World"}]]}))))

  (testing "from nil to nested user components"
    (let [log       (log)
          component (user-component
                      :foo
                      (fn [props]
                        (component :stage {:text "Hey"}
                                   {:root (user-component
                                            :foo
                                            (fn [props]
                                              (component :button {:text "Hello World"})))})))]
      (is (= (->Created 1) (diff log nil component)))
      (is (= @log {:id  2
                   :log [[:create 1 :stage {:text "Hey"}]
                         [:create 2 :button {:text "Hello World"}]
                         [:set-child 1 :root 2]]}))))
  )


(deftest component-diffing
  (testing "can change spec values"
    (let [log         (log)
          component-a (component :button {:text "test1"})
          component-b (component :button {:text "test2"})]
      (is (= (->Created 1) (diff log nil component-a)))
      (is (= (->Updated 1) (diff log component-a component-b)))

      (is (= @log {:id  1
                   :log [[:create 1 :button {:text "test1"}]
                         [:set-property 1 :text "test2"]]}))))

  (testing "can change component types"
    (let [log         (log)
          component-a (component :button {:text "test1"})
          component-b (component :text {:text "test2"})]
      (is (= (->Created 1) (diff log nil component-a)))
      (is (= (->Created 2) (diff log component-a component-b)))

      (is (= @log {:id  2
                   :log [[:create 1 :button {:text "test1"}]
                         [:delete 1]
                         [:create 2 :text {:text "test2"}]]}))))

  (testing "can diff two user components"
    (println "--------")
    (let [log          (log)
          rendered? (atom false)
          component-fn (fn [props]
                         (user-component
                           :foo
                           props
                           (fn [props]
                             (reset! rendered? true)
                             (if-not props
                               (component :button {:text "Start"})
                               (component :button {:text "End"})))))
          state-a      (component-fn false)]
      (is (= (->Created 1) (diff log nil state-a)))
      (is @rendered?)
      (reset! rendered? false)
      (is (:dom-node (:render-result state-a)))
      (let [state-b (component-fn false)]
        (is (= (->Noop 1) (diff log state-a state-b)))
        (is (not @rendered?))
        (is (:dom-node (:render-result state-b)))

        (is (= @log {:id  1
                     :log [[:create 1 :button {:text "Start"}]]}))


        (let [state-c (component-fn true)]
          (is (= (->Updated 1) (diff log state-b state-c))))

        (is (= @log {:id 1
                     :log [[:create 1 :button {:text "Start"}]
                           [:set-property 1 :text "End"]]}))))))


(deftest component-child-list-diffing
  (testing "can add children to a list"
    (let [log (log)
          component-a (component :list {} {:children []})
          component-b (component :list {} {:children [(component :text {:text "Hey"})]})
          component-c (component :list {} {:children []})]
      (is (= (->Created 1) (diff log nil component-a)))

      (is (= @log {:id 1
                   :log [[:create 1 :list {}]]}))

      (is (= (->Updated 1) (diff log component-a component-b)))

      (is (= @log {:id 2
                   :log [[:create 1 :list {}]
                         [:create 2 :text {:text "Hey"}]
                         [:set-indexed-child 1 :children 0 2]]}))

      (is (= (->Updated 1) (diff log component-b component-c)))

      (is (= @log {:id 2
                   :log [[:create 1 :list {}]
                         [:create 2 :text {:text "Hey"}]
                         [:set-indexed-child 1 :children 0 2]
                         [:delete-indexed-child 1 :children 0 2]]}))))


  (testing "complex nesting"
    (let [log (log)
          component-a (component :Stage {:title "Hello"}
                                 {:scene (component :ListView {}
                                                    {:items [(component :Button
                                                                        {:text "Hello"})]})})]

      (is (= (->Created 1) (diff log nil component-a)))
      (is (= @log {:id 3
                   :log [[:create 1 :Stage {:title "Hello"}]
                         [:create 2 :ListView {}]
                         [:create 3 :Button {:text "Hello"}]
                         [:set-indexed-child 2 :items 0 3]
                         [:set-child 1 :scene 2]]})))))