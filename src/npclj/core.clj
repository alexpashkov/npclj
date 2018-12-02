(ns npclj.core
  (:require [npclj.parser :refer [parse-puzzle]])
  (:gen-class))

(defn read-pzl [from]
  (reduce #(str %1 "\n" %2)
          ""
          (line-seq (java.io.BufferedReader. from))))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println (parse-puzzle (read-pzl *in*))))
