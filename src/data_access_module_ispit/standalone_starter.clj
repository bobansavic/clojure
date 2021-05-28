(ns data-access-module-ispit.standalone-starter
  (:require [data-access-module-ispit.data-access-layer :as dal]
            [clojure.java.jdbc :as jdbc]
            [clojure.java.io :as io]))

(def create-db (io/resource "sql-scripts/create-db.sql"))
(def populate-db (io/resource "sql-scripts/populate-db.sql"))

(def con "jdbc:sqlserver://localhost;user=sa;password=")

(defn exec-sql-files [dbname]
  (jdbc/db-do-prepared con false (slurp create-db))
  (println "Database " dbname " created successfully.")

  (dal/update-db dbname)
  (println "Updated global variable :dbname with: " dbname)

  (jdbc/db-do-prepared (dal/get-db) (slurp populate-db))
  (println "Database " ((dal/get-db) :dbname) " populated successfully."))
