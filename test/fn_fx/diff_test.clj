(ns fn-fx.diff-test
  (:require [fn-fx.diff :refer :all]
            [clojure.test :refer :all]))

(extend-type clojure.lang.Atom
  IDom
  (create-component! [this type]
    (let [id (:id (swap! this update-in [:id] inc))]
      (swap! this update-in [:log] conj [:create id type])
      id))

  (delete-component! [this node]
    (swap! this update-in [:log] conj [:delete node]))

  (set-child! [this parent id child]
    (pr)
    (swap! this update-in [:log] conj [:set-child parent id child]))

  (set-property! [this node property value]
    (swap! this update-in [:log] conj [:set-property node property value]))

  (set-indexed-child! [this parent k idx child]
    (swap! this update-in [:log] conj [:set-indexed-child parent k idx child]))

  (delete-indexed-child! [this parent k idx child]
    (swap! this update-in [:log] conj [:delete-indexed-child parent k idx child]))

  (replace-indexed-child! [this parent k idx child]
    (swap! this update-in [:log] conj [:replace-indexed-child parent k idx child])))


(defn log []
  (atom {:id 0 :log []}))

(deftest component-creation
  (testing "from nil to component"
    (let [log       (log)
          component (component :button {:text "Hello World"})]
      (is (= (->Created 1) (diff log nil component)))
      (is (= (:dom-node component) 1))
      (is (= @log {:id  1
                   :log [[:create 1 :button]
                         [:set-property 1 :text "Hello World"]]}))))

  (testing "from nil to nested component"
    (let [log       (log)
          component (component :stage
                               {:text "Hey"
                                :root (component :button {:text "Hello World"})})]
      (is (= (->Created 1) (diff log nil component)))
      (is (= (:dom-node component) 1))
      (is (= @log {:id  2
                   :log [[:create 1 :stage]
                         [:set-property 1 :text "Hey"]
                         [:create 2 :button]
                         [:set-property 2 :text "Hello World"]
                         [:set-property 1 :root 2]]}))))

  (testing "from nil to user component"

    (defui SimpleComponent
      (render [this props]
        (component :button {:text "Hello World"})))


    (let [log       (log)
          component (simple-component)]

      (is (= (->Created 1) (diff log nil component)))
      (is (= @log {:id  1
                   :log [[:create 1 :button]
                         [:set-property 1 :text "Hello World"]]}))))

  (testing "from nil to nested user components"

    (defui InternalComponent
      (render [this props]
        (component :button {:text "Hello World"})))

    (defui NestedComponent
      (render [this props]
        (component :stage
                   {:text "Hey" :root (internal-component)})))


    (let [log       (log)
          component (nested-component)]
      (is (= (->Created 1) (diff log nil component)))
      (is (= @log {:id  2
                   :log [[:create 1 :stage]
                         [:set-property 1 :text "Hey"]
                         [:create 2 :button]
                         [:set-property 2 :text "Hello World"]
                         [:set-property 1 :root 2]]}))))

  (testing "from value to nil"
    (let [log (log)
          value :x]
      (is (= (->Deleted value) (diff log value nil))))))

(deftest component-diffing
  (testing "can change spec values"
    (let [log         (log)
          component-a (component :button {:text "test1"})
          component-b (component :button {:text "test2"})]
      (is (= (->Created 1) (diff log nil component-a)))
      (is (= (->Updated 1) (diff log component-a component-b)))

      (is (= @log {:id  1
                   :log [[:create 1 :button]
                         [:set-property 1 :text "test1"]
                         [:set-property 1 :text "test2"]]}))))

  (testing "can change component types"
    (let [log         (log)
          component-a (component :button {:text "test1"})
          component-b (component :text {:text "test2"})]
      (is (= (->Created 1) (diff log nil component-a)))
      (is (= (->Updated 2) (diff log component-a component-b)))

      (is (= @log {:id  2
                   :log [[:create 1 :button]
                         [:set-property 1 :text "test1"]
                         [:create 2 :text]
                         [:set-property 2 :text "test2"]]}))))

  (testing "can diff two user components"
    (defui BranchingComponent
      (render [this {:keys [value rendered?]}]
        (reset! rendered? true)
        (if-not value
          (component :button {:text "Start"})
          (component :button {:text "End"}))))

    (let [log          (log)
          rendered?    (atom false)
          component-fn (partial branching-component :rendered? rendered? :value)
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
                     :log [[:create 1 :button]
                           [:set-property 1 :text "Start"]]}))


        (let [state-c (component-fn true)]
          (is (= (->Updated 1) (diff log state-b state-c))))

        (is (= @log {:id  1
                     :log [[:create 1 :button]
                           [:set-property 1 :text "Start"]
                           [:set-property 1 :text "End"]]}))))))


(deftest component-child-list-diffing
  (testing "can add children to a list"
    (let [log         (log)
          component-a (component :list {:children []})
          component-b (component :list {:children [(component :text {:text "Hey"})]})
          component-c (component :list {:children []})]
      (is (= (->Created 1) (diff log nil component-a)))

      (is (= @log {:id  1
                   :log [[:create 1 :list]]}))

      (is (= (->Updated 1) (diff log component-a component-b)))

      (is (= @log {:id  2
                   :log [[:create 1 :list]
                         [:create 2 :text]
                         [:set-property 2 :text "Hey"]
                         [:set-indexed-child 1 :children 0 2]]}))

      (is (= (->Updated 1) (diff log component-b component-c)))

      (is (= @log {:id  2
                   :log [[:create 1 :list]
                         [:create 2 :text]
                         [:set-property 2 :text "Hey"]
                         [:set-indexed-child 1 :children 0 2]
                         [:delete-indexed-child 1 :children 0 2]]}))))

  (testing "can replace child in list"
    (let [log (log)
          component-a (component :list {:children [(component :text {:text "Hey"})]})
          component-b (component :list {:children [(component :text {:text "Bye"})]})
          component-c (component :list {:children [(component :button {:text "Click me"})]})]

      (is (= (->Created 1) (diff log nil component-a)))
      (is (= (->Updated 1) (diff log component-a component-b)))

      (is (= @log {:id  2
                   :log [[:create 1 :list]
                         [:create 2 :text]
                         [:set-property 2 :text "Hey"]
                         [:set-indexed-child 1 :children 0 2]
                         [:set-property 2 :text "Bye"]
                         [:replace-indexed-child 1 :children 0 2]]}))

      (is (= (->Updated 1) (diff log component-b component-c)))
      (is (= @log {:id  3
                   :log [[:create 1 :list]
                         [:create 2 :text]
                         [:set-property 2 :text "Hey"]
                         [:set-indexed-child 1 :children 0 2]
                         [:set-property 2 :text "Bye"]
                         [:replace-indexed-child 1 :children 0 2]
                         [:create 3 :button]
                         [:set-property 3 :text "Click me"]
                         [:replace-indexed-child 1 :children 0 3]]}))))


  (testing "complex nesting"
    (let [log         (log)
          component-a (component :Stage
                                 {:title "Hello"
                                  :scene (component :ListView
                                                    {:items [(component :Button
                                                                        {:text "Hello"})]})})]

      (is (= (->Created 1) (diff log nil component-a)))
      (is (= @log {:id  3
                   :log [[:create 1 :Stage]
                         [:set-property 1 :title "Hello"]
                         [:create 2 :ListView]
                         [:create 3 :Button]
                         [:set-property 3 :text "Hello"]
                         [:set-indexed-child 2 :items 0 3]
                         [:set-property 1 :scene 2]]})))))


(deftest component-should-update-tests
  (testing "hard-coded always update"
    (defui AlwaysUpdating
      (render [this {:keys [rendered?]}]
        (reset! rendered? true)
        (component :button {:text "Hey"}))
      (should-update? [this old-props new-props]
        true))

    (let [log (log)
          rendered? (atom false)
          first (always-updating :rendered? rendered?)]
      (is (= (->Created 1) (diff log nil first)))
      (is (= true @rendered?))

      (is (= @log {:id 1

                   :log [[:create 1 :button]
                         [:set-property 1 :text "Hey"]]}))

      (reset! rendered? false)

      (is (= (->Updated 1) (diff log first (always-updating :rendered? rendered?))))
      (is (= true @rendered?))

      (is (= @log {:id 1
                   :log [[:create 1 :button]
                         [:set-property 1 :text "Hey"]]})))))
