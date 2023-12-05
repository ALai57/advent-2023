(ns day2
  (:require [clojure.java.io :as io]
            [clojure.string :as string]))


(def raw-data
  (-> "./resources/day2.txt"
      io/file
      slurp
      (string/split #"\n")))


(defn entry->map
  "3 blue"
  [string]
  (let [[n color] (string/split string #" ")]
    {(keyword color) (parse-long n)}))

(defn parse-handful
  "3 blue, 4 red"
  [string]
  (into {}
        (map (comp entry->map string/trim)
             (string/split string #","))))

(defn parse-games
  "3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green"
  [string]
  (mapv (comp parse-handful string/trim) (string/split string  #";")))

(defn parse-index
  "Game 1"
  [string]
  (parse-long (second (string/split string #" "))))

(defn parse-row
  [row-string]
  (let [[game-index games] (string/split row-string #":")]
    {:index    (parse-index game-index)
     :handfuls (parse-games games)}))

(defn valid-handful?
  [{:keys [blue red green]
    :or   {red   0
           green 0
           blue  0}
    :as _handful}]
  (and (<= red 12)
       (<= green 13)
       (<= blue 14)))

(defn valid-game?
  [{:keys [handfuls]}]
  (every? valid-handful? handfuls))

(comment
  (let [games (map parse-row raw-data)]
    (->> games
         (filter valid-game?)
         (map :index)
         (reduce +)))
  ;; => "3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green"

  (parse-games "3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green")
  (parse-handful "3 blue, 4 red")
  (entry->map "3 blue")

  )

(def part1
  ;; => 2771
  (let [games (map parse-row raw-data)]
    (->> games
         (filter valid-game?)
         (map :index)
         (reduce +))))


(defn minimum-cubes
  [handfuls]
  (apply merge-with max handfuls))

(defn game-power
  [{:keys [blue red green]
    :or   {red   0
           green 0
           blue  0}
    :as _handful}]
  (* red green blue))

(def part-2
  ;; => 70924
  (let [games (map parse-row raw-data)]
    (->> games
         (map (comp game-power minimum-cubes :handfuls))
         (reduce +))))
