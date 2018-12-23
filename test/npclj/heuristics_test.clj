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

(deftest euclidean-test
  (is (= 0.0 (euclidean [[1 2 3]
                         [8 0 4]
                         [7 6 5]])))
  (is (= 2.0 (euclidean [[2 1 3]
                         [8 0 4]
                         [7 6 5]])))
  (is (= 4.0 (euclidean [[1 2 4]
                         [8 0 3]
                         [7 5 6]])))
  (is (= 8.82842712474619
             (euclidean [[1 3 7]
                         [4 2 0]
                         [8 6 5]])))
  (is (= 13.70820393249937
             (euclidean [[3 1 8]
                         [4 0 7]
                         [6 5 2]]))))

(deftest hamming-test
  (is (= 0 (hamming [[1 2 3]
                     [8 0 4]
                     [7 6 5]])))
  (is (= 2 (hamming [[2 1 3]
                     [8 0 4]
                     [7 6 5]])))
  (is (= 4 (hamming [[1 2 4]
                     [8 0 3]
                     [7 5 6]])))
  (is (= 6 (hamming [[1 3 7]
                     [4 2 0]
                     [8 6 5]])))
  (is (= 8 (hamming [[3 1 8]
                     [4 0 7]
                     [6 5 2]]))))
