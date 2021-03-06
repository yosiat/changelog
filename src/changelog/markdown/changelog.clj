(ns changelog.markdown.changelog
  (:require [changelog.markdown.manip :refer :all]))

(defn- tags-only [tags]
  (filter map? tags))

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

  ([tags] (parse-changelog (tags-only tags) (sorted-map) nil)))


(defn changelog [markdown]
  "Parse the changelog, parse it's markdown and returns
  a parsed changelog (see 'parse-changelog')"
  (parse-changelog (parse-markdown markdown)))

