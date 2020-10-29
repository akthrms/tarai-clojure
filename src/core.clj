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
  (letfn [(tarai [fx fy fz]
            (if (<= (fx) (fy))
              (fy)
              (tarai (fn [] (tarai (fn [] (- (fx) 1)) fy fz))
                     (fn [] (tarai (fn [] (- (fy) 1)) fz fx))
                     (fn [] (tarai (fn [] (- (fz) 1)) fx fy)))))]
    (tarai (fn [] x) (fn [] y) (fn [] z))))

(defn delay-force-tarai [x y z]
  (letfn [(tarai [x y z]
            (if (<= (force x) (force y))
              (force y)
              (tarai (delay (tarai (- (force x) 1) y z))
                     (delay (tarai (- (force y) 1) z x))
                     (delay (tarai (- (force z) 1) x y)))))]
    (tarai x y z)))

(defn -main []
  (with-progress-reporting (bench (delay-force-tarai 15 5 0))))
