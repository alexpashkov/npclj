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

(defn euclidean-tile [target tile coords]
  (let [[x1 y1] coords
        [x2 y2] (puzzle/find-tile target tile)]
    (Math/sqrt
      (+ (Math/pow (- x2 x1) 2)
         (Math/pow (- y2 y1) 2)))))

(defn hamming-tile [target tile coords]
  (if (= coords (puzzle/find-tile target tile))
    0
    1))

(def manhattan (partial heuristics-with manhattan-tile))
(def euclidean (partial heuristics-with euclidean-tile))
(def hamming   (partial heuristics-with hamming-tile))

(def fns {"manhattan" manhattan
          "euclidean" euclidean
          "hamming"   hamming})
