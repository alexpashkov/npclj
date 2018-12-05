(ns npclj.core
  (:require [npclj.parser :refer [parse]]
            [npclj.solve :refer [solve]]
            [npclj.puzzle :as puzzle]
            [npclj.heuristics :refer [manhattan]])
  (:gen-class))

(defn -main
  [& args]
  (println "Waiting for puzzle...")
  (if-let [pzl (parse (line-seq (java.io.BufferedReader. *in*)))]
    (if-let [solved (solve pzl manhattan)]
      (println (puzzle/get-parents solved))
      (println "Puzzle isn't solvable"))
    (println "Failed to read puzzle")))
