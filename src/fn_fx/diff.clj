(ns fn-fx.diff
  (:require [fn-fx.set-once-type :refer [defquasitype set-once!]]
            [fn-fx.util :as util]))

(declare diff)

(defprotocol IDom
  (create-component! [this type])
  (set-child! [this parent id child])
  (set-indexed-child! [this parent k idx child])
  (delete-indexed-child! [this parent k idx child])
  (replace-indexed-child! [this parent k idx child])
  (set-property! [this node property value]))

(defquasitype Component [type dom-node props])

(defquasitype UserComponent [type props render-fn render-result])

(defprotocol IUserComponent
  (render [this props])
  (should-update? [this old-props new-props]))

(defrecord Created [node])
(defrecord Updated [node])
(defrecord NewValue [value])
(defrecord Deleted [node])
(defrecord Noop [node])

(defn render-user-component [{:keys [props render-result] :as comp}]
  (when (not render-result)
    (set-once! comp :render-result (render comp props)))
  (:render-result comp))

(defn val-type [a]
  (cond
    (nil? a) :nil
    (instance? Component a) :comp
    (satisfies? IUserComponent a) :ucomp
    :else :val))

(defn needs-update? [from to]
  (let [{:keys [props]} to]
    (if (not= (:type from) (:type to))
      true
      (if (should-update? to (:props from) props)
        (do (set-once! to :render-result nil)
            true)
        (do (set-once! to :render-result (:render-result from))
            false)))))


(defn diff-child-list [dom parent-node k a-list b-list]
  (dotimes [idx (max (count a-list) (count b-list))]
    (let [a (nth a-list idx nil)
          b (nth b-list idx nil)]
      (let [{:keys [node] :as result} (diff dom a b)]
        (condp instance? result
          ;; TODO: Unmount?
          Created (set-indexed-child! dom parent-node k idx node)
          Deleted (delete-indexed-child! dom parent-node k idx node)
          Updated (replace-indexed-child! dom parent-node k idx node)
          Noop nil)))))

(defn diff-component [dom dom-node spec-a spec-b]
  (reduce-kv
    (fn [_ k va]
      (let [vb (get spec-b k)]
        (if (sequential? vb)
          (diff-child-list dom dom-node k va vb)
          (let [result (diff dom va vb)]
            (if (or (instance? Created result)
                    (instance? Updated result))
              (set-property! dom dom-node k (:node result)))))))
    nil
    spec-a)

  (reduce-kv
    (fn [_ k vb]
      (when-not (get spec-a k)
        (if (sequential? vb)
          (diff-child-list dom dom-node k nil vb)
          (let [result (diff dom nil vb)]
            (if (or (instance? Created result)
                    (instance? Updated result))
              (set-property! dom dom-node k (:node result)))))))
    nil
    spec-b))

(defn diff
  [dom a b]
  (let [refresh-node (fn [node compo-a compo-b]
                       (set-once! compo-b :dom-node node)
                       (diff-component dom node (:props compo-a) (:props compo-b))
                       node)
        new-node (fn [compo]
                   (let [node (create-component! dom (:type compo))]
                     (assert node "No Node returned by create-component!")
                     (refresh-node node nil compo)))]
    (condp = [(val-type a) (val-type b)]
      [:nil :comp] (->Created (new-node b))

      [:val :val] (if (= a b)
                    (->Noop b)
                    (->Updated b))

      [:nil :val] (->Created b)

      [:nil :ucomp] (diff dom nil (render-user-component b))

      [:ucomp :nil] (diff dom (render-user-component a) nil)

      [:ucomp :ucomp] (if (needs-update? a b)
                        (diff dom (render-user-component a) (render-user-component b))
                        (->Noop (:dom-node (:render-result b))))

      [:ucomp :comp] (diff dom (render-user-component a) b)

      [:comp :ucomp] (diff dom a (render-user-component b))

      [:comp :comp] (-> (if (= (:type a) (:type b))
                          (doto (:dom-node a)
                            (assert (str "No DOM Node" (pr-str a)))
                            (refresh-node a b))
                          (new-node b))
                        ->Updated)

      [:comp :nil] (->Deleted (:dom-node a))

      [:val :nil] (->Deleted a))))


(defn component
  ([type spec]
   (->Component type nil spec)))

(def valid-ui-fns '#{render should-update?})

(defmacro defui [nm & fns]
  (let [has-should-update? (atom false)

        mp (reduce
             (fn [acc [nm :as fn]]
               (assert (valid-ui-fns nm) (str "Invalid UI function " nm))
               (when (= nm 'should-update?)
                 (reset! has-should-update? true))
               (conj acc fn))
             []
             fns)
        fn-name (symbol (util/camel->kabob nm))]
    `(let [kw# (keyword (name (.getName ^clojure.lang.Namespace *ns*)) ~(name nm))]
           (defquasitype ~nm [~'type ~'props ~'render-result]
                       IUserComponent
                       ~@mp
                       ~@(when (not @has-should-update?)
                           `[(should-update? [this# old-props# new-props#]
                                           (not= old-props# new-props#))]))
         (defn ~fn-name
           ([] (~fn-name {}))
           ([k# v# & props#]
             (~fn-name (apply hash-map k# v# props#)))
           ([props#]
             (~(symbol (str "->" (name nm)))
               kw#
               props#
               nil))))))
