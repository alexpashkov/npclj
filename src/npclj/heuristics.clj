(ns npclj.heuristics
  (:require [npclj.target :refer :all]
            [npclj.pzl-utils :as utils]))

(defn manhattan-tile [tile coords target]
  (reduce #(Math/abs (+ %1 %2))
          (map - coords (utils/find-tile target tile))))

(defn manhattan [tiles]
  "Inneficient manhattan distance heuristics"
  (reduce (fn [acc row]
            (reduce (fn [acc tile]
                      (+ acc (manhattan-tile
                              tile
                              (utils/find-tile tiles tile)
                              (gen-target (count tiles)))))
                    acc
                    row))
          0
          tiles))

(manhattan [[1 2] [0 3]])
