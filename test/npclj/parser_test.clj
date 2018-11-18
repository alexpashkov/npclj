(ns npclj.parser_test
  (:require [clojure.test :refer :all]
            [npclj.parser :refer :all]))

(deftest parse-puzzle-row-nums-test
  (testing "a num row parser for validated input"
    (let [parse-puzzle-row-nums #'npclj.parser/parse-puzzle-row-nums]
      (is (= '(1 2 3) (parse-puzzle-row-nums "1 2 3")))
      (is (= '(1 2) (parse-puzzle-row-nums "1 2")))
      (is (= '(1) (parse-puzzle-row-nums "1"))))))

(deftest parse-puzzle-row-test
  (let [parse-puzzle-row #'npclj.parser/parse-puzzle-row]
    (testing "valid cases"
      (is (= '(1 2) (parse-puzzle-row 2 "1 2")))
      (is (= '(1 2) (parse-puzzle-row 2 "     1 2")))
      (is (= '(1 2) (parse-puzzle-row 2 "1 2    ")))
      (is (= '(1 2) (parse-puzzle-row 2 "1      2")))
      (is (= '(1 2) (parse-puzzle-row 2 "   1      2")))
      (is (= '(1 2) (parse-puzzle-row 2 "1      2    ")))
      (is (= '(1 2) (parse-puzzle-row 2 "   1      2    ")))
      (is (= '(1 2) (parse-puzzle-row 2 "1 2 #whatever"))))
    (testing "with invalid size"
      (is (= nil (parse-puzzle-row 1 "1 2")))
      (is (= nil (parse-puzzle-row 1 "1 2 3")))
      (is (= nil (parse-puzzle-row 2 "1")))
      (is (= nil (parse-puzzle-row 2 "1 2 3"))))
    (testing "with invalid line"
      (is (= nil (parse-puzzle-row 1 nil)))
      (is (= nil (parse-puzzle-row 1 "a1")))
      (is (= nil (parse-puzzle-row 1 "1a")))
      (is (= nil (parse-puzzle-row 1 "12-23"))))))

(deftest parse-size-test
  (let [parse-size #'npclj.parser/parse-size]
    (is (= 1 (parse-size "1")))
    (is (= 1 (parse-size "  1")))
    (is (= 1 (parse-size "1  ")))
    (is (= 1 (parse-size "  1  ")))
    (is (= 1 (parse-size "1 #comment")))
    (is (= nil (parse-size "size")))
    (is (= nil (parse-size "1 2")))
    (is (= nil (parse-size "32size")))))

(deftest puzzle-get-lines-test
  (let [puzzle-get-lines #'npclj.parser/puzzle-get-lines]
    (is (= '("1" "1") (puzzle-get-lines "1\n1")))
    (is (= '("1" "1") (puzzle-get-lines "   1   \n   1   ")))
    (is (= '("1" "1") (puzzle-get-lines "1\n1\n   \n")))
    (is (= '("1" "1") (puzzle-get-lines "\n   \n1\n1")))
    (is (= '("1" "1") (puzzle-get-lines "\n   \n1\n1\n   \n")))
    (is (= '("2" "1 2" "3 4") (puzzle-get-lines "2\n1 2\n3 4")))
    (is (= '("2" "1 2" "3 4") (puzzle-get-lines "\n  2\n1 2\n3 4")))
    (is (= '("2" "1 2" "3 4") (puzzle-get-lines "   \n2\n1 2\n3 4")))
    (is (= '("2" "1 2" "3 4") (puzzle-get-lines "2\n1 2\n3 4\n   ")))
    (is (= '("2" "1 2" "3 4") (puzzle-get-lines "  \n2\n1 2\n3 4\n  \n")))))

(deftest parse-puzzle-test
  (testing "valid cases"
    (is (= '((1)) (parse-puzzle "1\n1")))
    (is (= '((1 2) (3 4)) (parse-puzzle "2\n1 2\n3 4\n")))
    (is (= '((1 2 3) (4 5 6) (7 8 9)) (parse-puzzle "3\n1 2 3\n4 5 6\n7 8 9\n")))
    (is (= '((1)) (parse-puzzle "1 #size is one\n1 #this is one"))))
  (testing "mismatched size"
    (is (= nil (parse-puzzle "1\n1 2\n3 4")))
    (is (= nil (parse-puzzle "1\n1 2")))
    (is (= nil (parse-puzzle "1\n1\n3")))
    (is (= nil (parse-puzzle "2\n1")))
    (is (= nil (parse-puzzle "2\n1 2")))
    (is (= nil (parse-puzzle "2\n1\n2"))))
  (testing "invalid lines"
    (is (= nil (parse-puzzle "2\n1 2\n3")))
    (is (= nil (parse-puzzle "2\n1\n2 3")))
    (is (= nil (parse-puzzle "size\nline")))))
