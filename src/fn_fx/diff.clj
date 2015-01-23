(ns fn-fx.diff)


(defrecord SetProperty [property-name value])
(defrecord UnSetProperty [property-name])
(defrecord Child [property-name updates])
(defrecord Create [template])

(declare diff)

(defn diff-properties [a b rfn changes]
  (let [changes (reduce-kv
                  ;; Find changed or removed properties
                  (fn [changes k v]
                    (if-let [bv (get b k)]
                      (if (or (= bv v)
                              (contains? (:children a) k))
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

(defn diff-children [a b rfn changes]
  (let [changes (reduce
                  (fn [changes child]
                    (let [d (diff (get a child) (get b child))]
                      (if (empty? d)
                        changes
                        (rfn changes (->Child child d)))))
                  changes
                  (:children a))]
    changes))

(defn diff [a b]
  (if (identical? (:type a) (:type b))
    (if (identical? a b)
      nil
      (->> #{}
           (diff-properties a b conj)
           (diff-children a b conj)))
    #{(->Create b)}))