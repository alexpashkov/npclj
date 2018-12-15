(ns npclj.http
  (:gen-class)
  (:require [org.httpkit.server :as http-kit]
            [clojure.java.io :as io]
            [npclj.generate :refer [generate]]
            [clojure.java.io :as io]
            [npclj.solve :refer [solve]]
            [npclj.puzzle :as puzzle]
            [npclj.is-solvable :refer [solvable?]]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [clojure.data.json :as json]
            [ring.middleware.cors :refer [wrap-cors]])
  (:gen-class))

(defn- server-log [& msg-list]
  (doseq [msg msg-list]
    (println (str "[SERVER] " msg))))

(defn- response-ok [result]
  {:status :ok
   :result result})

(defn- response-err [msg]
  (server-log (str "[ERROR] " msg))
  {:status :error
   :error  msg})

(defn- solve-to-json [body heuristic-fn]
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
             (response-err "Invalid puzzle.")))
         (response-err "No puzzle provided."))
       (response-err "Failed to read JSON."))
     (catch Exception _ (response-err "That is definitely not a correct puzzle...")))))

(def generate-handler
  (wrap-cors
   (fn [{{size :size} :params}]
     {:status  200
      :headers {"Content-Type" "application/json"}
      :body    (try
                 (let [size (Integer/parseInt size)]
                   (if (not (pos? size))
                     (throw (Exception. "Be positive!"))
                     (-> (generate size true)
                         (response-ok)
                         (json/write-str))))
                 (catch Exception _ (response-err "Invalid size")))})))

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
             (response-err "Invalid puzzle.")))
         (response-err "No puzzle provided."))
       (response-err "Failed to read JSON."))
     (catch Exception e (do
                          (server-log e)
                          (response-err "That is definitely not a correct puzzle..."))))))

(defn- solver-handler [heur]
  (wrap-cors
   (fn [req]
     {:status  200
      :headers {"Content-Type" "application/json"}
      :body    (solve-to-json (:body req) heur)})
   :access-control-allow-origin #".*"
   :access-control-allow-methods #{:get :post}))

(defn http-mode [heuristic-fn]
  (do
    (defroutes np-routes
      (GET "/" [] (io/resource "client/build/index.html"))
      (GET "/random/:size" [] generate-handler)
      (POST "/" [] (solver-handler heuristic-fn))
      (route/files "/static/" {:root "./client/build"}))
    (server-log "Setting up.")
    (http-kit/run-server #'np-routes {:port 3000})
    (server-log "Done. Waiting for requests.")))
