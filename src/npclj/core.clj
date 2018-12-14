(ns npclj.core
  (:require [clojure.tools.cli :as cli]
            [clojure.string :as str]
            [npclj.parser :refer [parse]]
            [npclj.solve :refer [solve]]
            [npclj.puzzle :as puzzle]
            [npclj.heuristic :as heuristic]
            [npclj.is-solvable :refer [solvable?]])
  (:gen-class))

(def cli-options [["-h" "--heuristic NAME" "Heuristic name"
                   :default (heuristic/fns "manhattan")
                   :parse-fn heuristic/fns
                   :validate [fn?
                              (str "Unknown heuristic is provided. "
                                   "Available names are: "
                                   (str/join ", " (keys heuristic/fns)))]]])

(defn -main
  [& args]
  (let [{{heuristic-fn :heuristic} :options
         errors                    :errors} (cli/parse-opts args cli-options)]
    (if-not errors
      (do
        (println "Waiting for a puzzle...")
        (if-let [pzl (parse (line-seq (java.io.BufferedReader. *in*)))]
          (do
            (println "The puzzle is:")
            (puzzle/prn pzl)
            (println "Solving...")
            (if-let [solved (and (solvable? pzl)
                                 (solve pzl heuristic-fn))]
              (let [{:keys [states max-count selects]} solved]
                (do
                  (doseq [parent states]
                    (println)
                    (puzzle/prn parent))
                  (println "Total number of states ever selected in the open set:"
                           selects)
                  (println "Maximum number of states represented in memory at the same time:"
                           max-count)
                  (println "Number of moves required to transition from the initial state to the final state:"
                           (dec (count states)))))
              (println "The puzzle isn't solvable")))
          (println "Failed to read a puzzle")))
      (doseq [err errors] (println err)))))
