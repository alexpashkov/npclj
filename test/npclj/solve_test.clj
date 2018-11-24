(ns npclj.solve-test
  (:require [clojure.test :refer :all]
            [npclj.puzzle :as puzzle]
            [npclj.solve :refer :all]))

(deftest get-neighbors-test
  (is (= (-> [[1 2]
              [0 3]]
             (get-neighbors))
         [[[1 2]
           [3 0]]

          [[0 2]
           [1 3]]]))
  (is (= (-> [[1 2]
              [0 3]]
             (puzzle/with-parent [[1 2]
                                  [3 0]])
             (get-neighbors))
         [[[0 2]
           [1 3]]]))
  (is (= (-> [[1 2 3]
              [8 0 4]
              [7 6 5]]
             (puzzle/with-parent [[1 2 3]
                                  [0 8 4]
                                  [7 6 5]])
             (get-neighbors))
         [[[1 2 3]
           [8 4 0]
           [7 6 5]]

          [[1 2 3]
           [8 6 4]
           [7 0 5]]

          [[1 0 3]
           [8 2 4]
           [7 6 5]]])))
