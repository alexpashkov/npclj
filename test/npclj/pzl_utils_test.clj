(ns npclj.pzl-utils-test
  (:require [clojure.test :refer :all]
            [npclj.pzl-utils :refer :all]))

(def pzl [[1 2] [0 3]])
(def pzl2 [[1 2 3] [4 5 6] [7 8 9]])

(deftest find-tile-test
  (is (= [0 0] (find-tile pzl 1)))
  (is (= [1 0] (find-tile pzl 2)))
  (is (= [1 1] (find-tile pzl 3)))
  (is (= [0 1] (find-tile pzl 0)))

  (is (= [0 0] (find-tile pzl2 1)))
  (is (= [1 0] (find-tile pzl2 2)))
  (is (= [2 0] (find-tile pzl2 3)))
  (is (= [2 2] (find-tile pzl2 9))))
