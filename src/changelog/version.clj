(ns changelog.version)

(def is-version-pattern #"(\d+\.*)+(-[\w]+)*")

(defn- split-by-space [string]
  "Split a string to list of tokens by space (one or more)"
  (clojure.string/split string #"\s+"))

(defn- extract-version-from-token [token]
  "Extract a version from a token, if no version is found, nil is returned"
  (first (re-find is-version-pattern token)))

(defn extract-version [string]
  "Given a string, returns the version in the string (0.5.34.2, 0.1.0-alpha)
  and reutrns it, if there is no version - nil is returned"
  (first (remove nil? (flatten (map extract-version-from-token (split-by-space string))))))




