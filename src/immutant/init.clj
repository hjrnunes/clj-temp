(ns immutant.init
  (:import (java.util Date))
  (:require [immutant.daemons :as daemon]
            [immutant.jobs :as jobs]
            [immutant.messaging :as msg]
            [immutant.web :as web]
            [immutant.util :as util]
            [clojurewerkz.quartzite.scheduler :as qs]
            [clojurewerkz.quartzite.jobs :as qj]
            [data-api.core :as core]
            [data-api.server :as server]))

;; http #########################
(web/start server/handler)

;; queues #########################

(msg/start "/queue/write" :durable false)


;; scheduler ######################

;; initialize and start quartz scheduler
;(qs/initialize)
;(qs/start)
;; deamons #########################

;; deamon function

(defn writerd [task]
  (println (nil? task))
  (println (str "got msg! " task)))

;; Controls the state of our daemon
(def writerd-done (atom false))

;; start
(defn writerd-start []
  (reset! writerd-done false)
  (while (not @writerd-done)
    (try
      (let [task (msg/receive "/queue/write")]
        (println "demon got msg!")
        (writerd task))
      (catch Exception e
        (str "caught exception: " (.getMessage e)))))
  )

;; stop
(defn writed-stop []
  (reset! writerd-done true))

;; register deamon
(daemon/daemonize "writerd"
                  writerd-start
                  writed-stop
                  :singleton true)




