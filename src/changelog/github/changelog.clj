(ns changelog.github.changelog
 (:require [tentacles.repos :as repos]
           [tentacles.data :as data]
           [clojure.string :refer string]
           [changelog.markdown.changelog :as markdown]))


(def changelog-files #{"CHANGELOG"      "CHANGELOG.MD"
                       "CHANGES"        "CHANGES.MD"
                       "RELEASE_NOTES"  "RELEASES_NOTES.MD"})

(def default-branch "master")

(defn file-contents [user repo path]
  "Given user, repo, and path we will fetch the file and returns it contents"
  ((repos/contents user repo path {:str? true}) :content))


(defn root-files [user repo]
  "Returns all the root files in the the given repo"
  (let [files ((data/tree user repo default-branch {:contents true}) :tree)
        is-file? (fn [node] (= "blob" (node :type)))
        get-path (fn [blob] (blob :path))]
    (map get-path (filter is-file? files))))


(defn find-changelog-file-names [user repo]
  "Given a user and repo we will find all changelog files by their name"
  (filter #(contains? changelog-files (string/upper-case %)) (root-files user repo)))

(defn parse-changelog-from-repo [user repo]
  "Given a user and repo, it will return a parsed changelog from the repo"
  (let [file-names (find-changelog-file-names user repo)
        files (map (partial file-contents user repo) file-names)]
    (merge (filter empty? (map markdown/changelog files)))))

(defn changelog [user repo]
  "Given a user and repo, we will try to find out it's changelog.
  We will check his releases and he if dosen't have a releases,
  We will find out his changelog from the changelog files"
  {})

