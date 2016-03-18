(defproject fn-fx "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/core.match "0.3.0-alpha4"]
                 [org.reflections/reflections "0.9.10"]]
  :profiles {:dev {:source-paths ["src" "examples"]
                   :dependencies [[org.clojure/core.match "0.3.0-alpha4"]]}})
