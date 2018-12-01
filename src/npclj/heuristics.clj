(ns npclj.heuristics
  (:require [npclj.target :as target]
            [npclj.puzzle :as puzzle]))

(defn manhattan-tile [tile coords target find-tile]
  (reduce #(Math/abs (+ %1 %2))
          (map - coords (find-tile target tile))))

(defn manhattan [pzl]
  (let [target (target/generate (count pzl))
        find-tile (memoize puzzle/find-tile)]
    (puzzle/reduce (fn [acc tile coords]
                     (+ acc (manhattan-tile
                              tile
                              coords
                              target
                              find-tile)))
                   0
                   pzl)))
