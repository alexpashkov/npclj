(ns npclj.gen-target
  (:require [clojure.string :as str]))

(defn- next-dir-gen []
  "Creates stateful next direction generator"
  (let [cur (atom 0) dirs [[1 0] [0 1] [-1 0] [0 -1]]]
    #(let [dir (dirs (mod @cur (count dirs)))]
       (swap! cur inc)
       dir)))

(defn- coords-valid? [pzl coords]
  "Checks if coords are direction is illegal"
  (or (pos? (get-in pzl coords))
      (not-every? #(and (>= % 0) (< % (dec (count pzl)))) coords)))

(defn- next-coords-gen []
  "Returns a stateful generator of next coordinates"
  (let [next-dir (next-dir-gen)]
    (fn [pzl cur-coords dir]
      (let [cur-coords-no-dir-change (map + cur-coords dir)])
      (if (coords-valid? pzl cur-coords-no-dir-change) cur-coords-no-dir-change ()))))

(defn gen-target [sz]
  "Generates a puzzle of size sz"
  (let [next-coords (next-coords-gen)]
    (loop [target (apply vector (repeat sz (apply vector (repeat sz 0))))
           coords [0 0]
           i 0]
      (if (< i (* sz sz))
        (recur (assoc-in target coords i)
               (next-coords target coords dir)
               (inc i))
        target))))

(assoc-in (gen-target 3) [0 0] 5)
