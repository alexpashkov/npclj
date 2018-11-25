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
         (map (comp
                #(puzzle/with-parent % pzl)
                (partial swap-tiles pzl zero-coords)))
         (filter (partial not= (puzzle/get-parent pzl))))))

(defn solve [pzl heur]
  (let [target (generate (count pzl))
        a*-eval (partial puzzle/a*-eval heur)
        open-set (atom (priority-map pzl (a*-eval pzl)))
        closed-set (atom #{})]
    (loop [cur (first (peek @open-set))]
      (swap! open-set pop)
      (swap! closed-set conj cur)
      (if (not= cur target)
        (do
          (doseq [neighbor (get-neighbors cur)]
            (let [neighbor-cost (a*-eval neighbor)]
              (when-let [neighbor-cost-in-open-set (@open-set neighbor)]
                (when (< neighbor-cost neighbor-cost-in-open-set)
                  (swap! open-set dissoc neighbor)))
              (when-let [neighbor-in-closed-set (@closed-set neighbor)]
                (when (< (puzzle/count-parents neighbor)
                         (puzzle/count-parents neighbor-in-closed-set))
                  (swap! closed-set dissoc neighbor)))
              (when-not (or (contains? @open-set neighbor)
                            (contains? @closed-set neighbor))
                (swap! open-set assoc neighbor neighbor-cost))))
          (recur (first (peek @open-set))))
        cur))))


(solve [[1 2], [0 3]] manhattan)
