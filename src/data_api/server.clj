(ns data-api.server
  (:use [data-api.http :only [app]]
        [liberator.dev :refer [wrap-trace]]
        [ring.middleware.params :refer [wrap-params]]
        [ring.middleware.multipart-params :only [wrap-multipart-params]]
        [ring.util.response :only [header]]
        [compojure.handler :only [api]]))

(def handler
  (-> app
      api
      (wrap-params)
      (wrap-multipart-params)
      ;(wrap-trace :header :ui)
      ))
