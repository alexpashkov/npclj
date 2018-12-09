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
          (println "Failed to read a puzzle")))
      (doseq [err errors] (println err))))))
