(ns npclj.is-solvable
  (:require [npclj.generate :as target]
            [npclj.puzzle :as puzzle])
  (:gen-class))

(defn- tile-inversions [pzl tile [x y]]
  (puzzle/reduce (fn [acc cur-tile [cur-x cur-y]]
                   (if (and (or (> cur-y y)
                                (and (= cur-y y)
                                     (> cur-x x)))
                            (pos? cur-tile)
                            (> tile cur-tile))
                     (inc acc)
                     acc))
                 0
                 pzl))

(defn- inversions [pzl]
  (puzzle/reduce (fn [acc tile coords]
                   (+ acc (tile-inversions pzl tile coords)))
                 (if (even? (puzzle/size pzl))
                   (puzzle/flat-find-tile pzl 0)
                   0)
                 pzl))

(defn solvable? [pzl]
  (=
   (even? (inversions pzl))
   (even? (inversions (target/generate (puzzle/size pzl))))))
