(ns npclj.solve
  (:require [clojure.data.priority-map :refer [priority-map]]
            [npclj.puzzle :as puzzle]
            [npclj.target :refer [generate]]
            [npclj.heuristics :refer [manhattan]]))

(defn solve [pzl heur]
  (let [target (generate (count pzl))
        eval (partial puzzle/eval heur)]
    (loop [open-set (priority-map pzl (eval pzl))
           closed-set #{}
           cur (first (peek open-set))]
      (if (= cur target) cur nil))))

(solve [[1, 2], [0, 3]] manhattan)
