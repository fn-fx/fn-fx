(ns fn-fx.util.filegen
  (:gen-class)
  (:require [clojure.string :as str]
            [fn-fx.util :as util]
            [clojure.pprint :as pprint]
            [fn-fx.render-core :as render-core]
            [fn-fx.util.reflect-utils :as ru])
  (:import (java.lang.reflect Constructor Method Executable)))

;; This file is used to generate fn-fx.controls


(def ^:dynamic *forms*)

(defn gen-value-ctor [^Class to-tp]
  (let [ctors (ru/get-value-ctors to-tp)
        props (set (for [{:keys [prop-names-kw prop-names-sym]} ctors
                         pairs (zipmap prop-names-sym
                                       prop-names-kw)]
                     pairs))
        symname (symbol (util/camel->kabob (last (str/split (.getName to-tp) #"\."))))

        ctor-helper `(~'defmacro ~symname [~'& {:as ~'props}]
                       (render-core/value-type-impl ~(symbol (.getName to-tp)) ~'props))]
    (swap! *forms* conj ctor-helper)
    (swap! *forms* conj `(render-core/register-value-converter ~(symbol (.getName to-tp))))))

(defn gen-control-ctor [^Class to-tp]
  (let [{:keys [prop-types] :as ctor-def}  (ru/get-control-ctor to-tp)
        symname (symbol (util/camel->kabob (last (str/split (.getName to-tp) #"\."))))
        kw-name (keyword (.getName to-tp))

        ctor-helper `(~'defmacro ~symname [& {:as ~'props}]
                       (render-core/component-impl
                         ~kw-name
                         ~'props))]
    (when ctor-def
      (swap! *forms* conj ctor-helper))))

(defn pretty-printer
    [o]
    (binding [pprint/*print-right-margin* 100
                          pprint/*print-miser-width* 60
              *print-meta* true]
          (pprint/with-pprint-dispatch pprint/code-dispatch
                                             (pprint/pprint o))))
(defn emit-all []
  (binding [*forms* (atom ['(ns fn-fx.controls
                              (:refer-clojure :exclude [when])
                             (:require [fn-fx.render-core :as render-core]
                                       [fn-fx.diff :as diff]))
                           `(set! *warn-on-reflection* true)])]
    (doseq [vt ru/value-types]
      (println "Emitting Value Type:" vt)
      (gen-value-ctor vt))

    (doseq [klass ru/control-types]
      (println "Emit control builder:" klass)
      (gen-control-ctor klass))

    (let [s (with-out-str
                (doseq [form @*forms*]
                  (println)
                  (pretty-printer form)))]
      (spit "./src/fn_fx/controls.clj" s))
    (println "DONE ")))
(comment

  (emit-all)

  )
