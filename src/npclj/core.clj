(ns npclj.core
  (:require [npclj.parser :refer [parse]]
            [npclj.solve :refer [solve]]
            [npclj.puzzle :as puzzle]
            [npclj.heuristics :refer [manhattan]])
  (:gen-class))

(set! *warn-on-reflection* true)

(defn -main
  [& args]
  (println "Waiting for a puzzle...")
  (if-let [pzl (parse (line-seq (java.io.BufferedReader. *in*)))]
    (do
      (println "The puzzle is" pzl)
      (if-let [solved (solve pzl manhattan)]
        (println (puzzle/get-parents solved))
        (println "The puzzle isn't solvable")))
    (println "Failed to read a puzzle")))
