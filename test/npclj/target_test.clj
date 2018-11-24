(ns npclj.target-test
  (:require [clojure.test :refer :all]
            [npclj.target :refer :all]))

(deftest target-test
  (is (= (gen-target 1) [[0]]))
  (is (= (gen-target 2) [[1 2]
                         [0 3]]))
  (is (= (gen-target 3) [[1 2 3]
                         [8 0 4]
                         [7 6 5]]))
  (is (= (gen-target 4) [[1 2 3 4]
                         [12 13 14 5]
                         [11 0 15 6]
                         [10 9 8 7]]))
  (is (= (gen-target 5) [[1 2 3 4 5]
                         [16 17 18 19 6]
                         [15 24 0 20 7]
                         [14 23 22 21 8]
                         [13 12 11 10 9]])))
