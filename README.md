[![Build Status](https://travis-ci.com/fn-fx/fn-fx.svg?branch=master)](https://travis-ci.com/fn-fx/fn-fx)
[![Dependencies Status](https://versions.deps.co/fn-fx/fn-fx/status.svg)](https://versions.deps.co/fn-fx/fn-fx)

**Assistance / contributions welcome!**  Please see the [contribution guide](https://github.com/fn-fx/fn-fx/blob/master/.github/CONTRIBUTING.md) for more details.

# fn(fx)
This library provides a functional, declarative wrapper around JavaFX / OpenJFX. The goals are to provide a "Virtual DOM"
interface over the OOP mutability JavaFX / OpenJFX embraces.

# Rationale
While the web has taken over many aspects of GUI programming that normally would have been implemented in JavaFX / OpenJFX, it's
still important to recognize that a certain amount of complexity is involved in adopting a web based GUI. Programmers must
now write in several other languages, setup web servers, and handle network data transfers, when all that was required
was a GUI to some backend process. Sometimes a desktop UI really is the simplest option.

However, clojure developers have traditionally shied away from adopting technologies such as Swing and JavaFX / OpenJFX for fear
of delving into the mess of mutability that is GUI programming.

This is the niche that fn(fx) attempts to fill: providing a functional interface over JavaFX / OpenJFX.

# Basic Overview
fn(fx) requires that users express their UI via data, and calls to a function known as "ui". This function constructs
a quasi-immutable datastructure that can easily be diffed against other components. We say "quasi-immutable", since
some of the fields on the structure are mutated, but only once, from nil to a known value, never from a value
to another value. This tree of components can then be handled by several functions:

* `(fn-fx.fx-dom/render component event-callback)` - This function takes a virtual dom (component tree) and
renders it, returning an opaque structure that can be used to later update the UI with a new virtual dom.
`event-callback` is a function that will be handed events from the UI, more on that later.
* `(fn-fn.fx-dom/update-dom prev-state new-dom)` - Given a value returned from a previous call to `render`
or `update-dom` this function will diff `new-dom` against the dom used to create `prev-state` the resulting diff
will be used to make minimal changes to the UI where required.

## Event handling
Events are data, and are attached to components where `EventHandler` instances would normally be used. Thus creating
a button with the property `:on-action {:event :clicked!}` would result in a button that sent `{:event :clicked!}` to
the event-callback handed to the initial call to `render`

## User components
The `defui` macro generates a "user component" that is not a UI component, but a rendering function, and an
optional differ function. The render method on this component is only invoked when the properties to the component
change. `defui` is most often used to optimize re-rendering as whole sections of the UI can be ignored during rendering
and diffing if the properties of the component haven't changed since the last render cycle.

# Example

```clojure

(ns getting-started.02-form
  (:require [fn-fx.fx-dom :as dom]
            [fn-fx.diff :refer [component defui render should-update?]]
            [fn-fx.controls :as ui]))

(def firebrick
  (ui/color :red 0.69 :green 0.13 :blue 0.13))

;; The main login window component, notice the authed? parameter, this defines a function
;; we can use to construct these ui components, named "login-form"
(defui LoginWindow
  (render [this {:keys [authed?]}]
    (ui/grid-pane
      :alignment :center
      :hgap 10
      :vgap 10
      :padding (ui/insets
                 :bottom 25
                 :left 25
                 :right 25
                 :top 25)
      :children [(ui/text
                   :text "Welcome"
                   :font (ui/font
                           :family "Tahoma"
                           :weight :normal
                           :size 20)
                   :grid-pane/column-index 0
                   :grid-pane/row-index 0
                   :grid-pane/column-span 2
                   :grid-pane/row-span 1)

                 (ui/label
                   :text "User:"
                   :grid-pane/column-index 0
                   :grid-pane/row-index 1)

                 (ui/text-field
                   :id :user-name-field
                   :grid-pane/column-index 1
                   :grid-pane/row-index 1)

                 (ui/label :text "Password:"
                   :grid-pane/column-index 0
                   :grid-pane/row-index 2)

                 (ui/password-field
                   :id :password-field
                   :grid-pane/column-index 1
                   :grid-pane/row-index 2)

                 (ui/h-box
                   :spacing 10
                   :alignment :bottom-right
                   :children [(ui/button :text "Sign in"
                                :on-action {:event :auth
                                            :fn-fx/include {:user-name-field #{:text}
                                                            :password-field #{:text}}})]
                   :grid-pane/column-index 1
                   :grid-pane/row-index 4)

                 (ui/text
                   :text (if authed? "Sign in was pressed" "")
                   :fill firebrick
                   :grid-pane/column-index 1
                   :grid-pane/row-index 6)])))

;; Wrap our login form in a stage/scene, and create a "stage" function
(defui Stage
       (render [this args]
               (ui/stage
                 :title "JavaFX Welcome"
                 :shown true
                 :scene (ui/scene
                          :root (login-window args)))))

(defn -main []
  (let [;; Data State holds the business logic of our app
        data-state (atom {:authed? false})

        ;; handler-fn handles events from the ui and updates the data state
        handler-fn (fn [{:keys [event] :as all-data}]
                     (println "UI Event" event all-data)
                     (case event
                       :auth (swap! data-state assoc :authed? true)
                       (println "Unknown UI event" event all-data)))

        ;; ui-state holds the most recent state of the ui
        ui-state (agent (dom/app (stage @data-state) handler-fn))]

    ;; Every time the data-state changes, queue up an update of the UI
    (add-watch data-state :ui (fn [_ _ _ _]
                                (send ui-state
                                      (fn [old-ui]
                                        (dom/update-app old-ui (stage @data-state))))))))

```

# JavaFX vs OpenJFX

JavaFX is included in JRE versions 1.7u6 through 10, but was unbundled as of JRE 11.  In that JRE version and beyond,
these capabilities are instead provided by a separate library called [OpenJFX](https://openjfx.io/) that is not part of
the default JRE installation.

As a result of this backwards-compatibility-breaking change, you will need to choose the right "edition" of `fn-fx`,
based on the JRE version your code will ultimately run on.  The two editions, while identical from a developer's
perspective, are deployed to Clojars under different names, to ensure that the correct dependencies are expressed for
each case.

These editions are available as:

```clojure
[fn-fx/fn-fx-javafx    "...version..."]   ; For JavaFX-equipped JREs (i.e. 1.7u6 through 10)
[fn-fx/fn-fx-openjfx11 "...version..."]   ; To use OpenJFX 11 (which targets JRE 11)
```

The regrettably [tight coupling between OpenJFX and JRE versions](http://mail.openjdk.java.net/pipermail/openjfx-discuss/2018-October/000061.html)
makes it more challenging for `fn-fx` to maintain both forward and backward compatibilty across JRE versions, but our
intent is to attempt to maintain the maximum compatibilty possible, at least until JRE 11 is widely deployed.  Whether
this continues to be done via "editions" as described here, or separate branches of `fn-fx` is an open question.  If
you have suggestions / comments / preferences on this, please
[let us know](https://github.com/fn-fx/fn-fx/issues/new?template=Support_question.md)!

# License
Copyright (c) 2016 Timothy Baldridge. All rights reserved.
The use and distribution terms for this software are covered by the
Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
which can be found in the file epl-v10.html at the root of this distribution.
By using this software in any fashion, you are agreeing to be bound by
the terms of this license.
You must not remove this notice, or any other, from this software.
