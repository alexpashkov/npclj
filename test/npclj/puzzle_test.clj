(ns npclj.puzzle_test
  (:require [clojure.test :refer :all]
            [npclj.puzzle :as puzzle]))

(def pzl [[1 2] [0 3]])
(def pzl2 [[1 2 3] [4 5 6] [7 8 9]])

(deftest find-tile-test
  (is (= [0 0] (puzzle/find-tile pzl 1)))
  (is (= [1 0] (puzzle/find-tile pzl 2)))
  (is (= [1 1] (puzzle/find-tile pzl 3)))
  (is (= [0 1] (puzzle/find-tile pzl 0)))

  (is (= [0 0] (puzzle/find-tile pzl2 1)))
  (is (= [1 0] (puzzle/find-tile pzl2 2)))
  (is (= [2 0] (puzzle/find-tile pzl2 3)))
  (is (= [2 2] (puzzle/find-tile pzl2 9))))
