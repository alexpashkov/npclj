(ns npclj.mode
  (:require [org.httpkit.server :as http-kit]
            [clojure.string :as str]
            [clojure.java.io :as io]
            [npclj.parser :refer [parse]]
            [npclj.solve :refer [solve]]
            [npclj.puzzle :as puzzle]
            [npclj.heuristic :as heuristic]
            [npclj.is-solvable :refer [solvable?]]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [clojure.data.json :as json])
  (:gen-class))

(defn cli-mode [heuristic-fn]
  (do
    (println "Waiting for a puzzle...")
    (if-let [pzl (parse (line-seq (java.io.BufferedReader. *in*)))]
      (do
        (println "The puzzle is:")
        (puzzle/prn pzl)
        (println "Solving...")
        (if-let [solved (and (solvable? pzl)
                             (solve pzl heuristic-fn))]
          (do
            (doseq [parent (puzzle/get-parents solved)]
              (println)
              (puzzle/prn parent))
            (println)
            (puzzle/prn solved))
          (println "The puzzle isn't solvable")))
      (println "Failed to read a puzzle"))))

(defn homepage-handler [heur]
  (fn [req]
      {:status  200
       :headers {"Content-Type" "text/html"}
       :body    "Homepage."}))

(defn solve-to-json
  [body heuristic-fn]
  (json/write-str
    (try
      (if-let
        [json-req (json/read (io/reader body) :encoding "UTF-8")]
        (do
          (prn json-req)
          (if-let
            [pzl (json-req "puzzle")]
            (if (puzzle/valid? pzl)
              (if (solvable? pzl)
                { :status :ok :solved (solve pzl heuristic-fn) }
                { :status :error :error "Puzzle is not solvable" })
              { :status :error :error "Invalid puzzle." })
            { :status :error :error "No puzzle provided." }))
          { :status :error :error "Failed to read JSON." })
      (catch Exception e { :status :error :error (str e) }))))

(defn solver-handler [heur]
  (fn [req]
    {:status  200
     :headers {"Content-Type" "text/html"}
     :body    (solve-to-json (:body req) heur)}))

(defn server-mode [heuristic-fn]
  (do
    (defroutes np-routes
      (GET "/" [] (homepage-handler heuristic-fn))
      (POST "/" [] (solver-handler heuristic-fn)))
    (http-kit/run-server #'np-routes {:port 3000})))

(def np-modes
  { "cli"    cli-mode
    "http"   server-mode})
