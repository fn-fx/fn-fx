(ns fn-fx.render
  (:import (javafx.embed.swing JFXPanel)
           (java.lang.reflect Method Constructor Parameter))
  (:require [fn-fx.util :as util]
            [fn-fx.diff :as diff]
            [clojure.string :as str]
            [fn-fx.render-core :as render-core]
            [fn-fx.util.reflect-utils :as ru]))

(set! *warn-on-reflection* true)

(JFXPanel.)


