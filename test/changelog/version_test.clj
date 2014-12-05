(ns changelog.version-test
  (:require [clojure.test :refer :all]
            [changelog.version :refer :all]))

(deftest major-minor-version
  (is (= "1.2" (extract-version "1.2"))))

(deftest major-minor-semi-version-a
  (is (= "1.2.0" (extract-version "1.2.0"))))

(deftest major-minor-semi-version-b
  (is (= "1.2.3.4" (extract-version "1.2.3.4"))))

(deftest version-with-stage
  (is (= "13.73.34-alpha" (extract-version "13.73.34-alpha"))))

(deftest version-with-stage-and-numbers
  (is (= "1.2.0-rc1" (extract-version "1.2.0-rc1"))))

(deftest extract-version-from-title
  (is (= "1.23.4" (extract-version "ProductName 1.23.4"))))

(deftest no-version-returns-nil
  (is (= nil (extract-version "havita and shnitzel"))))
