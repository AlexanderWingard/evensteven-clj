(ns evensteven.even
 (:require [clojure.core.reducers :as r]) )

(defn splitmount [split]
  (zipmap (:splitters split) (repeat (/ (:amount split) (count (:splitters split))))))

(defn convert-currency [data currency currencies]
  (if (nil? currency)
    data
    (into {} (for [[k v] data] [k (/ v (get currencies currency))]))))
(defn calc-new [transaction acc currencies]
  (let [total (- (reduce + (map :amount (:payments transaction)))
                 (reduce + (map :amount (:subsplits transaction))))
        with-payments (r/reduce (fn [acc elem]
                                    (merge-with + (splitmount elem) acc))
                                  (:payments transaction))
        with-subsplits (r/reduce (fn [acc elem]
                                    (merge-with + (splitmount elem) acc))
                                  (:subsplits transaction))
        rest-split (splitmount (assoc transaction :amount total))
        convert (fn [data] (convert-currency data (:currency transaction) currencies))]
    (merge-with - (merge-with + acc (convert with-payments)) (convert with-subsplits) (convert rest-split))))

(defn calculate [trip]
  (r/reduce (fn [acc elem] (conj acc (calc-new (merge {:splitters (:members trip)} elem)
                                               (last acc)
                                               (:currencies trip))))
            [(zipmap (:members trip) (repeat 0))]
            (:transactions trip)))

(defn currency-convert [amount currency currencies]
  (if (nil? currency)
    amount
    (/ amount (get currencies currency))))

(defn tag-sums [trip]
  (dissoc (r/reduce (fn [acc transaction]
                      (merge-with + acc {(:tag transaction) (reduce + (map (fn [t] (currency-convert (:amount t) (:currency transaction) (:currencies trip))) (:payments transaction)))}))
                    {}
                    (:transactions trip))
          "transfer"))

(defn tag-sums-sum [ts]
  (reduce-kv (fn [m k v] (if (= k "transfer") m (+ m v))) 0 ts))

(defn currency-saldos [even currencies]
  (reduce-kv (fn [m k v] (assoc m k (into {} (for [[ke ve] (last even)] [ke (* ve v)])))) {} currencies))
