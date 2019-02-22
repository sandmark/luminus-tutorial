(ns guestbook.routes.home
  (:require [clojure.java.io :as io]
            [compojure.core :refer [defroutes GET POST]]
            [guestbook.db.core :as db]
            [guestbook.layout :as layout]
            [ring.util.http-response :as response]
            [struct.core :as st]))

(defn home-page [request]
  (layout/render request "home.html" {:docs (-> "docs/docs.md" io/resource slurp)}))

(defn about-page [request]
  (layout/render request "about.html"))

(defroutes home-routes
  (GET "/" request (home-page request))
  (GET "/about" request (about-page request)))

