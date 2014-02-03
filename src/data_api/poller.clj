(ns data-api.poller
  (:require [immutant.jobs :as jobs]
            [immutant.messaging :as msg]
            [clojurewerkz.quartzite.conversion :as qc]
            [clojurewerkz.quartzite.jobs :as qj]
            [clojurewerkz.quartzite.scheduler :as qs]
            [clojurewerkz.quartzite.triggers :as t]
            [clj-time.core :as time]
            [clj-time.coerce :as tc]
            [cheshire.core :refer :all])
  (:use [clojurewerkz.quartzite.jobs :only [defjob]]
        [clojurewerkz.quartzite.schedule.simple :only [schedule with-interval-in-seconds]]))


(defjob Poll [ctx]
        (let [job-data (qc/from-job-data ctx)
              msg-data {:date_created (.toDate (time/now))
                        :profile-id   (job-data "profile-id")}]
          (println "queuing...")
          (msg/publish "/queue/write" msg-data :persistent false)))

(defn build-job [profile-id]
  (qj/build
    (qj/of-type Poll)
    (qj/using-job-data {"profile-id" profile-id})
    (qj/with-identity (qj/key (str "jobs.poll." profile-id)))))

(defn get-trigger-key [profile-id]
  (t/key (str "triggers." profile-id)))

(defn build-trigger [trigger-key]
  (t/build
    (t/with-identity trigger-key)
    (t/start-now)
    (t/with-schedule (schedule
                       (with-interval-in-seconds 5)))))

(defn schedule-poll-job [profile-id]
  (qs/with-scheduler (delay (jobs/internal-scheduler))
                     (let [job (build-job profile-id)
                           tk (get-trigger-key profile-id)
                           trigger (build-trigger tk)]
                       (qs/schedule job trigger)
                       tk)))

;(defn poll [profile-id]
;  (println (str "polling" " " profile-id))
;  (let [msg-data {:date_created (.toDate (time/now))
;                  :profile-id   profile-id}]
;    (msg/publish "/queue/write" msg-data :persistent false))
;  )

(defn schedule-poll [service]
  (let [profile-id (str (tc/to-long (time/now)))]
    (println (str "Scheduling poll for " service " with id " profile-id))
    (schedule-poll-job profile-id)))