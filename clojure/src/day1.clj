(ns day1
  (:require [clojure.java.io :as io]
            [clojure.string :as string]))

(def NUMERIC-STRINGS
  {"0" "0"
   "1" "1"
   "2" "2"
   "3" "3"
   "4" "4"
   "5" "5"
   "6" "6"
   "7" "7"
   "8" "8"
   "9" "9"})

(def NUMERIC-WORDS
  {"zero"  "0"
   "one"   "1"
   "two"   "2"
   "three" "3"
   "four"  "4"
   "five"  "5"
   "six"   "6"
   "seven" "7"
   "eight" "8"
   "nine"  "9"})

(defn all-matches
  [re-string s]
  (map second
       (-> (format "(?=(%s))"
                   re-string)
           re-pattern
           (re-seq s))))

(defn all-numeric-string-matches
  [s]
  (let [dict NUMERIC-STRINGS]
    (map (partial get dict) (all-matches (string/join "|" (keys dict)) s))))

(defn all-numeric-matches
  [s]
  (let [dict (merge NUMERIC-STRINGS NUMERIC-WORDS)]
    (map (partial get dict) (all-matches (string/join "|" (keys dict)) s))))



(def scrambled-rows
  (-> "./resources/day1.txt"
      io/file
      slurp
      (string/split #"\n")))

scrambled-rows
;; => ["1abc2" "pqr3stu8vwx" "a1b2c3d4e5f" "treb7uchet"]



(comment
  (all-numeric-string-matches "12 33cc 2")
  ;; => ("1" "2" "3" "3" "2")

  (all-numeric-matches "zerone11twox32")
  ;; => ("zero" "one" "1" "1" "two" "3" "2")
  )

(defn calibration-value
  [match-fn s]
  (let [ms (match-fn s)]
    (str (first ms) (last ms))))

(def part1-answer
  (->> scrambled-rows
       (map (comp parse-long (partial calibration-value all-numeric-string-matches)))
       (reduce + )))

(def part2-answer
  (->> scrambled-rows
       (map (comp parse-long (partial calibration-value all-numeric-matches)))
       (reduce +)))

(comment
  (calibration-value all-numeric-matches (first scrambled-rows))
  )
