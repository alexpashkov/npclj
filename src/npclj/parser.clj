(ns npclj.parser
  (:require [clojure.string :as str]
            [npclj.puzzle :as puzzle])
  (:gen-class))

(defn- remove-comments [line]
  (str/replace line #"#.*" ""))

(defn- parse-int [x]
  (Integer/parseInt x))

(defn- line->size [line]
  (->> line
       (re-find #"^\s*(\d+)\s*$")
       (second)
       (parse-int)))

(defn- line->row [size line]
  (if (re-matches (re-pattern
                   (format "^\\s*(\\d+\\s+){%d}\\d+\\s*$"
                           (dec size)))
                  line)
    (into [] (comp (filter seq)
                   (map parse-int))
          (str/split line #"\s+"))
    (throw (Exception. "invalid row"))))

(defn parse-line [pzl line]
  (let [line (remove-comments line)]
    (cond (empty? line) pzl
          (nil? pzl) (line->size line)
          (number? pzl) [(line->row pzl line)]
          (vector? pzl) (conj pzl
                              (line->row (puzzle/size pzl) line)))))

(defn parse [lines]
  (when-let [pzl
             (try
               (reduce parse-line nil lines)
               (catch Exception _ nil))]
    (when (puzzle/valid? pzl) pzl)))
