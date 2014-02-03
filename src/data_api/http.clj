(ns data-api.http
  (:require [liberator.core :refer [resource defresource]]
            [compojure.core :refer [defroutes ANY]]
            [cheshire.core :refer :all]
            [data-api.poller :refer :all]
            ))

(defresource poll []
             :available-media-types ["application/json"]
             :allowed-methods [:post]
             :new? false
             :post! (fn [ctx] (schedule-poll "twitter"))
             :respond-with-entity? true
             :handle-ok "")

(defroutes app
           (ANY "/" [] (resource :available-media-types ["text/html"]
                                    :handle-ok "<html>Hello, Internet.</html>"))
           (ANY "/poll" [] (poll))
           )

