(ns npclj.core
  (:gen-class))

(str "asdffsad" "asdf")

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
