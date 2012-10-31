(ns shake.static
  (:refer-clojure :only [defn- let dorun map or re-pattern
                         reify memfn partial ->])
  (:use [shake.core :only [create-shake-exec-var]])
  (:import [java.io File FileFilter]))

(def ^:private xfilter
  (reify FileFilter
    (accept [this f]
      (.canExecute f))))

(defn- generate-vars [dir]
  (let [files (map (memfn getName)
                   (-> dir (File.) (.listFiles xfilter)))]
    (dorun (map (partial create-shake-exec-var "shake.static") files))))

(dorun (map generate-vars
            (clojure.string/split
             (or
              (System/getenv "SHAKE_PATH")
              (System/getenv "PATH"))
             (re-pattern (System/getProperty "path.separator")))))

