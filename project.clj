(defproject npclj "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/data.priority-map "0.0.10"]
                 [org.clojure/tools.cli "0.4.1"]
                 [http-kit "2.3.0"]
                 [compojure "1.6.1"]]
  :main ^:skip-aot npclj.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
