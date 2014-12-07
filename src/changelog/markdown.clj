(ns changelog.markdown
  (:require [endophile.core :as ep]
            [changelog.version :refer :all]))


;; first we need to parse the changelog markdown
;; find all the h tags
;; try to extract the version
;;  -> read the nearest ul


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
  (if (is-list lst)
      (->> (lst :content)
           (map #(% :content))
           (map ep/html-string))
      nil))



(defn parse-changelog
  "Parsing the given parsed markdown and returns a map where the keys are the
  versions and the values the list of things that changes.

  example:
  For the next parsed changelog -
  # clojure
  ## 0.2
  * Adding **macros**
  * Improving performacne

  ## 0.1
  * Initial interpeter

  The output will be -
  {
    0.2: Adding <strong>macros</strong>, Improving performance
    0.1: Initial interpeter
  }
  "
  ;; Our execution plan:
  ;;
  ;; We have the rest of the tags
  ;; Our accumulated result
  ;; and the last version headline we saw
  ([tags result last-headline]
  ;; If this the end of iteration
  (if (empty? tags)
      result
      (let [current-tag (first tags)
            next-tags (rest tags)
            version (version-headline current-tag)]
          (cond
            ;; if current tag is a version let's continue
            ;; IMPORTANT: this line should be before the list check
            ;; so we will not stuck in infinite loop in-case there is no list
            ;; after a version headline
            (not (nil? version))
            (parse-changelog next-tags result version)

            ;; if we have headline, we are looking for list
            (and (not (nil? last-headline)) (is-list current-tag))
            (parse-changelog next-tags
                       (assoc result last-headline (format-list-values current-tag))
                       nil)

            :else (parse-changelog next-tags result last-headline)))))

  ([tags] (parse-changelog tags {} nil)))


(defn parse [changelog]
  (parse-changelog (parse-markdown changelog)))
