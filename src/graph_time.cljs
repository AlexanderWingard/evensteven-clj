(ns evensteven.graph-time
  (:require
   [cljsjs.d3 :as d3]))

(defn render [id data color]
  (let [select (.. js/d3
                   (select id))
        rect (.. select node getBoundingClientRect)
        height (.-height rect)
        width (.-width rect)
        max-y (apply max (mapcat #(map js/Math.abs (vals %1)) data))
        members (clj->js (keys (first data)))
        offset-scale (.. js/d3
                         (scaleBand)
                         (domain members)
                         (range #js [-4 4]))
        x-scale (.. js/d3
              (scaleLinear)
              (range #js [10 (- width 10)])
              (domain #js [0 (- (count data) 1)]))
        y-scale (.. js/d3
              (scaleLinear)
              (range #js [10 (- height 10)])
              (domain #js [max-y (- max-y)]))]
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
          (attr "stroke-width" 2))
      (.each merged (fn [member]
                      (this-as t
                        (let [select (.. js/d3
                                         (select t)
                                         (select "path")
                                         (datum (clj->js data)))
                              line (.. js/d3
                                       (line)
                                       (x (fn [d i] (x-scale i)))
                                       (y (fn [d] (+ (offset-scale member)
                                                     (y-scale (aget d member))))))]
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
                                                (attr "r" 5)
                                                (attr "fill" (fn [d] (color member)))
                                                (attr "cy" (fn [d] (+ (offset-scale member)
                                                                      (y-scale (aget d member)))))
                                                (attr "cx" (fn [d i] (x-scale i))))])))))))
