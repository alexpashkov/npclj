(ns npclj.http
  (:require [org.httpkit.server :as http-kit]
            [clojure.string :as str]
            [clojure.java.io :as io]
            [npclj.solve :refer [solve]]
            [npclj.puzzle :as puzzle]
            [npclj.is-solvable :refer [solvable?]]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [clojure.data.json :as json])
  (:gen-class))

(defn- server-log [& msg-list]
  (doseq [msg msg-list]
    (println (str "[SERVER] " msg))))

(defn- response-err [msg]
  (do
    (server-log (str "[ERROR] " msg))
    { :status :error
      :error  msg }))

(defn- response-ok [result]
  { :status :ok
    :result result })

(defn- solve-to-json
  [body heuristic-fn]
  (json/write-str
    (try
      (if-let
        [json-req (json/read (io/reader body) :encoding "UTF-8")]
          (if-let
            [pzl (json-req "puzzle")]
            (do
              (server-log "Got puzzle:" (str "\t" pzl))
              (if (puzzle/valid? pzl)
                (if (solvable? pzl)
                  (response-ok (solve pzl heuristic-fn))
                  (response-err "Puzzle is not solvable"))
                (response-err  "Invalid puzzle.")))
            (response-err "No puzzle provided."))
          (response-err "Failed to read JSON."))
      (catch Exception e (response-err "That is definitely not a correct puzzle...")))))

(defn- solver-handler [heur]
  (fn [req]
    {:status  200
     :headers {"Content-Type" "application/json"}
     :body    (solve-to-json (:body req) heur)}))

(defn http-mode [heuristic-fn]
  (do
    (defroutes np-routes
      (GET "/" [] (io/resource "client/build/index.html"))
      (POST "/" [] (solver-handler heuristic-fn))
      (route/files "/static/" {:root "./client/build"}))
    (server-log "Setting up.")
    (http-kit/run-server #'np-routes {:port 3000})
    (server-log "Done. Waiting for requests.")))
