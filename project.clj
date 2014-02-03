(defproject data-api "0.1.0-SNAPSHOT"
  :description "New clj data-api"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [liberator "0.10.0"]
                 [compojure "1.1.6"]
                 [ring/ring-core "1.2.1"]
                 [ring/ring-json "0.2.0"]
                 [clojurewerkz/quartzite "1.1.0" :exclusions [org.quartz-scheduler/quartz]]
                 [cheshire "5.2.0"]
                 [clj-time "0.6.0"]]

  ;:ring {:handler data-api.server/handler
  ;       :adapter {:port 8000}}

  :jvm-opts ["-Xmx4g" "-server"]

  :immutant {:context-path "/"})
