(ns changelog.markdown-test
  (:require [clojure.test :refer :all]
            [changelog.markdown :refer :all]))

(def simple-changelog (str "# This is the simplest changelog\n"
                           "## Version 0.7\n"
                           "* Bug 1\n"
                           "* Bug 2\n"
                           "* Bug 3\n"
                           "\n"
                           "# Version 0.2\n"
                           "* Bug 10\n"
                           "* Bug 9\n"
                           "* Bug 8\n"
                           ))


(def changelog-headlines-without-version (str "# This is my headline\n"
                                              "## This is another headline\n"
                                              "* Go to eat\n"
                                              "* Write some tests\n"
                                              "## This my kabukation\n"))

(def changelog-headlines-with-version-without-lists (str "# Eggs 0.8\n"
                                                         "# Eggs 0.7\n"))

(deftest is-headline-on-non-healdine-tags
  (do (is (not (is-headline {:tag :ul, :content ()})))
      (is (not (is-headline {:tag :p, :content ()})))))


(deftest is-headline-on-headline-tags
  (do (is (is-headline {:tag :h1, :content ()}))
      (is (is-headline {:tag :h2, :content ()}))
      (is (is-headline {:tag :h3, :content ()}))))


(deftest is-list-on-non-list-tags
  (do (is (not (is-list {:tag :h1, :content ()})))
      (is (not (is-list {:tag :code, :content ()})))
      (is (not (is-list {:tag :p, :content ()})))))

(deftest is-list-on-list
  (do (is (is-list {:tag :ol, :content ()}))
      (is (is-list {:tag :ul, :content ()}))))


(deftest version-headline-on-ul
  (is (= nil (version-headline {:tag :ul, :content ()}))))

(deftest version-headline-on-headline-without-version
  (is (= nil (version-headline {:tag :h1, :content ["Stimulation"]}))))

(deftest version-headline-on-headline-with-version
  (do (is (= "0.1" (version-headline {:tag :h1, :content ["Version 0.1"]})))
      (is (= "0.5.2-rc1" (version-headline {:tag :h1, :content ["0.5.2-rc1"]})))))


(deftest format-list-values-on-non-list
  (is (= nil (format-list-values {:tag :h1, :content ["Hello World"]}))))

(deftest format-list-values-on-list
  (do (is (= ["Hello" "World"]
             (format-list-values {:tag :ul, :content [
                                                      {:tag :li, :content ["Hello"]}
                                                      {:tag :li, :content ["World"]}
                                                ]})))
      (is (= ["Hello <strong>Changelog</strong>" "World"]
             (format-list-values {:tag :ul, :content [
                                                      {:tag :li, :content [
                                                                           "Hello "
                                                                           {:tag :strong, :content ["Changelog"]}
                                                                           ]}
                                                      {:tag :li, :content ["World"]}
                                                ]})))
      ))

(deftest invalid-changelog
  (do (is (= {} (parse changelog-headlines-without-version)))
      (is (= {} (parse changelog-headlines-with-version-without-lists)))))


;; in this test, I test the keys and values separately and then the whole map
;; becuase if one them fail - it will be easier to triage
(deftest parse-versions-and-changelist
  (do
    ;; check the versions
    (is (= ["0.2" "0.7"] (keys (parse simple-changelog))))

    ;; check the values
    (is (= (list (list "Bug 10" "Bug 9" "Bug 8") (list "Bug 1" "Bug 2" "Bug 3"))
           (vals (parse simple-changelog))))

    ;; check the whoel shabeng
    (is (= { "0.2" (list "Bug 10" "Bug 9" "Bug 8")
             "0.7" (list "Bug 1" "Bug 2" "Bug 3") }
           (parse simple-changelog)))))



