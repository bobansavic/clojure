(defproject data-access-module-ispit "0.1.0-SNAPSHOT"
  :description "Alati i metode softverskog inzenjerstva - Ispit"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/java.jdbc "0.7.11"]
                 [com.microsoft.sqlserver/mssql-jdbc "8.2.2.jre8"]
                 [compojure "1.6.2"]
                 [http-kit "2.3.0"]
                 [ring/ring-defaults "0.3.2"]
                 [ring/ring-json "0.5.0"]
                 [org.clojure/data.json "0.2.6"]]
  :jar-name "clojuredemo.jar"
  :jar-path "/target/clojuredemo.jar"
  :plugins [[org.clojars.benfb/lein-gorilla "0.6.0"]
            [lein-ring "0.12.5"]]
  :ring {:handler data-access-module-ispit.core/app-routes
         :auto-reload? true
         :auto-refresh false}
  :main "data-access-module-ispit.core"
  :repl-options {:init-ns data-access-module-ispit.core}
  :jvm-opts ["-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5010"])
