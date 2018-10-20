(defproject halgari/fn-fx "0.5.0-SNAPSHOT"
  :description      "A declarative wrapper for OpenJFX"
  :url              "https://github.com/fn-fx/fn-fx/tree/openjfx"
  :license          {:spdx-license-identifier "EPL-1.0"
                     :name                    "Eclipse Public License v1.0"
                     :url                     "http://www.eclipse.org/legal/epl-v10.html"}
  :min-lein-version "2.8.1"
  :repositories     [["sonatype-snapshots" {:url "https://oss.sonatype.org/content/groups/public" :snapshots true}]
                     ["jcenter"            {:url "https://jcenter.bintray.com/" :snapshots false}]]
  :dependencies     [[org.clojure/clojure         "1.9.0"]
                     [org.reflections/reflections "0.9.11"]]
  :profiles         {:dev            {:source-paths ["src" "examples"]
                                      :plugins      [[lein-release "1.0.5"]
                                                     [lein-licenses "0.2.2"]
                                                     [lein-codox    "0.10.4"]]
                                      :dependencies [[com.github.javaparser/javaparser-core "3.6.26"]
                                                     [org.jboss.forge.roaster/roaster-api   "2.20.2.Final"]
                                                     [org.jboss.forge.roaster/roaster-jdt   "2.20.2.Final"]]}
                     :openjfx11      {:dependencies [[org.openjfx/javafx-controls "11"]
                                                     [org.openjfx/javafx-swing    "11"]
                                                     [org.openjfx/javafx-media    "11"]
                                                     [org.openjfx/javafx-fxml     "11"]
                                                     [org.openjfx/javafx-web      "11"]
                                                     [org.openjfx/javafx-graphics "11"]]}
; Monocle seems pretty unreliable, especially on older JVMs, so we use Xvfb for CI instead.  I've left these details here for now in case we want to reintroduce Monocle at some point.
;                     :openjfx11-test {:dependencies [[org.testfx/openjfx-monocle  "jdk-11+26" :exclusions [org.openjfx/javafx-base
;                                                                                                           org.openjfx/javafx-controls
;                                                                                                           org.openjfx/javafx-graphics]]]
;                                      :jvm-opts     ["-Djava.awt.headless=true"
;                                                     "-Dprism.verbose=true"
;                                                     "-Dtestfx.robot=glass"
;                                                     "-Dglass.platform=Monocle"
;                                                     "-Dmonocle.platform=Headless"
;                                                     "-Dprism.order=sw"
;                                                     "-Dprism.text=t2k"
;                                                     "-Dheadless.geometry=1024x768-24"]}
                    })
