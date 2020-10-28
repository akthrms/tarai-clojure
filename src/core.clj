(ns core
  (:require [criterium.core :refer [bench with-progress-reporting]]))

(defn tarai [x y z]
  (if (<= x y)
    y
    (tarai (tarai (- x 1) y z)
           (tarai (- y 1) z x)
           (tarai (- z 1) x y))))

(defn -main []
  (with-progress-reporting (bench (tarai 15 5 0))))
