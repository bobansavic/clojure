(ns data-access-module-ispit.core
  (:require [compojure.core :refer :all]
            [clojure.java.io :as io]
            [org.httpkit.server :as server]
            [ring.middleware.json :as js]
            [ring.middleware.defaults :refer :all]
            [data-access-module-ispit.dam-routes :as r]
            [data-access-module-ispit.data-access-layer :as dal]
            [data-access-module-ispit.standalone-starter :as starter]))

(def my-site-defaults
  "A copy of a default configuration (ring.middleware.defaults/site-defaults)
   with the exception of anti-forgery (in this case it's false)."
  {:params    {:urlencoded true
               :multipart  true
               :nested     true
               :keywordize true}
   :cookies   true
   :session   {:flash true
               :cookie-attrs {:http-only true, :same-site :strict}}
   :security  {:anti-forgery   false
               :xss-protection {:enable? true, :mode :block}
               :frame-options  :sameorigin
               :content-type-options :nosniff}
   :static    {:resources "public"}
   :responses {:not-modified-responses true
               :absolute-redirects     true
               :content-types          true
               :default-charset        "utf-8"}})

(defn -main
  [& args]
  (if (= "standalone" (str (last args)))
    (let [standalone-db (slurp (io/resource "test-database-name.txt"))]
      (println "Data Access Module started as a standalone webservice. Attempting to create test database" standalone-db"...")
      (starter/exec-sql-files standalone-db))
    (let [agility "agilitydb"]
       (println "Data Access Module started as an Agility-dependant webservice. Updating JDBC configuration...")
       (dal/update-db agility)
       (println "Database name: " agility))
    )
  (let [port (Integer/parseInt (or (System/getenv "PORT") "3000"))]
    (server/run-server (js/wrap-json-body (js/wrap-json-response (wrap-defaults #'r/app-routes api-defaults))) {:port port})
    (println (str "Running webserver at port: " port "/")))
  )
