(ns npclj.target
  (:require [clojure.string :as str]))

(defn- get-tile [pzl coords]
  (get-in pzl (reverse coords)))

(defn- next-coords [coords dir]
  (map + coords dir))

(defn- next-dir-gen []
  "Creates stateful next direction generator"
  (let [cur (atom -1) dirs [[1 0] [0 1] [-1 0] [0 -1]]]
    #(dirs (mod (swap! cur inc) (count dirs)))))

(defn- gen-target- [sz]
  "Generates solved puzzle of size sz"
  (loop [next-dir (next-dir-gen)
         target (into [] (repeat sz (into [] (repeat sz 0))))
         i 1
         coords [0 0]
         dir (next-dir)]
    (if (= i (* sz sz))
      target
      (let [tile (get-tile target (next-coords coords dir))
            change-dir? (or (nil? tile) (pos? tile))
            dir (if change-dir? (next-dir) dir)]
        (recur next-dir
               (assoc-in target (reverse coords) i)
               (inc i)
               (next-coords coords dir)
               dir)))))

;; memized version of gen-target
(def gen-target (memoize gen-target-))
