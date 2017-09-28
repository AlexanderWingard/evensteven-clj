(ns evensteven.core-test
  (:require
   [evensteven.even :as e]
   [cljs.test :as t :refer-macros [async deftest is testing]]))

(def example-trip
  {:name "Bosnien"
   :members ["Alex" "Sadik" "Hussein" "Joachim" "Patrik"]
   :transactions [{:comment "Hyrbil"
                   :payments [{:amount 80
                               :splitters ["Alex"]}
                              {:amount 20
                               :splitters ["Sadik"]}]
                   :subsplits [{:amount 20
                                :splitters ["Alex" "Sadik"]}]}
                  {:comment "Hyrbil"
                   :payments [{:amount 80
                               :splitters ["Alex"]}
                              {:amount 20
                               :splitters ["Sadik"]}]
                   :subsplits [{:amount 20
                                :splitters ["Alex" "Sadik"]}]}]})
(def expected [{"Alex" 0 "Sadik" 0 "Hussein" 0 "Joachim" 0 "Patrik" 0}
               {"Alex" 54, "Sadik" -6, "Hussein" -16, "Joachim" -16, "Patrik" -16}
               {"Alex" 108, "Sadik" -12, "Hussein" -32, "Joachim" -32, "Patrik" -32}])

(deftest the-truth
  (is (= expected (e/calculate example-trip))))

(def example-payment
  {:name "Example payment"
   :members ["Alex" "Sadik"]
   :transactions [{:comment "Pay"
                   :payments [{:amount 100
                               :splitters ["Alex"]}]
                   :splitters ["Sadik"]}
                  {:comment "Pay"
                   :payments [{:amount 20
                               :splitters ["Sadik"]}]
                   :splitters ["Alex"]}]})

(def expected-payment {"Alex" 80 "Sadik" -80})

(deftest test-payment
  (is (= expected-payment (last (e/calculate example-payment)))))

(def example-currency
  {:name "Example payment"
   :members ["Alex" "Sadik"]
   :currencies {"KN" 4}
   :transactions [{:comment "Pay"
                   :currency "KN"
                   :payments [{:amount 100
                               :splitters ["Alex"]}]}
                  {:comment "Pay"
                   :payments [{:amount 25
                               :splitters ["Sadik"]}]}
                  {:comment "Pay"
                   :payments [{:amount 100
                               :splitters ["Alex"]}]}]})

(def expected-currency {"Alex" 50 "Sadik" -50})

(deftest test-currency
  (is (= expected-currency (last (e/calculate example-currency))))
  (is (= 150 (e/tag-sums-sum (e/tag-sums example-currency)))))


(t/run-tests)
