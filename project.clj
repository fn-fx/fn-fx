(require '[leiningen.core.project :as lein])
(require '[clojure.reflect :refer [resolve-class]])

(defn project-name!
  "Calculates the correct name for the project, based on the JVM used to *build* the code.  Marked with ! because it
  also has the side effect of enabling the correct profile based on the JVM version, so that library users don't have
  to continually remember to do it themselves."
  []
  (if (nil? (resolve-class (.getContextClassLoader (Thread/currentThread)) 'javafx.application.Platform))
    (let [java-version    (System/getProperty "java.specification.version")
          openjfx-version (str "openjfx" java-version)]
      (swap! lein/default-profiles
             #(assoc % :default (conj (:default %) (keyword openjfx-version))))
      (symbol (str "fn-fx/fn-fx-" openjfx-version)))
    (symbol "fn-fx/fn-fx-javafx")))

(defproject #=(project-name!) "0.5.0-SNAPSHOT"
  :description         "A declarative wrapper for JavaFX / OpenJFX"
  :url                 "https://github.com/fn-fx/fn-fx"
  :license             {:spdx-license-identifier "EPL-1.0"
                        :name                    "Eclipse Public License v1.0"
                        :url                     "http://www.eclipse.org/legal/epl-v10.html"}
  :min-lein-version    "2.8.1"
  :repositories        [["sonatype-snapshots" {:url "https://oss.sonatype.org/content/groups/public" :snapshots true}]
                        ["jcenter"            {:url "https://jcenter.bintray.com/" :snapshots false}]]
  :dependencies        [[org.clojure/clojure         "1.10.0"]
                        [org.reflections/reflections "0.9.11"]]
  :profiles            {:dev            {:source-paths ["src" "examples"]
                                         :dependencies [[com.github.javaparser/javaparser-core "3.10.0"]]
                                         :plugins      [[lein-release  "1.1.3"]
                                                        [lein-licenses "0.2.2"]
                                                        [lein-codox    "0.10.5"]
                                                        [cider/cider-nrepl "0.22.0-beta8"]
                                                        [refactor-nrepl "2.5.0-SNAPSHOT"]
                                                        [com.billpiel/sayid "0.0.17"]]}
                        :1.8            {:dependencies [[org.clojure/clojure "1.8.0"]]}
                        :1.9            {:dependencies [[org.clojure/clojure "1.9.0"]]}
                        :1.10           {:dependencies [[org.clojure/clojure "1.10.0"]]}
                        :openjfx11      ^:leaky   ; Ensure these dependencies "leak" through to the POM and JAR tasks
                                        {:dependencies [[org.openjfx/javafx-controls "11.0.2"]
                                                        [org.openjfx/javafx-swing    "11.0.2"]
                                                        [org.openjfx/javafx-media    "11.0.2"]
                                                        [org.openjfx/javafx-fxml     "11.0.2"]
                                                        [org.openjfx/javafx-web      "11.0.2"]
                                                        [org.openjfx/javafx-graphics "11.0.2"]]}
; Monocle seems pretty unreliable, especially on older JVMs, so we use Xvfb for CI instead.  I've left these details here for now in case we want to reintroduce Monocle at some point.
;                     :openjfx11-test    {:dependencies [[org.testfx/openjfx-monocle  "jdk-11+26" :exclusions [org.openjfx/javafx-base
;                                                                                                              org.openjfx/javafx-controls
;                                                                                                              org.openjfx/javafx-graphics]]]
;                                         :jvm-opts     ["-Djava.awt.headless=true"
;                                                        "-Dprism.verbose=true"
;                                                        "-Dtestfx.robot=glass"
;                                                        "-Dglass.platform=Monocle"
;                                                        "-Dmonocle.platform=Headless"
;                                                        "-Dprism.order=sw"
;                                                        "-Dprism.text=t2k"
;                                                        "-Dheadless.geometry=1024x768-24"]}
                       }
  :deploy-repositories [["snapshots" {:url           "https://clojars.org/repo"
                                      :username      :env/clojars_username
                                      :password      :env/clojars_password}]
                        ["releases"  {:url           "https://clojars.org/repo"
                                      :username      :env/clojars_username
                                      :password      :env/clojars_password}]]
  :codox               {:source-paths ["src"]
                        :metadata     {:doc "FIXME: write docs"}
                        :source-uri   "https://github.com/fn-fx/fn-fx/blob/master/{filepath}#L{line}"})
