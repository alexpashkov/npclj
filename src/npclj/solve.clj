(ns npclj.solve
  (:require [clojure.data.priority-map :refer [priority-map]]
            [npclj.puzzle :as puzzle]
            [npclj.generate :refer [generate]]))

(defn get-neighbors [pzl]
  (let [zero-coords (puzzle/find-tile pzl 0)]
    (->> (puzzle/get-neighboring-coords pzl zero-coords)
         (map (comp
               #(puzzle/with-parent % pzl)
               (partial puzzle/swap-tiles pzl zero-coords)))
         (filter (partial not= (puzzle/get-parent pzl))))))

(defn- update-max-count [obj]
  (vary-meta obj update :max-count max (count obj)))

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
               (-> (assoc open-set neighbor neighbor-cost)
                   (vary-meta update :selects inc))
               open-set)
             closed-set))
    [open-set closed-set]))

(defn solve [pzl heur]
  (let [target (generate (count pzl))
        peek (comp first peek)
        a*-eval (partial puzzle/a*-eval heur)
        handle-neighbors (partial handle-neighbors a*-eval)]
    (loop [open-set (-> (priority-map pzl (a*-eval pzl))
                        (with-meta {:selects 1
                                    :max-count 0}))
           closed-set (with-meta #{} {:max-count 0})]
      (when-let [cur (peek open-set)]
        (let [open-set (pop open-set)
              closed-set (conj closed-set cur)]
          (if (not= cur target)
            (let [[open-set closed-set] (handle-neighbors (get-neighbors cur)
                                                          open-set
                                                          closed-set)]
              (recur (update-max-count open-set)
                     (update-max-count closed-set)))
            (merge-with +
                        (meta open-set)
                        (meta closed-set)
                        {:states (puzzle/get-parents cur true)})))))))
