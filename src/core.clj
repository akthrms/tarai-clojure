(ns core
  (:require [criterium.core :refer [bench with-progress-reporting]]))

(defn tarai [x y z]
  (if (<= x y)
    y
    (tarai (tarai (- x 1) y z)
           (tarai (- y 1) z x)
           (tarai (- z 1) x y))))

(defn memoized-tarai [x y z]
  (let [memo (atom {})]
    (letfn [(tarai [x y z]
              (or (get @memo [x y z])
                  (if (<= x y)
                    y
                    (let [result (tarai (tarai (- x 1) y z)
                                        (tarai (- y 1) z x)
                                        (tarai (- z 1) x y))]
                      (do (swap! memo assoc [x y z] result)
                          result)))))]
      (tarai x y z))))

(defn lazy-tarai [x y z]
  (letfn [(tarai [x y z]
            (if (<= (x) (y))
              (y)
              (tarai (fn [] (tarai (fn [] (- (x) 1)) y z))
                     (fn [] (tarai (fn [] (- (y) 1)) z x))
                     (fn [] (tarai (fn [] (- (z) 1)) x y)))))]
    (tarai (fn [] x) (fn [] y) (fn [] z))))

(defn -main []
  (with-progress-reporting (bench (lazy-tarai 15 5 0))))
