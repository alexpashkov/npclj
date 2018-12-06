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
                       "  7        8 0 # loooooooooool      "]))))
  (testing "invalid cases"
    (is (nil? (parse ["2"
                      "1 2 3"
                      "4 5 6"
                      "7 8 0"])))
    (is (nil? (parse ["4"
                      "1 2 3"
                      "4 5 6"
                      "7 8 0"])))
    (is (nil? (parse ["0"
                      "1 2 3"
                      "4 5 6"
                      "7 8 0"])))
    (is (nil? (parse [""
                      "1 2 3"
                      "4 5 6"
                      "7 8 0"])))
    (is (nil? (parse ["1 2 3"
                      "4 5 6"
                      "7 8 0"])))
    (is (nil? (parse ["3"
                      "1 2 3"
                      "4 5 6"])))
    (is (nil? (parse ["3"
                      "1 asdfasfa 2 3"
                      "4 5 6"
                      "7 8 0"])))
    (is (nil? (parse ["3"
                      "1 2 3"
                      "4 56"
                      "7 8 0"])))
    (is (nil? (parse ["3"
                      "5 2 3"
                      "4 5 6"
                      "7 8 0"
                      "7 8 0"])))
    (is (nil? (parse ["3"
                      "1 2 3"
                      "4 5 6"
                      "7 8 10"])))
    (is (nil? (parse ["3"
                      "1 2 2"
                      "4 5 6"
                      "7 8 0"])))
    (is (nil? (parse ["1 2 3"
                      "3"
                      "4 5 6"
                      "7 8 0"])))))
