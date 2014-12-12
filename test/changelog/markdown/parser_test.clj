(ns changelog.markdown.parser-test
  (:require [clojure.test :refer :all]
            [changelog.markdown.parser :refer :all]))

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

    ;; check the whole shabeng
    (is (= { "0.2" (list "Bug 10" "Bug 9" "Bug 8")
             "0.7" (list "Bug 1" "Bug 2" "Bug 3") }
           (parse simple-changelog)))))



(run-tests)
