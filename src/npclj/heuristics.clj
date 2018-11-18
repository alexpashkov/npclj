(ns npclj.heuristics
  (:require [npclj.gen-target :refer :all]
            [npclj.pzl-utils :as utils]))

(defn manhattan-tile [tile coords target]
  (reduce #(Math/abs (+ %1 %2))
          (map - coords (utils/find-tile target tile))))

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
