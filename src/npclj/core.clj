(ns npclj.core
  (:require [clojure.tools.cli :as cli]
            [clojure.string :as str]
            [npclj.parser :refer [parse]]
            [npclj.solve :refer [solve]]
            [npclj.puzzle :as puzzle]
            [npclj.heuristic :as heuristic])
  (:gen-class))

(def cli-options [["-h" "--heuristic NAME" "Heuristic name"
                   :default "manhattan"
                   :parse-fn heuristic/fns
                   :validate [fn?
                              (str "Unknown heuristic is provided. "
                                   "Available names are: "
                                   (str/join ", " (keys heuristic/fns)))]]])

(defn -main
  [& args]
  (let [{{heuristics-fn :heuristic} :options
         errors                      :errors} (cli/parse-opts args cli-options)]
    (if errors (doseq [err errors] (println err))
               (do
                 (println "Waiting for a puzzle...")
                 (if-let [pzl (parse (line-seq (java.io.BufferedReader. *in*)))]
                   (do
                     (println "The puzzle is" pzl)
                     (if-let [solved (solve pzl heuristics-fn)]
                       (println (puzzle/get-parents solved))
                       (println "The puzzle isn't solvable")))
                   (println "Failed to read a puzzle"))))))
