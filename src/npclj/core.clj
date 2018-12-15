(ns npclj.core
  (:require [clojure.tools.cli :as cli]
            [clojure.string :as str]
            [npclj.parser :refer [parse]]
            [npclj.solve :refer [solve]]
            [npclj.puzzle :as puzzle]
            [npclj.heuristic :as heuristic]
            [npclj.is-solvable :refer [solvable?]]
            [npclj.mode :as mode])
  (:gen-class))

(def cli-options [["-h" "--heuristic NAME" "Heuristic name"
                   :default (heuristic/fns "manhattan")
                   :parse-fn heuristic/fns
                   :validate [fn?
                              (str "Unknown heuristic is provided. "
                                   "Available names are: "
                                   (str/join ", " (keys heuristic/fns)))]]
                  ["-m" "--mode MODE" "NPuzzle mode"
                   :default (mode/np-modes "cli")
                   :parse-fn mode/np-modes
                   :validate [fn?
                              (str "Unknown mode is provided. "
                                   "Available modes are: "
                                   (str/join ", " (keys mode/np-modes)))]]])

(defn -main
  [& args]
  (let [{{heuristic-fn :heuristic
          np-mode-fn   :mode}      :options
         errors                    :errors} (cli/parse-opts args cli-options)]
    (if-not errors
      (do
        (np-mode-fn heuristic-fn)
        (comment
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
                    (println)
                    )
                  (println "Total number of states ever selected in the open set:"
                           selects)
                  (println "Maximum number of states represented in memory at the same time:"
                           max-count)
                  (println "Number of moves required to transition from the initial state to the final state:"
                           (dec (count states)))))
              (println "The puzzle isn't solvable")))
          (println "Failed to read a puzzle")))
      (doseq [err errors] (println err))))))
