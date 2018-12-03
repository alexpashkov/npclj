(ns npclj.core
  (:require [npclj.parser :refer [parse-puzzle]]
            [npclj.solve :refer [solve]]
            [npclj.puzzle :as puzzle]
            [npclj.heuristics :refer [manhattan]])
  (:gen-class))

(defn read-pzl [from]
  (reduce #(str %1 "\n" %2)
          ""
          (line-seq (java.io.BufferedReader. from))))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Waiting for puzzle...")
  (if-let [pzl (-> *in*
                   (read-pzl)
                   (parse-puzzle))]
    (if-let [solved (solve (->> pzl
                                (map (partial into []))
                                (into [])) manhattan)]
      (println (puzzle/get-parents solved))
      (println "Puzzle isn't solvable"))
    (println "Failed to read puzzle")))
