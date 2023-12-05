(ns day3
  (:require [clojure.java.io :as io]
            [clojure.string :as string]))

;; X Y grid
;;
;; 5 |
;; 4 |
;; 3 |
;; 2 |
;; 1 |
;; 0 |
;; - |- - - - - -
;;   |0 1 2 3 4 5
(comment
  {:n      467
   :coords [{:x 0 :y 0}
            {:x 1 :y 0}
            {:x 2 :y 0}]})

(def GRID
  (vec (reverse (string/split (slurp (io/file "./resources/day3.txt")) #"\n"))))

(defn parse-row
  ".664.598.."
  [row-number row-string]
  (loop [current-idx 0
         current-num {:number ""
                      :coords []}
         acc         []]
    ;;(println "current idx" current-idx " current-num" current-num "acc" acc)
    (if-let [c (get row-string current-idx)]
      (cond
        (Character/isDigit c)                 (recur (inc current-idx)
                                                     (-> current-num
                                                         (update :number str c)
                                                         (update :coords conj {:x current-idx
                                                                               :y row-number}))
                                                     acc)
        (zero? (count (:number current-num))) (recur (inc current-idx)
                                                     current-num
                                                     acc)
        :else                                 (recur (inc current-idx)
                                                     {:number ""
                                                      :coords []}
                                                     (conj acc (update current-num
                                                                       :number parse-long)))
        )
      (if (empty? (:number current-num))
        acc
        (conj acc (update current-num
                          :number parse-long))))))

(defn point-neighbors
  "{:x 1, :y 0}"
  [{:keys [x y] :as point}]
  (for [dx [-1 0 1]
        dy [-1 0 1]]
    {:x (+ dx x)
     :y (+ dy y)}))

(defn neighbors
  [{:keys [coords] :as _number}]
  (into #{} (mapcat point-neighbors coords)))

(comment
  (neighbors {:x 1, :y 0})
  (neighbors {:number 664, :coords [{:x 1, :y 0} {:x 2, :y 0} {:x 3, :y 0}]})
  )

(defn adjacent-symbol?
  [grid {:keys [number coords] :as candidate}]
  (some (fn string-symbol?
          [{:keys [x y] :as _point}]
          (let [v (get-in grid [y x])]
            (and v
                 (not (Character/isDigit v))
                 (not= \. v))))
        (neighbors candidate)))

(comment
  (parse-row 1 ".664.598..12")

  (adjacent-symbol? GRID {:number 664, :coords [{:x 1, :y 0} {:x 2, :y 0} {:x 3, :y 0}]})
  (adjacent-symbol? GRID {:number 755, :coords [{:x 6, :y 2} {:x 7, :y 2} {:x 8, :y 2}]})

  ;; => ({:number 664, :coords [{:x 1, :y 0} {:x 2, :y 0} {:x 3, :y 0}]}
  ;;     {:number 598, :coords [{:x 5, :y 0} {:x 6, :y 0} {:x 7, :y 0}]}
  ;;     {:number 755, :coords [{:x 6, :y 2} {:x 7, :y 2} {:x 8, :y 2}]}
  ;;     {:number 592, :coords [{:x 2, :y 3} {:x 3, :y 3} {:x 4, :y 3}]}
  ;;     {:number 58, :coords [{:x 7, :y 4} {:x 8, :y 4}]}
  ;;     {:number 617, :coords [{:x 0, :y 5} {:x 1, :y 5} {:x 2, :y 5}]}
  ;;     {:number 35, :coords [{:x 2, :y 7} {:x 3, :y 7}]}
  ;;     {:number 633, :coords [{:x 6, :y 7} {:x 7, :y 7} {:x 8, :y 7}]}
  ;;     {:number 467, :coords [{:x 0, :y 9} {:x 1, :y 9} {:x 2, :y 9}]}
  ;;     {:number 114, :coords [{:x 5, :y 9} {:x 6, :y 9} {:x 7, :y 9}]})
  ;;
  )

(def part1
  ;; => 527364
  (let [candidate-numbers  (->> GRID
                                (map-indexed parse-row)
                                flatten)]
    (->> candidate-numbers
         (filter (partial adjacent-symbol? GRID))
         (map :number)
         (reduce +))))

(defn get-neighboring-stars
  [grid {:keys [number coords] :as num}]
  (->> num
       neighbors
       (filter (fn star?
                 [{:keys [x y] :as point}]
                 (= \* (get-in grid [y x]))))
       (mapv (fn enrich
               [m]
               (assoc m :original-number number)))))

(def part2
  ;; => 79026871
  (let [candidate-numbers  (->> GRID
                                (map-indexed parse-row)
                                flatten)]
    (->> candidate-numbers
         (mapcat (partial get-neighboring-stars GRID))
         (group-by (juxt :x :y))
         (filter (fn two-neighbors?
                   [[coords nums]]
                   (= 2 (count nums))))
         (map (fn compute-power
                [[coords entries]]
                (apply * (map :original-number entries))))
         (reduce +))))
