(ns npclj.solve
  (:refer-clojure :exclude [eval])
  (:require [clojure.data.priority-map :refer [priority-map]]
            [npclj.pzl-utils :as puzzle]
            [npclj.target :refer [gen-target]]
            [npclj.heuristics :refer [manhattan]]))

(defn solve [pzl heur]
  (let [target (gen-target (count pzl))
        eval (partial puzzle/eval heur)]
    (loop [open-set (priority-map pzl (eval pzl))
           closed-set #{}
           cur (first (peek open-set))]
      (if (= cur target) cur nil))))

(solve [[1, 2], [0, 3]] manhattan)
