(ns npclj.gen-target
  (:require [clojure.string :as str]))

(defrecord Coords [x y])
(defn next-corrds [pzl cur])

(defn gen-target [sz]
  "Generates a puzzle of size sz"
  (loop [i 0
         target (apply vector (repeat sz (apply vector (repeat sz 0))))]
    target))

(assoc-in (gen-target 3) [0 0] 5)
