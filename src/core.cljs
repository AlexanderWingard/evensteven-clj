(ns evensteven.core
  (:require
   [evensteven.graph-time :as time]
   [goog.string :as gstring]
   [cljsjs.d3 :as d3]
   [clojure.string :as str]
   [clojure.core.reducers :as red]
   [evensteven.even :as e]
   [reagent-forms.core :refer [bind-fields]]
   [reagent.core :as r]))

(enable-console-print!)
(def colors (.. js/d3
                 (scaleOrdinal js/d3.schemeCategory10)))
(defn log [& args]
  (apply js/console.log args))
(def state (r/atom
            {:trips
             {"bosnien"
              {:name "Bosnien"
               :members ["Alex" "Sadik" "Hussein" "Joachim" "Patrik"]
               :currencies {"ba" 1
                            "hr" 4
                            "eu" 0.5
                            "se" 5}
               :transactions [;; {:payments [{:amount 1000
                              ;;              :splitters ["Alex" "Joachim" "Sadik" "Patrik"]}]
                              ;;  :splitters ["Alex" "Joachim" "Sadik" "Patrik"]
                              ;;  :currency "se"
                              ;;  :tag "flight"}
                              {:payments [{:amount 494.87
                                           :splitters ["Alex"]}]
                               :tag "car"}
                              {:payments [{:amount 352
                                           :splitters ["Sadik"]}]
                               :tag "car"}
                              {:payments [{:amount 106
                                           :splitters ["Sadik"]}]
                               :tag "food"}
                              {:payments [{:amount 151.59
                                           :splitters ["Joachim"]}]
                               :tag "food"}
                              {:payments [{:amount 179
                                           :splitters ["Patrik"]}]
                               :tag "food"}
                              {:payments [{:amount 46.53
                                           :splitters ["Alex"]}]
                               :subsplits [{:amount (+ 1.9 1.65 11.75)
                                            :splitters ["Alex" "Joachim" "Sadik" "Patrik"]}]
                               :tag "food"}
                              {:payments [{:amount 35
                                           :splitters ["Sadik"]}]
                               :tag "food"}
                              {:payments [{:amount 6.7
                                           :splitters ["Sadik"]}]
                               :tag "food"}
                              {:payments [{:amount 64
                                           :splitters ["Sadik"]}]
                               :tag "car"}
                              {:payments [{:amount 100
                                           :splitters ["Joachim"]}]
                               :currency "hr"
                               :tag "food"}
                              {:payments [{:amount 900
                                           :splitters ["Hussein"]}]
                               :currency "hr"
                               :tag "food"}
                              {:payments [{:amount 50
                                           :splitters ["Sadik"]}]
                               :currency "hr"
                               :tag "food"}
                              {:payments [{:amount 30
                                           :splitters ["Sadik"]}]
                               :currency "hr"
                               :tag "car"}
                              {:payments [{:amount 260
                                           :splitters ["Sadik"]}]
                               :currency "hr"
                               :tag "car"}
                              {:payments [{:amount 100
                                           :splitters ["Patrik"]}]
                               :currency "hr"
                               :tag "food"}
                              {:payments [{:amount 600
                                           :splitters ["Joachim"]}]
                               :currency "hr"
                               :tag "food"}
                              {:payments [{:amount 120
                                           :splitters ["Sadik"]}]
                               :currency "hr"
                               :tag "food"}
                              {:payments [{:amount 12
                                           :splitters ["Hussein"]}
                                          {:amount 100
                                           :splitters ["Sadik"]}]
                               :tag "food"}
                              {:payments [{:amount 18
                                           :splitters ["Joachim"]}]
                               :tag "food"}
                              {:payments [{:amount 45
                                           :splitters ["Sadik"]}]
                               :tag "food"}
                              {:payments [{:amount 3
                                           :splitters ["Hussein"]}]
                               :tag "car"}
                              {:payments [{:amount 50
                                           :splitters ["Joachim"]}]
                               :currency "eu"
                               :splitters ["Alex" "Hussein" "Joachim" "Patrik"]
                               :tag "food"}
                              {:payments [{:amount 10
                                           :splitters ["Joachim"]}]
                               :splitters ["Alex" "Hussein" "Joachim" "Patrik"]
                               :tag "food"}
                              {:payments [{:amount 3
                                           :splitters ["Joachim"]}]
                               :splitters ["Alex" "Hussein" "Joachim" "Patrik"]
                               :tag "car"}
                              {:payments [{:amount 14
                                           :splitters ["Joachim"]}]
                               :splitters ["Alex" "Hussein" "Joachim" "Patrik"]
                               :tag "food"}
                              {:payments [{:amount 2
                                           :splitters ["Sadik"]}]
                               :splitters ["Alex"]}
                              {:payments [{:amount 18
                                           :splitters ["Sadik"]}]
                               :currency "hr"
                               :tag "food"}
                              {:payments [{:amount 75.88
                                           :splitters ["Patrik"]}]
                               :tag "car"}
                              {:payments [{:amount 40
                                           :splitters ["Sadik"]}]
                               :tag "car"}
                              {:payments [{:amount 200
                                           :splitters ["Sadik"]}]
                               :splitters ["Alex" "Joachim" "Sadik" "Patrik"]
                               :currency "hr"
                               :tag "booze"}
                              {:payments [{:amount 210
                                           :splitters ["Sadik"]}]
                               :splitters ["Alex" "Joachim" "Sadik" "Patrik"]
                               :currency "hr"
                               :tag "booze"}
                              {:payments [{:amount 30
                                           :splitters ["Sadik"]}]
                               :currency "hr"
                               :tag "car"}
                              {:payments [{:amount 50
                                           :splitters ["Sadik"]}]
                               :currency "hr"
                               :tag "food"}
                              {:payments [{:amount 521
                                           :splitters ["Hussein"]}]
                               :currency "hr"
                               :tag "food"}
                              {:payments [{:amount 60
                                           :splitters ["Joachim"]}]
                               :currency "hr"
                               :tag "food"}
                              {:payments [{:amount 7
                                           :splitters ["Alex"]}]
                               :currency "hr"
                               :tag "car"}
                              {:payments [{:amount 2.5
                                           :splitters ["Sadik"]}]
                               :tag "car"}
                              {:payments [{:amount 102.5
                                           :splitters ["Patrik"]}]
                               :tag "food"}
                              {:payments [{:amount 8
                                           :splitters ["Sadik"]}]
                               :tag "food"}
                              {:payments [{:amount 43
                                           :splitters ["Hussein"]}]
                               :tag "food"}
                              {:payments [{:amount 90
                                           :splitters ["Sadik"]}]
                               :tag "food"}
                              {:payments [{:amount 50
                                           :splitters ["Alex"]}]
                               :splitters ["Sadik"]
                               :currency "hr"
                               :tag "transfer"}
                              {:payments [{:amount 20
                                           :splitters ["Alex"]}]
                               :splitters ["Sadik"]
                               :tag "transfer"}]}
              "simple" {
                        :members ["A" "B" "C" "D"]
                        :currencies {"se" 1
                                     "eu" .1}
                        :transactions [{:payments [{:amount 100
                                                    :splitters ["A"]}]
                                        :subsplits [{:amount 20
                                                     :splitters ["A" "B"]}]}
                                       {:payments [{:amount 10
                                                    :splitters ["B"]}]
                                        :splitters ["A"]
                                        :tag "transfer"}
                                       {:payments [{:amount 20
                                                    :splitters ["D"]}]
                                        :splitters ["A"]
                                        :tag "transfer"}]}}}))

(def staging (r/atom {}))

(defn hash-change []
  (swap! staging assoc :location (filter #(not (= "" %1))(str/split (.-hash (.-location js/window)) #"[#/]"))))
(aset js/window "onhashchange" hash-change)

(hash-change)

(defn field [type label state path]
  (let [error (get-in @state (conj path :error))]
    [:div.field {:class (when (some? error) "error")}
     [:label label]
     [bind-fields [:input {:field type :id (conj path :value)}] state]
     (when (some? error) [:div.ui.pointing.red.basic.label error])]))

(defn trips-view []
  [:div
   [:ui
    (for [trip (keys (:trips @state))]
      ^{:key trip} [:li[:a {:href (str "#" trip)} trip]])]
   [:div (field :text "Trip" staging [:trip])]
   [:button.ui.button {:on-click #(swap! state assoc-in [:trips (get-in @staging [:trip :value])] {})} "Add trip"]])

(defn even-view [even trip]
  (let [members (:members trip)
        currencies (:currencies trip)
        currency-saldos (e/currency-saldos even (:currencies trip))
        last (last even)]
    [:table.ui.fixed.striped.celled.table
     [:thead
      [:tr
       [:th]
       (for [[currency _] currencies]
         [:th.right.aligned [:i {:class (str currency " flag")}]])]]
     [:tbody
      (for [member members]
        [:tr {:style {:color (colors member)}}
         [:td
          member
          [(if (< 0 (get last member))
             :i.ui.green.caret.up.icon
             :i.ui.red.caret.down.icon)]]
         (for [[currency _] currencies]
           [:td.right.aligned {:style {:color (colors member)}}
            (gstring/format "%.2f" (get-in currency-saldos [currency member]))])])]]))

(defn render-tag [tag]
  (case tag
    "car" [:i.ui.large.car.icon]
    "flight" [:i.ui.large.plane.icon]
    "food" [:i.ui.large.food.icon]
    "booze" [:i.ui.large.cocktail.icon]
    nil [:i.ui.large.help.icon]
    tag))

(defn turnover-view [trip]
  (let [currencies (:currencies trip)
        tag-sum (e/tag-sums trip)
        tags (sort-by val > tag-sum)
        to (e/tag-sums-sum tag-sum)]
    [:table.ui.fixed.striped.celled.table
     [:thead
      [:tr
       [:th]
       (for [[c _] currencies]
         [:th.right.aligned
          [:i {:class (str c " flag")}]])]]
     [:tbody
      (for [[t s] tags]
        [:tr
         [:td.right.aligned (render-tag t)]
         (for [[c v] currencies]
           [:td.right.aligned (gstring/format "%.2f" (* v s))])])]
     [:tfoot  [:tr
               [:th {:style {:font-weight "bold"}} "Total"]
              (for [[c v] currencies]
                [:th.right.aligned  {:style {:font-weight "bold"}} (gstring/format "%.2f" (* v to))])]]]))

(defn app []
  [:div.ui.container
   [:div.ui.segment {:style {:margin-top "5em"}}
    (cond
      (and (< 0 (count (:location @staging)))
           (contains? (:trips @state) (nth (:location @staging) 0)))
      (let [trip (get-in @state [:trips (nth (:location @staging) 0)])
            even (e/calculate trip)]
        [:div
         [:h2.ui.header
          [:i.line.chart.icon]
          [:div.content "Balance"]]
         [time/graph-time {:style {:width "100%" :height "300px"}} even colors]
         [:h2.ui.header
          [:i.users.icon]
          [:div.content "Saldo"]]
         [even-view even trip]
         [:h2.ui.header
          [:i.calculator.icon]
          [:div.content "Turnover"]]
         [turnover-view trip]
         [:h2.ui.header
          [:i.database.icon]
          [:div.content "Transactions"]]
         (for [row (:transactions trip)]
           [:div (pr-str row)])])

      :else
      [trips-view])]])

(r/render [app] (js/document.getElementById "app"))
