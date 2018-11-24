(ns npclj.puzzle)

(defn find-tile [pzl tile]
  "Find coordinates of a tile, returns nil if not found"
  (loop [y 0]
    (when (< y (count pzl))
      (let [x (.indexOf (pzl y) tile)]
        (if (>= x 0)
          [x y]
          (recur (inc y)))))))

(defn with-parent [pzl parent]
  "Associates parent with a puzzle"
  (with-meta pzl {:parent parent}))

(defn get-parent [pzl]
  (:parent (meta pzl)))

(defn count-parents [pzl]
  (loop [pzl pzl count 0]
    (if pzl (recur (get-parent pzl) (inc count)) count)))

(defn a*-eval [heur pzl]
  "Evaluates puzzle "
  (+ (count-parents pzl) (heur pzl)))
