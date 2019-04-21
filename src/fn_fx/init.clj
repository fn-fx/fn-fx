(ns fn-fx.init
  (:import (javafx.application Platform)))


(def ^:private javafx-initialized (atom false))



;; Initialize the Java FX platform
(defn init-javafx! []
  (swap! javafx-initialized
         (fn [is-initialized]
           (if-not is-initialized
             (do
               (println "*compile-files*:" *compile-files*)

               (if *compile-files*

                 ;; AOT compilation will trigger JavaFX initialization, which starts the JavaFX application thread
                 ;; We need to make it a daemon thread, otherwise compilation will never finish
                 (let [t (Thread. #(Platform/startup (fn [] nil)))]
                   (.setDaemon t true)
                   (.start t)
                   (.join t))

                 (Platform/startup (fn [] nil)))
               true)
             true))))

