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

(defn handle-neighbors [a*-eval neighbors open-set closed-set]
  (if-let [neighbor (first neighbors)]
    (let [neighbor-cost (a*-eval neighbor)
          neighbor-cost-in-open-set (open-set neighbor)
          open-set (if (and neighbor-cost-in-open-set
                            (< neighbor-cost
                               neighbor-cost-in-open-set))
                     (dissoc open-set neighbor)
                     open-set)
          neighbor-in-closed-set (closed-set neighbor)
          closed-set (if (and neighbor-in-closed-set
                              (< (puzzle/count-parents
                                  neighbor)
                                 (puzzle/count-parents
                                  neighbor-in-closed-set)))
                       (disj closed-set neighbor-in-closed-set)
                       closed-set)]
      (recur a*-eval
             (rest neighbors)
             (if-not (or (contains? open-set neighbor)
                         (contains? closed-set neighbor))
               (assoc open-set neighbor neighbor-cost)
               open-set)
             closed-set))
    [open-set closed-set]))

(defn solve [pzl heur]
  (let [target (generate (count pzl))
        peek (comp first peek)
        a*-eval (partial puzzle/a*-eval heur)
        handle-neighbors (partial handle-neighbors a*-eval)]
    (loop [open-set (priority-map pzl (a*-eval pzl))
           closed-set #{}]
      (let [cur (peek open-set)
            open-set (pop open-set)
            closed-set (conj closed-set cur)]
        (if (not= cur target)
          (let [[open-set closed-set] (handle-neighbors (get-neighbors cur)
                                                        open-set
                                                        closed-set)]
            (recur open-set closed-set))
          cur)))))

(puzzle/get-parents (solve [[0 1]
                            [3 2]] manhattan))

(puzzle/get-parents (solve [[8 0 1]
                            [5 4 2]
                            [7 6 3]] manhattan))

(puzzle/get-parents (solve [[2  12 4  5]
                            [11  13  3 8]
                            [10 1 14 6]
                            [9 15  7  0]] manhattan))
