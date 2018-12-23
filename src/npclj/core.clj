(ns npclj.core
  (:require [clojure.tools.cli :as cli]
            [clojure.string :as str]
            [npclj.heuristic :as heuristic]
            [npclj.generate :as generator]
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
                                   (str/join ", " (keys mode/np-modes)))]]
                  ["-g" "--generate SIZE" "Generate random puzzle of size SIZE"
                   :default nil
                   :parse-fn #(try
                                (Integer/parseInt %)
                                (catch Exception e -1))
                   :validate [#(> % 2)
                              (str "Please, provide valid puzzle size >= 3.")]]])

(defn -main
  [& args]
  (let [{{heuristic-fn :heuristic
          np-mode-fn   :mode
          gen-size     :generate } :options
         errors :errors} (cli/parse-opts args cli-options)]
    (if-not errors
      (if (and
            (= np-mode-fn (mode/np-modes "cli"))
            (not= gen-size nil))
        (mode/cli-solve heuristic-fn (generator/generate gen-size true))
        (np-mode-fn heuristic-fn))
      (doseq [err errors] (println err)))))
