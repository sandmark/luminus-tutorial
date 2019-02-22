(ns guestbook.test.db.core
  (:require [java-time :refer [local-date-time]]
            [clojure.java.jdbc :as jdbc]
            [clojure.test :refer :all]
            [guestbook.config :refer [env]]
            [guestbook.db.core :as db :refer [*db*]]
            [luminus-migrations.core :as migrations]
            [mount.core :as mount]))

(use-fixtures
  :once
  (fn [f]
    (mount/start
     #'guestbook.config/env
     #'guestbook.db.core/*db*)
    (migrations/migrate ["migrate"] (select-keys env [:database-url]))
    (f)))

(deftest test-users
  (jdbc/with-db-transaction [t-conn *db*]
    (jdbc/db-set-rollback-only! t-conn)
    (let [timestamp (local-date-time)]
      (is (= 1 (db/save-message!
                t-conn
                {:name      "Bob"
                 :message   "Hello, World"
                 :timestamp timestamp}
                {:connection t-conn})))
      (is (= {:name      "Bob"
              :message   "Hello, World"
              :timestamp timestamp}
             (-> (db/get-messages t-conn {})
                 first
                 (select-keys [:name :message :timestamp])))))))
