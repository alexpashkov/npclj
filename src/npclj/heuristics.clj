(ns npclj.heuristics
  (:require [npclj.target :as target]
            [npclj.puzzle :as puzzle]))

(defn heuristics [f pzl]
  (let [target (target/generate (count pzl))]
    (puzzle/reduce (fn [acc tile coords]
                     (+ acc (f target tile coords)))
                   0
                   pzl)))

(defn manhattan-tile [target tile coords]
  (reduce #(Math/abs (+ %1 %2))
          (map - coords (puzzle/find-tile target tile))))

(def manhattan (partial heuristics manhattan-tile))
