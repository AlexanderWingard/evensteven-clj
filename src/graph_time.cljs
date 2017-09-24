(ns evensteven.graph-time
  (:require
   [cljsjs.d3 :as d3]))

(defn render [id data]
  (let [select (.. js/d3
                   (select id))
        rect (.. select node getBoundingClientRect)
        height (.-height rect)
        width (.-width rect)
        max-y (apply max (map #(apply max (vals %1)) data))
        members (clj->js (keys (first data)))
        color (.. js/d3
                  (scaleOrdinal js/d3.schemeCategory10))
        x-scale (.. js/d3
              (scaleLinear)
              (range #js [0 width])
              (domain #js [0 (- (count data) 1)]))
        y-scale (.. js/d3
              (scaleLinear)
              (range #js [0 height])
              (domain #js [(- max-y) max-y]))]
    (let [select (.. select
                     (selectAll ".dotgroup")
                     (data members))
          enter (.. select
                    (enter)
                    (append "g")
                    (attr "class" "dotgroup"))
          merged (.. select
                     (merge enter))]
      (.. enter
          (append "path")
          (attr "fill" "none")
          (attr "stroke-width" 4))
      (.each merged (fn [member]
                      (this-as t
                        (let [select (.. js/d3
                                         (select t)
                                         (select "path")
                                         (datum (clj->js data)))
                              line (.. js/d3
                                       (line)
                                       (x (fn [d i] (x-scale i)))
                                       (y (fn [d] (y-scale (aget d member)))))]
                          (.. select
                              (attr "stroke" (fn [d] (color member)))
                              (attr "d" line)))
                        (let [select (.. js/d3
                                                (select t)
                                                (selectAll "circle")
                                                (data (clj->js data)))
                                     enter (.. select
                                               (enter)
                                               (append "circle"))
                                     merged (.. select
                                                (merge enter)
                                                (attr "r" 7)
                                                (attr "fill" (fn [d] (color member)))
                                                (attr "cy" (fn [d] (y-scale (aget d member))))
                                                (attr "cx" (fn [d i] (x-scale i))))])))))))
