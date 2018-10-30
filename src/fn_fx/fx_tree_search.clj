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
    (.getParent this)))


;; Covers JavaFX objects which are containers, but are not necessarily nodes (like Stage, Scene)
(defprotocol IContainer
  (get-root-node [this]))

(extend-protocol IContainer
  javafx.stage.Window
  (get-root-node [this]
    (let [scene (.getScene this)]
      (and scene (.getRoot scene))))

  javafx.scene.Scene
  (get-root-node [this]
    (.getRoot this))

  javafx.scene.Parent
  (get-root-node [this]
    this))


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
  ([container id]
   (let [node (and container (get-root-node container))]
     (when node
       (or (find-child-by-id node id)
           (find-nearest-by-id node id node)))))
  ([^Node node id skip]
   (if-let [parent (get-parent node)]
     (if-let [found (reduce
                      (fn [_ node]
                        (when-not (= node skip)
                          (if-let [found (find-child-by-id node id)]
                            (reduced found))))
                      nil
                      (get-children parent))]
       found
       (find-nearest-by-id parent id parent)))))