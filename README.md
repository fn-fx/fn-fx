# fn(fx)
This library provides a functional, declarative wrapper around JavaFX. The goals are to provide a "Virtual DOM" like
interface over the common OOP mutability JavaFX embrances. 

# Rationale
While the web has taken over many aspects of GUI programming that normally would have been implemented in JavaFX, its
still important to notice that a certain amount of complexity is involved in adopting a web based GUI. Programmers must
now write in several other languages, setup web servers, and handle network data transfers, when all that was required
was a GUI to some backend process. Sometimes a desktop UI really is the simplest option. 

However, clojure developers have tranditionally shied away from adopting technologies such as Swing and JavaFX for fear
of delving into the mess of mutability that is GUI programming. 

This is where fn(fx) will attempt to help, by providing a functional interface over JavaFX

# How it works
A developer describes a interface via a nested datastructure that maps quite closely to the naming conventions of JavaFX:

```clojure 
(ns getting_started.hello_world
    (:require [fn-fx.render :as render]))

;; Describe the GUI
(def init-state
  {:type           :Stage
   :fn-fx/children #{:root}
   :title          "Hello World!"
   :scene          {:type           :Scene
                    :width          300
                    :height         250
                    :fn-fx/children #{:root}
                    :root           {:type           :StackPane
                                     :fn-fx/children #{:children}
                                     :children       [{:type     :Button
                                                       ;; When this action is fired, provide the tag data
                                                       ;; as part of the event handed to the event handler
                                                       :onAction {:tag :say-hello}
                                                       :text     "Say Hello World"}]}}})


(let [c (render/create-root init-state)]
  ;; Attach a handler
  (render/update-handler! c (fn [evt]
                              ;; When we get an event, resize the window
                              (render/update! c (update-in init-state [:scene :width] + 10))
                              (println "Hello world! : " evt)))
  (render/show! c))
```

In this example the root is considered a window, the root can be updated by applying a new description datastructure
to the component. The new state and the old state are diffed and the smallest changes are made to the GUI. 

If this process sounds alot like projects such as ReactJS, that's not surprising as this entire project could probably be
described as "ReactJS for JavaFX". 

# License
Copyright (c) Timothy Baldridge. All rights reserved.
The use and distribution terms for this software are covered by the
Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
which can be found in the file epl-v10.html at the root of this distribution.
By using this software in any fashion, you are agreeing to be bound by
the terms of this license.
You must not remove this notice, or any other, from this software.
