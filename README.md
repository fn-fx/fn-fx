[![Build Status](https://travis-ci.com/fn-fx/fn-fx.svg?branch=master)](https://travis-ci.com/fn-fx/fn-fx)
[![JavaFX build on Clojars](https://img.shields.io/clojars/v/fn-fx/fn-fx-javafx.svg)](https://clojars.org/fn-fx/fn-fx-javafx)
[![OpenJFX build on Clojars](https://img.shields.io/clojars/v/fn-fx/fn-fx-openjfx11.svg)](https://clojars.org/fn-fx/fn-fx-openjfx11)
<!-- [![Dependencies Status](https://versions.deps.co/fn-fx/fn-fx/status.svg)](https://versions.deps.co/fn-fx/fn-fx) -->

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
the event-callback handed to the initial call to `render`.

## User components
The `defui` macro generates a "user component" that is not a UI component, but a rendering function, and an
optional differ function. The render method on this component is only invoked when the properties to the component
change. `defui` is most often used to optimize re-rendering as whole sections of the UI can be ignored during rendering
and diffing if the properties of the component haven't changed since the last render cycle.

# Usage

## Tested Versions

`fn-fx` is [tested on](https://travis-ci.com/fn-fx/fn-fx):

|                           |  JVM 1.8 (Oracle) | JVM 1.8 (OpenJDK) | JVM 11 (Oracle) | JVM 11 (OpenJDK) |
|                      ---: |   :---:           |  :---:            |  :---:          |  :---:           |
| Clojure 1.7.0             | ❌<sup>1</sup>    | ❌<sup>1,2</sup>  | ❌<sup>1</sup> | ❌<sup>1</sup>   |
| Clojure 1.8.0             | ✅                | ❌<sup>2</sup>    | ✅             | ✅               |
| Clojure 1.9.0             | ✅                | ❌<sup>2</sup>    | ✅             | ✅               |
| Clojure 1.10.0 (snapshot) | ✅                | ❌<sup>2</sup>    | ✅             | ✅               |

<sup>1</sup> For now we've decided to only test back as far as Clojure 1.8.0.  If anyone needs this tested on older versions of Clojure, PRs are welcome!

<sup>2</sup> Currently, [there is no easy way to obtain OpenJFX for OpenJDK 1.8](https://github.com/fn-fx/fn-fx/issues/71), so it is not supported.

## A Note on JavaFX vs OpenJFX

JavaFX was included in Oracle JRE versions 1.7u6 through 10, but has never been bundled in any version of OpenJDK, nor is
it bundled in any edition of JRE (Oracle or OpenJDK) from 11 onward.  In JRE 11 and up, these capabilities are instead provided by a
separate library called [OpenJFX](https://openjfx.io/) that is not part of the default JRE installation.

In an attempt to hide this backwards-compatibility-breaking mess from the library user to the maximum extent possible,
`fn-fx` is provided as multiple artifacts in Clojars:
1. **`fn-fx-javafx`** - for code that targets JRE versions that bundle JavaFX (i.e. 1.7u6 through 10)
2. **`fn-fx-openjfx##`** - for code that targets JRE versions that do not bundle JavaFX (i.e. 11 and up).  Note that "##" is a specific number (currently only "11" is provided, yielding `fn-fx-openjfx11`)

Although these artifacts are code-identical, they have different upstream dependencies that are JRE-version-specific, so
please make sure you select the correct artifact based on the version of the JRE that will be used **at runtime**.

The regrettably [tight coupling between OpenJFX and JRE versions](http://mail.openjdk.java.net/pipermail/openjfx-discuss/2018-October/000061.html)
makes it more challenging for the project to maintain both forward and backward compatibility across JRE versions, but our
intent is to maintain the broadest practical compatibility, at least until JRE 11+ is widely deployed.  Whether this continues
to be done via multiple artifacts, or some other mechanism is an open question.  If you have suggestions / comments / preferences
on this, please [let us know](https://github.com/fn-fx/fn-fx/issues/new?template=Support_question.md)!

## Trying the Library Out

### With `lein-try`

Kicking the tyres is a snap with the handy [`lein-try`](https://github.com/avescodes/lein-try) plugin:

```shell
$ lein try fn-fx/fn-fx-openjfx11 "0.5.0-SNAPSHOT"   # If you're running JRE 11
$ lein try fn-fx/fn-fx-javafx "0.5.0-SNAPSHOT"      # If you're running on JRE 1.7u6-10
```

### With a Placeholder Project

Or, if you'd rather not use `lein-try`, you could create a new folder and put a `project.clj` file in it, something like this:

```clojure
(defproject your-name/my-first-fn-fx-project "0.1.0-SNAPSHOT"
  :description      "My first fn(fx) project"
  :min-lein-version "2.8.1"
  :dependencies     [[org.clojure/clojure "1.9.0"]
                     ; Pick one, and only one, of the following dependencies:
                     [fn-fx/fn-fx-openjfx11 "0.5.0-SNAPSHOT"]    ; If you're running JRE 11
                     [fn-fx/fn-fx-javafx "0.5.0-SNAPSHOT"]       ; If you're running JRE 1.7u6-10
])
```

Then start a REPL from that directory:

```shell
$ lein repl
nREPL server started on port 55554 on host 127.0.0.1 - nrepl://127.0.0.1:55554
REPL-y 0.3.7, nREPL 0.2.12
Clojure 1.9.0
OpenJDK 64-Bit Server VM 11.0.1+13
    Docs: (doc function-name-here)
          (find-doc "part-of-name-here")
  Source: (source function-name-here)
 Javadoc: (javadoc java-object-or-class-here)
    Exit: Control+D or (exit) or (quit)
 Results: Stored in vars *1, *2, *3, an exception in *e

user=> 
```

### Using the Clojure Command Line Tools

Not yet implemented, though this is being tracked as [issue #49](https://github.com/fn-fx/fn-fx/issues/49).  PRs welcome!

## Making API Calls from the REPL

Once you're in a REPL you can make standard `fn-fx` API calls:

```clojure
user=> (require '[fn-fx.fx-dom :as dom])
nil
user=> (require '[fn-fx.controls :as ui])
nil
user=> ; Do awesome GUI stuff here!
```

[API documentation is published here](https://fn-fx.github.io/fn-fx/), but note that these are currently *\*cough\**
"limited" due to a lack of docstrings in the code.  Issue #s [21](https://github.com/fn-fx/fn-fx/issues/21),
[26](https://github.com/fn-fx/fn-fx/issues/26), [27](https://github.com/fn-fx/fn-fx/issues/27), and
[28](https://github.com/fn-fx/fn-fx/issues/28) go into more detail on this, and any additional comments / feedback / PRs
for documentation are welcome!

## Running the Examples

Note that [the examples](https://github.com/fn-fx/fn-fx/tree/master/examples) are not deployed to Clojars, so to run
those you'll need to clone the project locally, and run a REPL from within the cloned directory:

```
$ cd <directory where you like to put your GitHub clones>
$ git clone https://github.com/fn-fx/fn-fx.git
Cloning into 'fn-fx'...
remote: Enumerating objects: 47, done.
remote: Counting objects: 100% (47/47), done.
remote: Compressing objects: 100% (27/27), done.
remote: Total 550 (delta 23), reused 39 (delta 20), pack-reused 503
Receiving objects: 100% (550/550), 301.67 KiB | 4.02 MiB/s, done.
Resolving deltas: 100% (264/264), done.
$ cd fn-fx
$ lein repl
nREPL server started on port 52005 on host 127.0.0.1 - nrepl://127.0.0.1:52005
REPL-y 0.3.7, nREPL 0.2.12
Clojure 1.9.0
OpenJDK 64-Bit Server VM 11.0.1+13
    Docs: (doc function-name-here)
          (find-doc "part-of-name-here")
  Source: (source function-name-here)
 Javadoc: (javadoc java-object-or-class-here)
    Exit: Control+D or (exit) or (quit)
 Results: Stored in vars *1, *2, *3, an exception in *e

user=>
```

Once you have a REPL up within the cloned directory, the examples may be run as follows:

* **[01 Hello world](https://github.com/fn-fx/fn-fx/blob/master/examples/getting_started/01_hello_word.clj)**: `(require '[getting-started.01-hello-word :as hello]) (hello/-main)`
* **[02 Form](https://github.com/fn-fx/fn-fx/blob/master/examples/getting_started/02_form.clj)**: `(require '[getting-started.02-form :as form]) (form/-main)`
* **[Shapes 3D](https://github.com/fn-fx/fn-fx/blob/master/examples/other_examples/shapes_3d.clj)**: `(require '[other-examples.shapes-3d :as shapes-3d]) (shapes-3d/-main)`
* **[Todo](https://github.com/fn-fx/fn-fx/blob/master/examples/other_examples/todo.clj)**: `(require '[other-examples.todo :as todo]) (todo/-main)`
* **[Menu Bar](https://github.com/fn-fx/fn-fx/blob/master/examples/other_examples/menubar.clj)**: `(require '[other-examples.menubar :as menu]) (menu/-main)`

Each example will open a window that can be closed at any time (and reopened by calling `-main` again).

# License
Copyright (c) 2016 Timothy Baldridge. All rights reserved.
The use and distribution terms for this software are covered by the
Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
which can be found in the file epl-v10.html at the root of this distribution.
By using this software in any fashion, you are agreeing to be bound by
the terms of this license.
You must not remove this notice, or any other, from this software.
