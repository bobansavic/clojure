(ns data-access-module-ispit.handlers
  (:require [data-access-module-ispit.data-access-layer :as dal]))

(defn login-handler [req]
  (
    if (= (:authToken (:params req)) "agility")
    (do
      (let [result (dal/login (:email (:params req)) (:password (:params req)))]
        (cond nil? result)
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

    {
     :status 401
     :body   "Invalid token!"
     }
    )
  )

(defn find-by-email-handler [req]
  (if (= (:authToken (:params req)) "agility")
    (do
      (let [result (dal/find-by-email (:email (:params req)))]
        (if (nil? result)
          {
           :status  404
           :headers {"Content-Type" "text/text"}
           :body    "User not found."
           }
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
      :headers {"Content-Type" "text/text"}
      :body "Invalid token!"
      }
  ))

(defn find-by-email-post-handler [req]
  (
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
  if (= (:authToken (:params req)) "agility")
  {:status  200
   :headers {"Content-Type" "text/json"}
   :body    (->
              (dal/find-all-users))}
  {:status 401
   :body "Invalid token!"
   }
  ))

(defn role-handler [req]
  {:status  200
   :headers {"Content-Type" "text/json"}
   :body    (->
              (dal/find-user-role (:user_id (:params req))))})

(defn delete-user-handler [req]
  (
    if (= (:authToken (:params req)) "agility")
    (do
      (let [delete-result (dal/delete-user (:email (:params req)))]
            (if (nil? delete-result)
                     {:status 500
                      :body "Failed to delete user!"
                      }
                     {:status 200
                      :body "User successfully deleted!"
                      })
            )
      )
    {:status 401
     :body "Invalid token!"
     }
    )
  )

(defn save-user-handler [req]
  (
    if (= (:authToken (:params req)) "agility")
    (if (nil? (get-in req [:body "user_id"]))
      {:status 200
        :headers {"Content-Type" "text/json"}
        :body (->
                (dal/create-user req))
        }
      {:status 200
       :headers {"Content-Type" "text/json"}
       :body (->
               (dal/update-user req))
       }
      )
    {:status 401
     :body "Invalid token!"
     }
    )
  )

(defn create-project-handler [req]
  (
    if (= (:authToken (:params req)) "agility")
    (do
      (dal/create-project (:title (:params req)))
      {:status 201})
    {:status 401
     :body "Invalid token!"
     }
    )
  )

(defn delete-project-handler [req]
  (
    if (= (:authToken (:params req)) "agility")
    (do
      (dal/delete-project (:project_id (:params req)))
      {:status 201})
    {:status 401
     :body "Invalid token!"
     }
    )
  )
