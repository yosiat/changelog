(ns changelog.github.releases
  [:require [tentacles.repos :as repos]])



(defn- create-release-tuple [release]
  "Returns a tuple, where the first item is the tag_name (which is the version),
  and the value is the release changes"
  [(:tag_name release) (:body release)])

(defn- tuples-to-sorted-map [tuples]
  (into (sorted-map) tuples))

(defn releases-changelog [user repo]
  "Given a use and repo, we will fetch it's releases and parse them.
  as a changelog"
  (map create-release-tuple (repos/releases user repo))

