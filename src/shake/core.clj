(ns shake.core
  (:import [java.io File FileFilter]))

(def ^:dynamic *print-output* false)

(def ^:private xfilter
  (reify FileFilter
    (accept [this f]
      (.canExecute f))))

(defn- create-shake-exec-var [n]
  (eval `(defmacro ~(symbol n) [& args#]
           (let [str-args# (list* (map str args#))
                 proc-sym# (symbol "proc")]
             `(let [~proc-sym# (.start (ProcessBuilder.
                                  (conj '~str-args# ~~n)))]
                (if *print-output*
                  (print (slurp (.getInputStream ~proc-sym#)))
                  ~proc-sym#))))))

(defn- generate-vars [dir]
  (let [files (map (memfn getName)
                   (-> (File. dir) (.listFiles xfilter)))]
    (dorun (map create-shake-exec-var files))))

(dorun (map generate-vars
            (clojure.string/split
             (or
              (System/getenv "SHAKE_PATH")
              (System/getenv "PATH"))
             #":")))

