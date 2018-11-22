(ns npclj.solve
  (:require [clojure.data.priority-map :refer [priority-map]]
            [npclj.gen-target :refer [gen-target]]
            [npclj.heuristics :refer [manhattan]]))

(defrecord with-parent [pzl parent])

(defn solve [pzl heur]
  (let [target (gen-target (count pzl))]
    (loop [open-set (priority-map
                     (->with-parent pzl nil) (heur pzl))
           closed-set #{}
           cur (first (peek open-set))]
      (if (= (:pzl cur) target) cur nil))))

(solve [[1, 2], [0, 3]] manhattan)
