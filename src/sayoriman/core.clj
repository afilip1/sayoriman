(ns sayoriman.core
  (:require [clojure.set :refer [difference intersection]])
  (:gen-class))

(def secret-word "perkele")
(def death-word "Sayori")

(defn cls
  []
  (dotimes [n 1000] (println)))

(defn count-mistakes
  [guessed-letters]
  (count (difference guessed-letters (set secret-word))))

(defn count-correct
  [guessed-letters]
  (count (intersection guessed-letters (set secret-word))))

(defn render-word
  [guessed-letters]
  (apply str
    (for [c secret-word]
      (if (contains? guessed-letters c)
        c
        "_"))))

(defn render-ui
  [guessed-letters]
  (cls)
  (println "Guessed letters:" (apply str guessed-letters))
  (println "How close are we? -" (subs death-word 0 (count-mistakes guessed-letters)))
  (println "Word:" (render-word guessed-letters))
  (print "Input: ")
  (flush))

(defn game-end
  [f]
  (cls)
  (f)
  (flush)
  (System/exit 0))

(defn game-over
  []
  (game-end #(dotimes [n 10000] (print "Just   Monika   "))))

(defn game-won
  []
  (game-end #(println "You win!")))

(defn game-loop
  [guessed-letters]
  (render-ui guessed-letters)
  (let [guess (read-line)]
    (if (= (count guess) 1)
      (let [new-guessed (conj guessed-letters (first guess))]
        (cond
          (= (count-mistakes new-guessed) (count death-word))
          (game-over)
          (= (count-correct new-guessed) (count (set secret-word)))
          (game-won)
          :else (recur new-guessed)))
      (recur guessed-letters))))

(defn -main
  [& args]
  (game-loop #{}))
