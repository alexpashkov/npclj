(ns npclj.is-solvable
  (:require [npclj.target :as target])
  (:gen-class))

(defn- count-inversions-for-index
  [flat-pzl idx]
  (let [pzl-len (count flat-pzl)
        cur-tile (nth flat-pzl idx)]
    (loop [i idx
           inversion 0]
      (if (< i pzl-len)
        (if (> cur-tile (nth flat-pzl i))
          (recur (+ 1 i) (+ 1 inversion))
          (recur (+ 1 i) inversion))
        inversion))))

(defn- count-inversions
  [pzl]
  (let [flat-pzl (flatten pzl)
        flat-pzl-no-zero (filter #(not= 0 %) flat-pzl)]
    (reduce
      (fn [inversions zipped-pzl]
        (let [[idx _] zipped-pzl]
          (+ inversions (count-inversions-for-index flat-pzl idx))
          ))
      (if (even? (count pzl)) (.indexOf flat-pzl 0) 0)
      (map-indexed (fn [idx item] [idx item]) flat-pzl))))

(defn is-solvable
  [pzl]
  (=
    (even? (count-inversions pzl))
    (even? (count-inversions (target/generate (count pzl))))))
