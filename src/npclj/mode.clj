(ns npclj.mode
  (:require [npclj.parser :refer [parse]]
            [npclj.solve :refer [solve]]
            [npclj.puzzle :as puzzle]
            [npclj.is-solvable :refer [solvable?]]
            [npclj.http :refer [http-mode]])
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

(def np-modes
  { "cli"    cli-mode
    "http"   http-mode})
