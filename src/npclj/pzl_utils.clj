(ns npclj.pzl-utils)

(defn find-tile [pzl tile]
  "Find coordinates of a tile, returns nil if not found"
  (loop [y 0]
    (when (< y (count pzl))
      (let [x (.indexOf (pzl y) tile)]
        (if (>= x 0)
          [x y]
          (recur (inc y)))))))
