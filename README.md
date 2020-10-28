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

Evaluation count : 60 in 60 samples of 1 calls.
             Execution time mean : 1.344263 sec
    Execution time std-deviation : 6.533120 ms
   Execution time lower quantile : 1.337213 sec ( 2.5%)
   Execution time upper quantile : 1.356408 sec (97.5%)
                   Overhead used : 1.905512 ns
```

```clojure
(bench (tarai 15 5 0))

Evaluation count : 60 in 60 samples of 1 calls.
             Execution time mean : 42.311679 sec
    Execution time std-deviation : 402.154581 ms
   Execution time lower quantile : 41.899797 sec ( 2.5%)
   Execution time upper quantile : 43.106598 sec (97.5%)
                   Overhead used : 2.278610 ns
```
