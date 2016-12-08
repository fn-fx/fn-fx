(defproject fn-fx "0.1.0-SNAPSHOT"
  :description "A declarative wrapper for JavaFX2"
  :url "http://github.com/halgari/fn-fx"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0-alpha13"]
                 [org.reflections/reflections "0.9.10"]]

  :profiles {:dev {:source-paths ["src" "examples"]
                   :dependencies [[com.github.javaparser/javaparser-core "2.5.1"]
                                  [org.jboss.forge.roaster/roaster-api "2.19.0.Final"]
                                  [org.jboss.forge.roaster/roaster-jdt "2.19.0.Final"]]}}
  :plugins [[lein-release "1.0.5"]])