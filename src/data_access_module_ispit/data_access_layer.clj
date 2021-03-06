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
  "DELETE FROM p FROM ProjectsUsers AS p INNER JOIN Users AS u ON p.user_id = u.id WHERE u.id = ?\n\n
  DELETE FROM Users WHERE id = ?")

(def delete-project-query
  "DELETE FROM t FROM Task AS t INNER JOIN Project AS p ON t.project_id = p.id WHERE p.id = ?\n\n
  DELETE FROM pu FROM ProjectsUsers AS pu INNER JOIN Project AS p ON pu.project_id = p.id WHERE p.id = ?\n\n
  DELETE FROM Project WHERE id = ?")

(def find-role-by-user-id-query
  "SELECT ur.id, ur.name FROM UserRole AS ur INNER JOIN Users AS u ON u.role_id = ur.id AND u.id = ?")

(def find-role-by-id-query
  "SELECT * FROM UserRole WHERE id=?")

(def find-user-by-username-email-and-pass "SELECT * FROM Users WHERE (username = ? OR email = ?) AND password = ?")

(def get-latest-id-users "SELECT id FROM Users ORDER BY id DESC")

(def get-latest-id-project "SELECT id FROM Users ORDER BY id DESC")

(def get-latest-id-task "SELECT id FROM Users ORDER BY id DESC")

(def find-projects-by-email-query
  "SELECT p.id, p.title FROM Project AS p INNER JOIN ProjectsUsers AS pu ON pu.project_id=p.id INNER JOIN Users AS u ON (u.email=? OR u.username=?) AND u.id = pu.user_id")

(defn get-id-users []
  (let [result (j/query (get-db) get-latest-id-users)]
    (cond (empty? result)
          1
          :else
          (+ 1 (get (first result) :id)))
    ))

(defn get-id-project []
  (let [result (j/query (get-db) get-latest-id-project)]
    (cond (empty? result)
          1
          :else
          (+ 1 (get (first result) :id)))
    ))

(defn get-id-task []
  (let [result (j/query (get-db) get-latest-id-task)]
    (cond (empty? result)
          1
          :else
          (+ 1 (get (first result) :id)))
    ))

(defn find-user-role [id]
  (let [result (j/query (get-db) [find-role-by-id-query id])]
    (prn result)

    (json/write-str(first result))))

(defn find-all-users []
  (let [result (j/query (get-db) ["SELECT * FROM Users"])]
    (prn result)

    (json/write-str(vec result))))

(defn find-by-email [email]
  (let [result (j/query (get-db) ["SELECT * FROM Users WHERE email = ?" email])]
    (prn result)
    (cond (empty? result)
          nil
          :else
          (do
            (prn (str "User found: " (get (first result) :email)))
            (json/write-str(first result)))
          )))

(defn login [email password]
  (let [result (j/query (get-db) [find-user-by-username-email-and-pass email email password])]
    (cond (empty? result)
          nil
          :else
          (do
            (prn (str "User found: " (get (first result) :email)))
            (json/write-str(first result))))))

(defn delete-user [email]
  (let [find-result (j/query (get-db) ["SELECT * FROM Users WHERE email=? OR username=?" email email])]
        (cond (empty? find-result)
              nil
              :else
              (do
                (let [user-id (get (first find-result) :id)]
                      (j/db-do-prepared (get-db) [delete-user-query user-id user-id])
                      (let [result (j/query (get-db) ["SELECT * FROM Users WHERE id = ?" user-id])]
                        (cond (empty? result)
                              "Successfully deleted user."
                              :else
                              nil))
                      )
                )
              )
        )
  )

(defn create-user [req]
  (let [result (j/insert! (get-db) :Users {:id (get-id-users)
                                           :firstName (get-in req [:body "firstName"])
                                           :lastName (get-in req [:body "lastName"])
                                           :email (get-in req [:body "email"])
                                           :username (get-in req [:body "username"])
                                           :password (get-in req [:body "password"])
                                           :role_id (get-in req [:body "role_id"])
                                     })]

    (cond (empty? result)
          nil
          :else
          "Successfully created user."))
  )

(defn update-user [req]
  (let [values {}]
    (if (not (nil? (get-in req [:body "firstName"])))
    (assoc values :firstName (get-in req [:body "firstName"]))
    nil)
    (if (not (nil? (get-in req [:body "lastName"])))
      (assoc values :lastName (get-in req [:body "lastName"]))
      nil)
    (if (not (nil? (get-in req [:body "email"])))
      (assoc values :email (get-in req [:body "email"]))
      nil)
    (if (not (nil? (get-in req [:body "username"])))
      (assoc values :username (get-in req [:body "username"]))
      nil)
    (if (not (nil? (get-in req [:body "password"])))
      (assoc values :password (get-in req [:body "password"]))
      nil)
    (if (not (nil? (get-in req [:body "role_id"])))
      (assoc values :role_id (get-in req [:body "role_id"]))
      nil)

    (let [result (j/update! (get-db) :Users values ["id = ?" (get-in req [:body "user_id"])])]

      (if (> result 0)
        "Successfully updated user"))
        "Nothing changed.")

  )

(defn find-all-projects []
  (let [result (j/query (get-db) ["SELECT * FROM Project"])]
    (prn result)

    (json/write-str(vec result))))

(defn find-projects-by-email [email]
  (let [result (j/query (get-db) [find-projects-by-email-query email email])]
    ((if (empty? result)
      (prn (str "No projects found for " email))
      (prn result)))

    (json/write-str(vec result))))

(defn create-project [title]
  (j/insert! (get-db) :Project {:id (get-id-project)
                                :title title})
  )


(defn delete-project [title]
  (let [result (j/query (get-db) ["SELECT id FROM Project WHERE title=?" title])]
    (prn result)
    (if (empty? result)
      nil
      (let [project_id (get (first result) :id)]
        (prn (str "Deleting project: " title))
        (j/db-do-prepared (get-db) [delete-project-query project_id project_id project_id])
        )
      )
    )
  )

(defn check-project-exists [title]
  (let [result (j/query (get-db) ["SELECT * FROM Project WHERE title=?" title])]
    (if (empty? result)
      false
      true)
    )
  )