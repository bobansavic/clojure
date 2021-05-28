(ns data-access-module-ispit.dam-routes
  (:use compojure.core ring.middleware.json)
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.json :as middleware]
            [ring.util.response :refer [response]]
            [ring.util.io :refer [string-input-stream]]
            [clojure.data.json :refer [json-str]]
            [data-access-module-ispit.handlers :as h]))

(defroutes app-routes
           (GET "/" [] h/simple-body-page)
           (GET "/hello" [] h/hello-name)
           (GET "/users/login" [] h/login-handler)
           (GET "/users/role" [] h/role-handler)
           (GET "/users/delete" [] h/delete-user-handler)
           (GET "/users/get" [] h/find-all-users-handler)
           (GET "/users/find-by-email" [] h/find-by-email-handler)
           (POST "/users/find-by-email-post" [] h/find-by-email-post-handler)
           (GET "/users/save" [] h/save-user-handler)
           (GET "/projects/delete" [] h/delete-project-handler))