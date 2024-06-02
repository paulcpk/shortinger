(ns server.db
  (:require [clojure.java.jdbc :as j]
            [honey.sql :as sql]
            [honey.sql.helpers :as helpers]
           ))

;; https://github.com/clojure/java.jdbc
;; https://github.com/seancorfield/honeysql
;; https://search.maven.org/artifact/mysql/mysql-connector-java/8.0.30/jar


;; (def mysql-db {:host (env :HOST)
;;                :dbtype "mysql"
;;                :dbname (env :DBNAME)
;;                :user (env :USER)
;;                :password (env :PASSWORD)})


(def mysql-db {:host "127.0.0.1"
               :dbtype "mysql"
               :dbname "shortinger"
               :user "root"
               :password "shorty"})

(defn query [q]
  (j/query mysql-db q))

(defn insert! [q]
  (j/db-do-prepared mysql-db q))

(defn insert-redirect! [slug url]
  (insert! (-> (helpers/insert-into :redirects)
               (helpers/columns :slug :url)
               (helpers/values
                [[slug url]])
               (sql/format))))

(defn get-url [slug]
  (-> (query (-> (helpers/select :*)
                 (helpers/from :redirects)
                 (helpers/where [:= :slug slug])
                 (sql/format)))
      first
      :url))

(comment
  (query (-> (helpers/select :*)
             (helpers/from :redirects)
             (sql/format)))
  (insert! (-> (helpers/insert-into :redirects)
               (helpers/columns :slug :url)
               (helpers/values
                [["abc" "https://github.com/seancorfield/honeysql"]])
               (sql/format)))
  (insert-redirect! "xyz" "https://github.com/clojure/java.jdbc")
  (get-url "abc"))
