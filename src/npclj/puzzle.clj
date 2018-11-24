(ns npclj.puzzle)

(def directions [[1 0] [0 1] [-1 0] [0 -1]])

(defn find-tile [pzl tile]
  "Find coordinates of a tile, returns nil if not found"
  (loop [y 0]
    (when (< y (count pzl))
      (let [x (.indexOf (pzl y) tile)]
        (if (>= x 0)
          [x y]
          (recur (inc y)))))))

(defn get-tile [pzl coords]
  (get-in pzl (reverse coords)))

(defn with-parent [pzl parent]
  "Associates a parent with a puzzle"
  (with-meta pzl {:parent parent}))

(defn get-parent [pzl]
  (:parent (meta pzl)))

(defn count-parents [pzl]
  (loop [pzl pzl count 0]
    (if pzl (recur (get-parent pzl) (inc count)) count)))

(defn a*-eval [heur pzl]
  "Evaluates puzzle "
  (+ (count-parents pzl) (heur pzl)))

(defn coords-within? [pzl coords]
  (every? #(and (>= % 0) (< % (count pzl))) coords))
