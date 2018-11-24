(ns npclj.solve
  (:require [clojure.data.priority-map :refer [priority-map]]
            [npclj.puzzle :as puzzle]
            [npclj.target :refer [generate]]
            [npclj.heuristics :refer [manhattan]]))

(defn- get-neighboring-coords [pzl coords]
  (->> (map #(map + % coords) puzzle/directions)
       (filter (partial puzzle/coords-within? pzl))))

(defn- swap-tiles [pzl a-coords b-coords]
  (let [a-tile (puzzle/get-tile pzl a-coords)
        b-tile (puzzle/get-tile pzl b-coords)]
    (-> pzl
        (assoc-in (reverse a-coords) b-tile)
        (assoc-in (reverse b-coords) a-tile))))

(defn get-neighbors [pzl]
  (let [zero-coords (puzzle/find-tile pzl 0)]
    (->> (get-neighboring-coords pzl zero-coords)
         (map (partial swap-tiles pzl zero-coords))
         (filter (partial not= (puzzle/get-parent pzl))))))

(defn solve [pzl heur]
  (let [target (generate (count pzl))
        a*-eval (partial puzzle/a*-eval heur)]
    (loop [open-set (priority-map pzl (a*-eval pzl))
           closed-set #{}
           cur (first (peek open-set))]
      (if (= cur target) cur nil))))

(solve [[1, 2], [0, 3]] manhattan)
