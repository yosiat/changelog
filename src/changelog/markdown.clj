(ns changelog.markdown
  (:require [endophile.core :as ep]
            [changelog.version :refer :all]))


(def markdown-headline-tags #{:h1 :h2 :h3})
(def markdown-list-tags #{:ul :ol})

(defn parse-markdown [markdown]
  "Parses the markdown using endophile"
  (ep/to-clj (ep/mp markdown)))

(defn is-headline [tag]
  "Checks whether this tag is an headline tag (h1, h2 or h3)"
  (contains? markdown-headline-tags (tag :tag)))

(defn version-headline [tag]
  "Returns the version from this headline, if there is no version or this not
  an headline - nil will be returned"
  (if (is-headline tag)
      (first (map extract-version (tag :content)))
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
