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

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; String matching
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn all-matches
  "Finds all matches for a regular expression, allowing for overlap.
  i.e. (all-matches #\"zero|one\" \"zerone\") => [\"zero\" \"one\"]
  See https://www.regular-expressions.info/lookaround.html for more details"
  [re-string s]
  (map second
       (-> (format "(?=(%s))"
                   re-string)
           re-pattern
           (re-seq s))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; String matching
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(def scrambled-rows
  (-> "./resources/day1.txt"
      io/file
      slurp
      (string/split #"\n")))

(defn calibration-value
  [dictionary s]
  (let [ms (->> s
                (all-matches (string/join "|" (keys dictionary)))
                (map (partial get dictionary)))]
    (str (first ms) (last ms))))

(def part1-answer
  (->> scrambled-rows
       (map (partial calibration-value NUMERIC-STRINGS))
       (map parse-long)
       (reduce +)))

(def part2-answer
  (->> scrambled-rows
       (map (partial calibration-value (merge NUMERIC-STRINGS NUMERIC-WORDS)))
       (map parse-long)
       (reduce +)))
