(ns npclj.parser
  (:gen-class)
  (:require [clojure.string :as str]))

(defn- parse-puzzle-row-nums
  [puzzle-row-str]
  (map #(Integer/parseInt %) (str/split puzzle-row-str #" +")))

(defn- get-row-pattern-by-size
  [size]
  (if (= size 1)
    #" *(:?\d+) *(?:#[^\n]*)?"
    (re-pattern
     (str " *((:?\\d+ +){" (- size 1) "}(:?\\d+)) *(?:#[^\\n]*)?"))))

(defn- parse-puzzle-row
  [size line]
  (when line
    (let [matches (re-matches (get-row-pattern-by-size size) line)]
      (when matches
        (parse-puzzle-row-nums (second matches))))))

(defn- parse-size
  [size-line]
  (when size-line
    (try
      (Integer/parseInt
       (second (re-matches #"^ *(\d) *(?:#[^\n]*)?$" size-line)))
      (catch NumberFormatException e nil))))

(defn- puzzle-get-lines
  [input]
  (map str/trim
       (filter (complement str/blank?)
               (str/split input #"\n"))))

(defn parse-puzzle
  [input]
  (let [[size-line & puzzle-rows] (puzzle-get-lines input)
        size (parse-size size-line)]
    (when (and size
               (< 0 size)
               (= size (count puzzle-rows)))
      (let [parsed-rows (map #(parse-puzzle-row size %1) puzzle-rows)]
        (when (every? (complement nil?) parsed-rows)
          parsed-rows)))))
