(ns npclj.mode
  (:gen-class)
  (:require [npclj.parser :refer [parse]]
            [npclj.solve :refer [solve]]
            [npclj.puzzle :as puzzle]
            [npclj.is-solvable :refer [solvable?]]
            [npclj.http :refer [http-mode]]))

(defn cli-mode [heuristic-fn]
  (println "Waiting for a puzzle...")
  (if-let [pzl (parse (line-seq (java.io.BufferedReader. *in*)))]
    (do
      (println "The puzzle is:\n")
      (puzzle/prn pzl)
      (println "\nSolving...\n")
      (if-let [solved (and (solvable? pzl)
                           (solve pzl heuristic-fn))]
        (let [{:keys [states max-count selects]} solved]
          (do
            (doseq [parent states]
              (puzzle/prn parent)
              (println))
            (println "Total number of states ever selected in the open set:"
                     selects)
            (println "Maximum number of states represented in memory at the same time:"
                     max-count)
            (println "Number of moves required to transition from the initial state to the final state:"
                     (dec (count states)))))
        (println "The puzzle isn't solvable")))
    (println "Failed to read a puzzle")))

(def np-modes
  {"cli"  cli-mode
   "http" http-mode})
