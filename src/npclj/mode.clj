(ns npclj.mode
  (:require [org.httpkit.server :as http-kit]
            [clojure.string :as str]
            [npclj.parser :refer [parse]]
            [npclj.solve :refer [solve]]
            [npclj.puzzle :as puzzle]
            [npclj.heuristic :as heuristic]
            [npclj.is-solvable :refer [solvable?]]
            [compojure.core :refer :all]
            [compojure.route :as route])
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
    (do
      (prn heur)
      {:status  200
       :headers {"Content-Type" "text/html"}
       :body    "Homepage."})))

(defn solver-handler [heur]
  (fn [req]
    {:status  200
     :headers {"Content-Type" "text/html"}
     :body     "Solver."}))

(defn server-mode [heuristic-fn]
  (do
    (defroutes np-routes
      (GET "/" [] (homepage-handler heuristic-fn))
      (POST "/" [] (solver-handler heuristic-fn)))
    (http-kit/run-server #'np-routes {:port 3000})))

(def np-modes
  { "cli"    cli-mode
    "http"   server-mode})
