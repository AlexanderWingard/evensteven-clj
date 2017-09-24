(ns evensteven.even
 (:require [clojure.core.reducers :as r]) )

(defn splitmount [split]
  (zipmap (:splitters split) (repeat (/ (:amount split) (count (:splitters split))))))

(defn calc-new [transaction acc]
  (let [total (- (reduce + (map :amount (:payments transaction)))
                 (reduce + (map :amount (:subsplits transaction))))
        with-payments (r/reduce (fn [acc elem]
                                    (merge-with + (splitmount elem) acc))
                                  (:payments transaction))
        with-subsplits (r/reduce (fn [acc elem]
                                    (merge-with + (splitmount elem) acc))
                                  (:subsplits transaction))
        rest-split (splitmount (assoc transaction :amount total))
        ]
    (merge-with - (merge-with + acc with-payments) with-subsplits rest-split)))

(defn calculate [trip]
  (r/reduce (fn [acc elem] (conj acc (calc-new (merge {:splitters (:members trip)} elem) (last acc))))
              [(zipmap (:members trip) (repeat 0))]
              (:transactions trip)))
