(ns fn-fx.core-test
  (:require [fn-fx.init :as fi]
            [clojure.test :refer :all]
            ))

(println "\n☔️ Running tests on Clojure" (clojure-version) "/ JVM" (System/getProperty "java.version") (str "(" (System/getProperty "java.vm.name") " v" (System/getProperty "java.vm.version") ")"))

(fi/init!)
