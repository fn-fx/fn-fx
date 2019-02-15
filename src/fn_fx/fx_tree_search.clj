(ns fn-fx.fx-tree-search
  (:import (javafx.scene Node)))

(set! *warn-on-reflection* true)

(defprotocol IParentNode
  (get-children [this])
  (get-parent [this]))

(extend-protocol IParentNode
  javafx.scene.Parent
  (get-children [this]
    (.getChildrenUnmodifiable this))
  (get-parent [this]
    (.getParent this))

  javafx.scene.Node
  (get-children [this]
    nil)
  (get-parent [this]
    (.getParent this))

  javafx.scene.Scene
  (get-children [this]
    (get-children (.getRoot this)))
  (get-parent [this]
    (get-parent (.getRoot this)))

  javafx.stage.Window
  (get-children [this]
    (get-children (.getRoot (.getScene this))))
  (get-parent [this]
    (get-parent (.getRoot (.getScene this)))))



(defn find-child-by-id [^Node node id]
  (if (= (.getId node) id)
    node
    (reduce
      (fn [_ node]
        (if-let [found (find-child-by-id node id)]
          (reduced found)))
      nil
      (get-children node))))


(defn find-nearest-by-id
  ([node id]
    (when node
          (or (find-child-by-id node id)
              (find-nearest-by-id node id node))))
  ([^Node node id skip]
   (if-let [parent (get-parent node)]
     (if-let [found (if (= (.getId parent) id)
                      parent
                      (reduce
                        (fn [_ node]
                            (when-not (= node skip)
                                      (if-let [found (find-child-by-id node id)]
                                              (reduced found))))
                        nil
                        (get-children parent)))]
       found
       (find-nearest-by-id parent id parent)))))