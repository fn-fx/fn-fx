(ns fn-fx.init
  (:import (javafx.application Platform)
           (javafx.embed.swing JFXPanel)))


(def ^:private javafx-initialized (atom false))

;; Platform/startup is available only starting with JavaFX 9
(defmacro ^:private start-javafx []
  (let [version (System/getProperty "javafx.runtime.version")
        major-version (try (Integer/parseInt (re-find #"^[0-9]+" version))
                           ; when we can't find out what JavaFX version we have, we go for 8
                           (catch Exception e 8))]
    (if (< major-version 9)
      '(JFXPanel.)
      '(Platform/startup (fn [] nil)))))


;; Initialize the Java FX platform
(defn init-javafx! []
  (swap! javafx-initialized
         (fn [is-initialized]
           (if-not is-initialized
             (do

               (if *compile-files*

                 ;; AOT compilation will trigger JavaFX initialization, which starts the JavaFX application thread
                 ;; We need to make it a daemon thread, otherwise compilation will never finish
                 (let [t (Thread. #(start-javafx))]
                   (.setDaemon t true)
                   (.start t)
                   (.join t))

                 (start-javafx))
               true)
             true))))

