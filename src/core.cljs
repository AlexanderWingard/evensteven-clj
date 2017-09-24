(ns evensteven.core
  (:require
   [cljsjs.d3 :as d3]
   [clojure.string :as str]
   [clojure.core.reducers :as red]
   [reagent-forms.core :refer [bind-fields]]
   [reagent.core :as r]))

(enable-console-print!)
(defn log [& args]
  (apply js/console.log args))
(defonce state (r/atom {}))
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
   [:div (pr-str @state)]
   [:div (pr-str @staging)]
   (cond
     (and (< 0 (count (:location @staging)))
          (contains? (:trips @state) (nth (:location @staging) 0)))
     [:div "apa"]
     :else
     [trips-view])])

(r/render [app] (js/document.getElementById "app"))
