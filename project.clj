(defproject halgari/fn-fx "0.5.0-SNAPSHOT"
  :description      "A declarative wrapper for OpenJFX"
  :url              "http://github.com/fn-fx/fn-fx-openjfx"
  :license          {:spdx-license-identifier "EPL-1.0"
                     :name                    "Eclipse Public License v1.0"
                     :url                     "http://www.eclipse.org/legal/epl-v10.html"}
  :min-lein-version "2.8.1"
  :repositories     [["sonatype-snapshots" {:url "https://oss.sonatype.org/content/groups/public" :snapshots true}]
                     ["jcenter"            {:url "https://jcenter.bintray.com/" :snapshots false}]
                     ["jitpack"            {:url "https://jitpack.io"}]]
  :dependencies     [[org.clojure/clojure         "1.9.0"]
                     [org.reflections/reflections "0.9.11"]
                     [org.openjfx/javafx-controls "11"]
                     [org.openjfx/javafx-swing    "11"]
                     [org.openjfx/javafx-media    "11"]
                     [org.openjfx/javafx-fxml     "11"]
                     [org.openjfx/javafx-web      "11"]
                     [org.openjfx/javafx-graphics "11"]]
  :profiles         {:test {:dependencies [[org.testfx/openjfx-monocle "jdk-11+26" :exclusions [org.openjfx/javafx-base
                                                                                                org.openjfx/javafx-controls
                                                                                                org.openjfx/javafx-graphics]]]
                            :jvm-opts     ["-Dprism.verbose=true" "-Dtestfx.robot=glass" "-Dglass.platform=Monocle" "-Dmonocle.platform=Headless" "-Dprism.order=sw" "-Dprism.text=t2k"]}
                     :dev  {:source-paths ["src" "examples"]
                            :plugins      [[lein-release "1.0.5"]
                                           [lein-licenses "0.2.2"]
                                           [lein-codox    "0.10.4"]]
                            :dependencies [[com.github.javaparser/javaparser-core "3.6.25"]
                                           [org.jboss.forge.roaster/roaster-api   "2.20.2.Final"]
                                           [org.jboss.forge.roaster/roaster-jdt   "2.20.2.Final"]]}}
  )
