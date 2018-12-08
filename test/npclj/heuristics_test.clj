(ns npclj.heuristics_test
  (:require [clojure.test :refer :all]
            [npclj.heuristic :refer :all]))

(deftest manahattan-test
  (is (= 0 (manhattan [[1 2 3]
                       [8 0 4]
                       [7 6 5]])))
  (is (= 2 (manhattan [[2 1 3]
                       [8 0 4]
                       [7 6 5]])))
  (is (= 4 (manhattan [[0 2 3]
                       [8 1 4]
                       [7 6 5]])))
  (is (= 8 (manhattan [[5 2 3]
                       [8 0 4]
                       [7 6 1]]))))
