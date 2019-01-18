(ns fn-fx.init
  (:import  (javafx.embed.swing JFXPanel))
  (:require [fn-fx.render-core :as rc]
            [fn-fx.util.reflect-utils :as ru]))

(set! *warn-on-reflection* true)

(def init! (memoize (fn []
                      (import 'javax.swing.JFrame)
                      (JFXPanel.)
                      (doseq [enum (ru/enum-classes)]
                        (rc/register-enum-converter enum)))))
