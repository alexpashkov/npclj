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
          np-mode-fn   :mode} :options
         errors               :errors} (cli/parse-opts args cli-options)]
    (if-not errors
      (np-mode-fn heuristic-fn)
      (doseq [err errors] (println err)))))
