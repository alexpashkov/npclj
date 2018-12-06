(ns npclj.parser_test
  (:require [clojure.test :refer :all]
            [npclj.parser :refer :all]))

(def pzl [[1 2 3]
          [4 5 6]
          [7 8 0]])

(deftest parse-puzzle-test
  (testing "valid cases"
    (is (= pzl (parse ["3"
                       "1 2 3"
                       "4 5 6"
                       "7 8 0"])))
    (is (= pzl (parse ["     3   #sdafsafsaf    "
                       "  1 2    3 "
                       "    4    5            6"
                       "  7        8 0 # loooooooooool      "])))))
