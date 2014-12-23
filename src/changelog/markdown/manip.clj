(ns changelog.markdown.manip
  (:require [endophile.core :as ep]
            [changelog.version :refer :all]))


(def markdown-headline-tags #{:h1 :h2 :h3})
(def markdown-list-tags #{:ul :ol})

(defn parse-markdown [markdown]
  "Parses the markdown using endophile"
  (try
    (ep/to-clj (ep/mp markdown))
    ;; Catching exception - if this is not a valid markdown
    ;; we would like to return empty vector.
    (catch Exception e [])))

(defn is-headline [tag]
  "Checks whether this tag is an headline tag (h1, h2 or h3)"
  (contains? markdown-headline-tags (tag :tag)))

(defn inner-content [header]
    "Returns the inner content of header (can handle nested headers) separated
    by space"
    (cond
        (map? header) (inner-content (header :content ""))
        (or (seq? header) (vector? header) (list? header)) (flatten (map inner-content header))
        (string? header) [header]))

(defn version-headline [tag]
  "Returns the version from this headline, if there is no version or this not
  an headline - nil will be returned"
  (if (is-headline tag)
      (first (remove nil? (map extract-version (inner-content tag))))
      nil))

(defn is-list [tag]
  "Checks whether this tag is a list tag (ul or ol)"
  (contains? markdown-list-tags (tag :tag)))

(defn format-list-values [lst]
  "Returns the content of the list tag as a list of html strings,
  if this not a list - nil is returned"
  (if (is-list lst)
      (->> (lst :content)
           (map #(% :content))
           (map ep/html-string))
      nil))
