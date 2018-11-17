(ns npclj.gen-target
  (:require [clojure.string :as str]))

(defn- get-tile [pzl coords]
  (get-in pzl (reverse coords)))

(defn- inside? [pzl coords]
  (number? (get-tile pzl coords)))

(defn- next-coords [coords dir]
  (map + coords dir))

(defn- next-dir-gen []
  "Creates stateful next direction generator"
  (let [cur (atom 0) dirs [[1 0] [0 1] [-1 0] [0 -1]]]
    #(let [dir (dirs (mod @cur (count dirs)))]
       (swap! cur inc)
       dir)))

(defn gen-target [sz]
  "Generates solved puzzle of size sz"
  (loop [next-dir (next-dir-gen)
         target (into [] (repeat sz (into [] (repeat sz 0))))
         i 1
         coords [0 0]
         dir (next-dir)]
    (if (>= i (* sz sz))
      target
      (let [tile (get-tile target (next-coords coords dir))
            change-dir? (or (nil? tile) (pos? tile))
            dir (if change-dir? (next-dir) dir)]
        (recur next-dir
               (assoc-in target (reverse coords) i)
               (inc i)
               (next-coords coords dir)
               dir)))))

(= (gen-target 1) [[0]])
(= (gen-target 2) [[1 2]
                   [0 3]])
(= (gen-target 3) [[1 2 3]
                   [8 0 4]
                   [7 6 5]])
(= (gen-target 4) [[1 2 3 4]
                   [12 13 14 5]
                   [11 0 15 6]
                   [10 9 8 7]])
(= (gen-target 5) [[1 2 3 4 5]
                   [16 17 18 19 6]
                   [15 24 0 20 7]
                   [14 23 22 21 8]
                   [13 12 11 10 9]])
