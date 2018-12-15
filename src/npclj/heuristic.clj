(ns npclj.heuristic
  (:require [npclj.generate :as target]
            [npclj.puzzle :as puzzle]))

(defn heuristics-with [f pzl]
  (let [target (target/generate (count pzl))]
    (puzzle/reduce (fn [acc tile coords]
                     (+ acc (f target tile coords)))
                   0
                   pzl)))

(defn abs [x]
  (if (neg? x) (- x) x))

(defn manhattan-tile [target tile coords]
  (apply +
         (map (comp abs -)
              coords
              (puzzle/find-tile target tile))))

(def manhattan (partial heuristics-with manhattan-tile))

(def fns {"manhattan" manhattan
          "lala"      manhattan
          "tata"      manhattan})
