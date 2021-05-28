(ns data-access-module-ispit.handlers
  (:require [clojure.pprint :as pp]
            [clojure.string :as str]
            [clojure.data.json :as json]
            [data-access-module-ispit.data-access-layer :as dal]))

(defn simple-body-page [req]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    "Hello World"})

(defn hello-name [req]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    (->
              (pp/pprint req)
              (str "Hello " (:name (:params req))))})

; Return List of People
(defn login-handler [req]
  (
    if (= (:authToken (:params req)) "agility")
    (do
      (let [result (dal/login (:username (:params req)) (:password (:params req)))]
        (cond (nil? result)
              {
               :status  404
               :headers {"Content-Type" "text/text"}
               :body    (-> "User not found.")
               }
              :else
              {
               :status  200
               :headers {"Content-Type" "application/json"}
               :body    (-> result)
               }
          )
        )
      )
    {
     :status 401
     :body "Invalid token!"
     }
  ))

(defn find-by-email-handler [req]
  (
    if (= (:authToken (:params req)) "agility")
    (do
      (let [result (dal/find-by-email (:email (:params req)))]
      (cond nil? result)
      {
       :status  404
       :headers {"Content-Type" "text/text"}
       :body    "User not found."
       }
      :else
      {
       :status  200
       :headers {"Content-Type" "application/json"}
       :body    (-> result)
       })
      )
     {
      :status 401
      :headers {"Content-Type" "text/text"}
      :body "Invalid token!"
      }
  ;if (= (:authToken (:params req)) "agility")
  ;  {:status  200
  ;   :headers {"Content-Type" "application/json"}
  ;   :body    (->
  ;              (dal/find-by-email (:email (:params req))))}
  ;  {:status 401
  ;   :body "Invalid token!"}
  ))

(defn find-by-email-post-handler [req]
  (
    ;if (= (:authToken (:params req)) "agility")
    if (= (get-in req [:body "authToken"]) "agility")
    {
     :status  200
     :headers {"Content-Type" "application/json"}
     :body    (-> "Success!")
     }
    {
     :status 401
     :headers {"Content-Type" "text/text"}
     :body (get-in req [:body "authToken"])
     }
    ))

(defn find-all-users-handler [req] (
  if (= (:authToken (:params req)) "a")
  ;{:status  200
  ; :headers {"Content-Type" "text/json"}
  ; :body    (->
  ;            (dal/find-all-users))}
  ;{:status 401
  ; :body "Invalid token!"
  ; }
  {:status 200
   :body "Cool!"
   }
  {:status 400
   :body "Not cool!"
   }
  ))

(defn role-handler [req]
  {:status  200
   :headers {"Content-Type" "text/json"}
   :body    (->
              (dal/find-user-role (:user_id (:params req))))})

(defn delete-user-handler [req]
  (
    if (= (:authToken (:params req)) "token")
    (dal/delete-user (:user_id (:params req)))
    {:status 401
     :body "Invalid token!"
     }
    )
  )

(defn save-user-handler [req]
  (
    if (= (:authToken (:params req)) "token")
    (if (nil? (:user_id (:params req)))
      ({:status 200
        :headers {"Content-Type" "text/json"}
        :body (->
                (dal/update-user
                  (:firstname (:params req))
                  (:lastname (:params req))
                  (:email (:params req))
                  (:username (:params req))
                  (:password (:params req))
                  (:user_id (:params req)))
                ((dal/bind-role (:user_id (:params req)) (:role_id (:params req)))))
        })
      (dal/create-user req)
      )
    {:status 401
     :body "Invalid token!"
     }
    )
  )

(defn delete-project-handler [req]
  (
    if (= (:authToken (:params req)) "token")
    (dal/delete-user (:project_id (:params req)))
    {:status 401
     :body "Invalid token!"
     }
    )
  )
