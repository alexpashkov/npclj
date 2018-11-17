(ns npclj.core
  (:gen-class))

(->> "asdf"
     (map identity)
     (apply str))

(defn read-pzl [from]
  (doseq [line (line-seq (java.io.BufferedReader. from))]
    (println line)))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (read-pzl *in*))

(assoc [1 2 3] 2 1000000)

(get-in [[1]] [0 0])
