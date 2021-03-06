(ns npclj.puzzle
  (:refer-clojure :exclude [prn reduce]))

(def directions [[1 0] [0 1] [-1 0] [0 -1]])

(defn size [pzl]
  (count (first pzl)))

(def find-tile
  (memoize
   (fn [pzl tile]
     "Find coordinates of a tile, returns nil if not found"
     (loop [y 0]
       (when (< y (count pzl))
         (let [x (.indexOf (pzl y) tile)]
           (if (>= x 0)
             [x y]
             (recur (inc y)))))))))

(defn flat-find-tile [pzl tile]
  (let [[x y] (find-tile pzl tile)]
    (+ x (* y (size pzl)))))

(defn get-tile [pzl coords]
  (get-in pzl (reverse coords)))

(defn with-parent [pzl parent]
  "Associates a parent with a puzzle"
  (with-meta pzl {:parent parent}))

(defn get-parent [pzl]
  (:parent (meta pzl)))

(defn get-parents
  ([pzl]
   (get-parents pzl false))
  ([pzl include-pzl?]
   (loop [parent (get-parent pzl)
          parents (if include-pzl? (list pzl) ())]
     (if parent
       (recur (get-parent parent) (conj parents parent))
       parents))))

(defn count-parents [pzl]
  (loop [pzl pzl count 0]
    (if pzl (recur (get-parent pzl) (inc count)) count)))

(defn a*-eval [heur pzl]
  "Evaluates puzzle "
  (+ (count-parents pzl) (heur pzl)))

(defn coords-within? [pzl coords]
  (every? #(and (>= % 0) (< % (count pzl))) coords))

(defn reduce
  [f seed pzl]
  (loop [acc seed pzl pzl y 0]
    (if-let [row (first pzl)]
      (recur
       (loop [acc acc row row x 0]
         (if-let [tile (first row)]
           (recur (f acc tile [x y]) (rest row) (inc x))
           acc))
       (rest pzl)
       (inc y))
      acc)))

(defn valid? [pzl]
  (let [size (size pzl)]
    (and (>= size 2)
         (vector? pzl)
         (every? #(and (= (count %) size)
                       (every? int? %)) pzl)
         (= (range (* size size))
            (-> pzl (flatten) (sort))))))

(defn prn [pzl]
  (doseq [row pzl]
    (apply println row)))

(defn get-neighboring-coords [pzl coords]
  (->> (map #(map + % coords) directions)
       (filter (partial coords-within? pzl))))

(defn swap-tiles [pzl a-coords b-coords]
  (let [a-tile (get-tile pzl a-coords)
        b-tile (get-tile pzl b-coords)]
    (-> pzl
        (assoc-in (reverse a-coords) b-tile)
        (assoc-in (reverse b-coords) a-tile))))
