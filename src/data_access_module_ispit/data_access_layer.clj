(ns data-access-module-ispit.data-access-layer
  (:use ring.util.response)
  (:require [clojure.java.jdbc :as j]
            [clojure.data.json :as json]))

(def global-db (atom
                 {:dbtype "mssql"
                  :dbname nil
                  :host "localhost"
                  :user "sa"
                  :password ""}))

(defn get-db []
  @global-db)

(defn update-db [dbname]
  (swap! global-db assoc :dbname dbname)
  )

(def delete-user-query
  "DELETE FROM r FROM Users_roles AS r INNER JOIN Users AS u ON r.users_id = u.id WHERE u.id = ?\n\n
  DELETE FROM p FROM Users_projects AS p INNER JOIN Users AS u ON p.users_id = u.id WHERE u.id = ?\n\n
  DELETE FROM Users WHERE id = ?")

(def delete-project-query
  "DELETE FROM up FROM Users_projects AS up INNER JOIN Project AS p ON up.projects_id = p.id WHERE p.id = ?\n\n
  DELETE FROM Project WHERE id = ?")

(def find-role-by-user-id-query
  "SELECT r.id, r.name FROM UserRole AS r INNER JOIN Users_roles AS ur ON r.id = ur.roles_id INNER JOIN Users u ON u.id = ur.users_id WHERE u.id = ?")

(defn find-user-role [id]
  (let [result (j/query (get-db) [find-role-by-user-id-query id])]
    (prn result)

    (json/write-str(vec result))))

(defn find-all-users []
  (let [result (j/query (get-db) ["SELECT * FROM Users"])]
    (prn result)

    (json/write-str(vec result))))

(defn find-by-email [email]
  (let [result (j/query (get-db) ["SELECT * FROM Users WHERE email = ?" email])]
    (prn result)
    (prn (:row-fn :email))

    (cond (empty? result)
          nil
          :else
          (json/write-str(first (vec result))))))

(defn login [username password]
  (let [result (j/query (get-db) ["SELECT * FROM Users WHERE username = ? AND password = ?" username password])]
    (prn result)

    (cond (empty? result)
          nil
          :else
          (json/write-str(first (vec result))))))

(defn delete-user [user_id]
  (j/db-do-prepared (get-db) [delete-user-query user_id user_id user_id])
  (let [result (j/query (get-db) ["SELECT * FROM Users WHERE id = ?" user_id])]

    (cond (empty? result)
          "Successfully deleted user."
          :else
          nil))
  )

(defn create-user [req]
  (let [result (j/insert! (get-db) :Users {:firstname (:firstname (:params req))
                                     :lastname (:lastname (:params req))
                                     :email (:email (:params req))
                                     :username (:username (:params req))
                                     :password (:password (:params req))
                                     })]

    (cond (empty? result)
          nil
          :else
          "Successfully created user."))
  )

(defn update-user [firstname lastname email username password user_id]
  (let [result (j/update! (get-db) :Users {:firstname firstname
                                     :lastname lastname
                                     :email email
                                     :username username
                                     :password password
                                     } ["id = ?" user_id])]

    (json/write-str(vec result)))
  )

(defn bind-role [user_id role_id]
  (let [result (j/insert! (get-db) :Users_roles
                          {:users_id user_id
                           :roles_id role_id})]

    (json/write-str(vec result)))
  )

(defn delete-project [project_id]
  (j/db-do-prepared (get-db) [delete-project-query project_id])
  (let [result (j/query (get-db) ["SELECT * FROM Users WHERE id = ?" project_id])]

    (cond (empty? result)
          "Successfully deleted user."
          :else
          nil))
  )