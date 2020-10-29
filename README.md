# 竹内関数 in Clojure

```clojure
(defn tarai [x y z]
  (if (<= x y)
    y
    (tarai (tarai (- x 1) y z)
           (tarai (- y 1) z x)
           (tarai (- z 1) x y))))
```

```clojure
(bench (tarai 13 7 0))

; Evaluation count : 60 in 60 samples of 1 calls.
;              Execution time mean : 1.344263 sec
;     Execution time std-deviation : 6.533120 ms
;    Execution time lower quantile : 1.337213 sec ( 2.5%)
;    Execution time upper quantile : 1.356408 sec (97.5%)
;                    Overhead used : 1.905512 ns

(bench (tarai 15 5 0))

; Evaluation count : 60 in 60 samples of 1 calls.
;              Execution time mean : 42.311679 sec
;     Execution time std-deviation : 402.154581 ms
;    Execution time lower quantile : 41.899797 sec ( 2.5%)
;    Execution time upper quantile : 43.106598 sec (97.5%)
;                    Overhead used : 2.278610 ns
```

```clojure
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
```

```clojure
(bench (memoized-tarai 13 7 0))

; Evaluation count : 316200 in 60 samples of 5270 calls.
;              Execution time mean : 190.141959 µs
;     Execution time std-deviation : 1.270458 µs
;    Execution time lower quantile : 188.723155 µs ( 2.5%)
;    Execution time upper quantile : 193.373780 µs (97.5%)
;                    Overhead used : 2.254548 ns

(bench (memoized-tarai 15 5 0))

; Evaluation count : 242100 in 60 samples of 4035 calls.
;              Execution time mean : 272.416784 µs
;     Execution time std-deviation : 26.309818 µs
;    Execution time lower quantile : 247.722408 µs ( 2.5%)
;    Execution time upper quantile : 334.899719 µs (97.5%)
;                    Overhead used : 2.254548 ns
```

```clojure
(defn lazy-tarai [x y z]
  (letfn [(tarai [fx fy fz]
            (if (<= (fx) (fy))
              (fy)
              (tarai (fn [] (tarai (fn [] (- (fx) 1)) fy fz))
                     (fn [] (tarai (fn [] (- (fy) 1)) fz fx))
                     (fn [] (tarai (fn [] (- (fz) 1)) fx fy)))))]
    (tarai (fn [] x) (fn [] y) (fn [] z))))
```

```clojure
(bench (lazy-tarai 13 7 0))

; Evaluation count : 1950780 in 60 samples of 32513 calls.
;              Execution time mean : 31.543290 µs
;     Execution time std-deviation : 1.881052 µs
;    Execution time lower quantile : 30.694189 µs ( 2.5%)
;    Execution time upper quantile : 36.065131 µs (97.5%)
;                    Overhead used : 2.254120 ns

(bench (lazy-tarai 15 5 0))

; Evaluation count : 1320120 in 60 samples of 22002 calls.
;              Execution time mean : 46.135455 µs
;     Execution time std-deviation : 1.475262 µs
;    Execution time lower quantile : 45.328301 µs ( 2.5%)
;    Execution time upper quantile : 50.337993 µs (97.5%)
;                    Overhead used : 2.254120 ns
```

```clojure
(defn delay-force-tarai [x y z]
  (letfn [(tarai [x y z]
            (if (<= (force x) (force y))
              (force y)
              (tarai (delay (tarai (- (force x) 1) y z))
                     (delay (tarai (- (force y) 1) z x))
                     (delay (tarai (- (force z) 1) x y)))))]
    (tarai x y z)))
```

```clojure
(bench (delay-force-tarai 13 7 0))

; Evaluation count : 9140220 in 60 samples of 152337 calls.
;              Execution time mean : 6.758895 µs
;     Execution time std-deviation : 394.171091 ns
;    Execution time lower quantile : 6.554295 µs ( 2.5%)
;    Execution time upper quantile : 7.629930 µs (97.5%)
;                    Overhead used : 2.249955 ns

(bench (delay-force-tarai 15 5 0))

; Evaluation count : 7537740 in 60 samples of 125629 calls.
;              Execution time mean : 7.767428 µs
;     Execution time std-deviation : 267.725678 ns
;    Execution time lower quantile : 7.582429 µs ( 2.5%)
;    Execution time upper quantile : 8.461426 µs (97.5%)
;                    Overhead used : 2.249955 ns
```
