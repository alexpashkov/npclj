(ns npclj.core
  (:require [clojure.tools.cli :as cli]
            [clojure.string :as str]
            [npclj.parser :refer [parse]]
            [npclj.solve :refer [solve]]
            [npclj.puzzle :as puzzle]
            [npclj.heuristic :as heuristic]
            [npclj.is-solvable :refer [solvable?]]
            )
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
            (if (solvable? pzl)
              (if-let [solved (solve pzl heuristic-fn)]
                (doseq [parent (puzzle/get-parents solved)]
                  (println)
                  (puzzle/prn parent))
                ;; It must be impossible to get here after solvable? check, but just in case
                (println "The puzzle isn't solvable"))
              (println "The puzzle isn't solvable")))
          (println "Failed to read a puzzle")))
      (doseq [err errors] (println err)))))
