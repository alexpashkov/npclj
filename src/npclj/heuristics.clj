(ns npclj.heuristics
  (:require [npclj.gen-target :refer :all]
            [npclj.pzl-utils :as utils]))

(defn manhattan-tile [tile coords target]
  (reduce #(Math/abs (+ %1 %2))
          (map - coords (utils/find-tile target tile))))

(manhattan-tile 1 [1 1] [[1 2] [3 4]])

(defn manhattan [pzl]
  "Inneficient manhattan distance heuristics"
  (reduce (fn [acc row]
            (reduce (fn [acc tile]
                      (+ acc (manhattan-tile
                              tile
                              (utils/find-tile pzl tile)
                              (gen-target (count pzl)))))
                    acc
                    row))
          0
          pzl))

(manhattan [[5 2 3] [8 0 4] [7 6 1]])
