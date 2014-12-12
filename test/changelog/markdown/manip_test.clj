(ns changelog.markdown.manip-test
  (:require [clojure.test :refer :all]
            [changelog.markdown.manip :refer :all]))

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



(run-tests)
