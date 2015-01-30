(ns fn-fx.diff)


(defrecord SetProperty [property-name value])
(defrecord UnSetProperty [property-name])
(defrecord Child [property-name updates])
(defrecord ListChild [property-name updates])
(defrecord Create [template])
(defrecord ListDelete [])

(def list-delete (->ListDelete))

(declare diff)

(defn diff-properties [a b rfn changes]
  (let [changes (reduce-kv
                  ;; Find changed or removed properties
                  (fn [changes k v]
                    (if-let [bv (get b k)]
                      (if (or (= bv v)
                              (contains? (:fn-fx/children a) k))
                        changes
                        (rfn changes (->SetProperty k bv)))
                      (rfn changes (->UnSetProperty k))))
                  changes
                  a)
        changes (reduce-kv
                  ;; Find new properties
                  (fn [changes k v]
                    (if-some [av (get a k)]
                      changes
                      (rfn changes (->SetProperty k v))))
                  changes
                  b)]
    changes))

(defn diff-list [a b]
  (reduce
    (fn [changes idx]
      (let [aitem (nth a idx ::not-found)
            bitem (nth b idx ::not-found)]
        (cond
          (= aitem ::not-found) (conj changes [idx (->Create bitem)])
          (= bitem ::not-found) (conj changes [idx list-delete])
          :else (let [child-changes (diff aitem bitem)]
                  (if child-changes
                    (conj changes [idx child-changes])
                    changes)))))
    []
    (range (max (count a) (count b)))))

(defn diff-children [a b rfn changes]
  (let [changes (reduce
                  (fn [changes child]
                    (if (sequential? (get a child))
                        (rfn changes (->ListChild child (diff-list (get a child) (get b child))))
                      (let [d (diff (get a child) (get b child))]
                        (if (empty? d)
                          changes
                          (rfn changes (->Child child d))))))
                  changes
                  (:fn-fx/children a))]
    changes))

(defn diff [a b]
  (if (identical? (:type a) (:type b))
    (if (identical? a b)
      nil
      (->> #{}
           (diff-properties a b conj)
           (diff-children a b conj)))
    #{(->Create b)}))