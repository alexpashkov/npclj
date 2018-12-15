(ns npclj.mode
<<<<<<< HEAD
  (:gen-class)
=======
>>>>>>> 215376e2dabcd28047fb9e23b3f262db946076b5
  (:require [npclj.parser :refer [parse]]
            [npclj.solve :refer [solve]]
            [npclj.puzzle :as puzzle]
            [npclj.is-solvable :refer [solvable?]]
<<<<<<< HEAD
            [npclj.http :refer [http-mode]]))
=======
            [npclj.http :refer [http-mode]])
  (:gen-class))
>>>>>>> 215376e2dabcd28047fb9e23b3f262db946076b5

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
<<<<<<< HEAD
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
=======
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
>>>>>>> 215376e2dabcd28047fb9e23b3f262db946076b5
