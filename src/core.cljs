(ns evensteven.core
  (:require
   [evensteven.graph-time :as time]
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
(def state (r/atom {:trips {"bosnien" {:name "Bosnien"
                                           :members ["Alex" "Sadik" "Hussein" "Joachim" "Patrik"]
                                           :currencies {"KN" 4}
                                           :transactions [{:payments [{:amount 106
                                                                       :splitters ["Sadik"]}]}
                                                          {:payments [{:amount 151.59
                                                                       :splitters ["Joachim"]}]}
                                                          {:payments [{:amount 179
                                                                       :splitters ["Patrik"]}]}
                                                          {:payments [{:amount 46.53
                                                                       :splitters ["Alex"]}]}
                                                          {:payments [{:amount 35
                                                                       :splitters ["Sadik"]}]}
                                                          {:payments [{:amount 6.7
                                                                       :splitters ["Sadik"]}]}
                                                          {:payments [{:amount 64
                                                                       :splitters ["Sadik"]}]}
                                                          {:payments [{:amount 100
                                                                       :splitters ["Joachim"]}]
                                                           :currency "KN"
                                                           }
                                                          {:payments [{:amount 900
                                                                       :splitters ["Hussein"]}]
                                                           :currency "KN"
                                                           }
                                                          {:payments [{:amount 50
                                                                       :splitters ["Sadik"]}]
                                                           :currency "KN"
                                                           }
                                                          {:payments [{:amount 30
                                                                       :splitters ["Sadik"]}]
                                                           :currency "KN"
                                                           }
                                                          {:payments [{:amount 260
                                                                       :splitters ["Sadik"]}]
                                                           :currency "KN"
                                                           }
                                                          {:payments [{:amount 100
                                                                       :splitters ["Patrik"]}]
                                                           :currency "KN"
                                                           }
                                                          {:payments [{:amount 600
                                                                       :splitters ["Joachim"]}]
                                                           :currency "KN"
                                                           }
                                                          {:payments [{:amount 120
                                                                       :splitters ["Sadik"]}]
                                                           :currency "KN"
                                                           }
                                                          {:payments [{:amount 12
                                                                       :splitters ["Hussein"]}
                                                                      {:amount 100
                                                                       :splitters ["Sadik"]}]}
                                                          {:payments [{:amount 18
                                                                       :splitters ["Joachim"]}]}
                                                          {:payments [{:amount 45
                                                                       :splitters ["Sadik"]}]}
                                                          {:payments [{:amount 3
                                                                       :splitters ["Hussein"]}]}
                                                          ]}}}))
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

(defn app []
  [:div.ui.container
   [:div.ui.segment
    (cond
      (and (< 0 (count (:location @staging)))
           (contains? (:trips @state) (nth (:location @staging) 0)))
      [:div
       [:svg {:id "vis" :style {:width "100%" :height "300px"}}]
       (let [trip (get-in @state [:trips (nth (:location @staging) 0)])]
         [:div
          (for [member (:members trip)]
                [:span {:style {:font-size "2em" :margin "0.1em" :color (colors member)}} member])
          (for [row (:transactions trip)]
            [:div (pr-str row)])
          [:h3 (pr-str (last(e/calculate trip)))]])]
      :else
      [trips-view])]])

(r/render [app] (js/document.getElementById "app"))
(time/render "#vis" (e/calculate(get-in @state [:trips "bosnien"])) colors)
