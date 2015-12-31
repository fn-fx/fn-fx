(ns fn-fx.diff
  (:require [clojure.core.match :refer [match]]
            [fn-fx.set-once-type :refer [defquasitype set-once!]]))

(declare diff)

(defprotocol IDom
  (create-component! [this type spec])
  (delete-component! [this node])
  (set-child! [this parent id child])
  (set-indexed-child! [this parent k idx child])
  (delete-indexed-child! [this parent k idx child])
  (set-property! [this node property value]))

(defquasitype Component [type dom-node props children])

(defquasitype UserComponent [type props render-fn render-result])

(defrecord Created [node])
(defrecord Updated [node])
(defrecord Deleted [node])
(defrecord Noop [node])

(defn render-user-component [{:keys [props render-fn render-result] :as comp}]
  (when (not render-result)
    (set-once! comp :render-result (render-fn props)))
  (:render-result comp))

(defn val-type [a]
  (cond
    (nil? a) :nil
    (instance? Component a) :comp
    (instance? UserComponent a) :ucomp))

(defn needs-update? [from to]
  (let [{:keys [props render-fn render-result]} to]
    (if (and (= (:props from) props)
             (= (:type-k from) (:type-k to)))
      (do (set-once! to :render-result (:render-result from))
          false)
      (do (set-once! to :render-result nil)
          true))))


(defn diff-child-list [dom parent-node k a-list b-list]
  (dotimes [idx (max (count a-list) (count b-list))]
    (let [a (nth a-list idx nil)
          b (nth b-list idx nil)]
      (let [{:keys [node] :as result} (diff dom a b)]
        (condp instance? result
          ;; TODO: Unmount?
          Created (set-indexed-child! dom parent-node k idx node)
          Deleted (delete-indexed-child! dom parent-node k idx node)
          Updated nil)))))

(defn diff [dom a b]
  (match [(val-type a) (val-type b)]
    [:nil :comp] (let [node (create-component! dom (:type b) (:props b))]
                   (assert node "No Node returned by create-component!")
                   (set-once! b :dom-node node)
                   (reduce-kv
                     (fn [_ k v]
                       (if (sequential? v)
                         (diff-child-list dom node k nil v)
                         (let [child (:node (diff dom nil v))]
                           (set-child! dom node k child))))
                     nil
                     (:children b))
                   (->Created node))

    [:nil :ucomp] (diff dom nil (render-user-component b))

    [:ucomp :ucomp] (if (needs-update? a b)
                      (diff dom (render-user-component a) (render-user-component b))
                      (->Noop (:dom-node (:render-result b))))

    [:comp :comp] (if (= (:type a) (:type b))
                    (let [spec-a   (:props a)
                          spec-b   (:props b)
                          dom-node (:dom-node a)]
                      (assert dom-node (str "No DOM Node" (pr-str a)))
                      (set-once! b :dom-node dom-node)
                      (reduce-kv
                        (fn [_ k va]
                          (let [vb (get spec-b k)]
                            (when (not= va vb)
                              (set-property! dom dom-node k vb))))
                        nil
                        spec-a)

                      (let [children-a (:children a)]
                        (reduce-kv
                          (fn [_ k vb]
                            (let [va (get children-a k)]
                              (if (sequential? vb)
                                (diff-child-list dom dom-node k va vb)
                                (let [{:keys [node] :as result} (diff dom va vb)]
                                  (when (instance? Created result)
                                    ;; TODO: Unmount?
                                    (set-child! dom dom-node k node))))))
                          nil
                          (:children b)))

                      (->Updated dom-node))
                    (do (delete-component! dom (:dom-node a))
                        (diff dom nil b)))

    [:comp :nil] (->Deleted (:dom-node a))))


(defn component
  ([type spec]
   (component type spec nil))
  ([type spec children]
   (->Component type nil spec children)))


(defn user-component
  ([type-k render-fn]
   (->UserComponent type-k nil render-fn nil))
  ([type-k props render-fn]
   (->UserComponent type-k props render-fn nil)))