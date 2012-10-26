(ns shake.core
  (:use [clojure.java.io :only [IOFactory default-streams-impl]])
  (:import [java.io File FileFilter]))

(def ^:dynamic *print-output* false)

#_(def ^:private xfilter
  (reify FileFilter
    (accept [this f]
      (.canExecute f))))

(extend Process
  IOFactory
  (assoc default-streams-impl
    :make-input-stream (fn [^Process x opts] (.getInputStream x))
    :make-output-stream (fn [^Process x opts] (.getOutputStream x))))

(defn- create-shake-exec-var [n]
  (binding [*ns* (the-ns (symbol "shake.core"))]
    (eval `(defmacro ~(symbol n) [& args#]
             (let [str-args#
                   (filter #(not (nil? %))
                           (map #(cond
                                  (= (str %) "$") nil
                                  (list? %) %
                                  (.startsWith (str %) "$") (symbol (subs (str %) 1))
                                  :else (str %))
                                args#))
                   proc-builder-args# (conj str-args# ~n)
                  proc-sym# (gensym "proc")]
               `(let [~proc-sym# (.start (ProcessBuilder.
                                          (list ~@proc-builder-args#)))]
                  (if *print-output*
                    (print (slurp (.getInputStream ~proc-sym#)))
                    ~proc-sym#)))))))

#_(defn- generate-vars [dir]
  (let [files (map (memfn getName)
                   (-> (File. dir) (.listFiles xfilter)))]
    (dorun (map create-shake-exec-var files))))

(defn -var-missing [sym]
  (create-shake-exec-var sym))

#_(dorun (map generate-vars
            (clojure.string/split
             (or
              (System/getenv "SHAKE_PATH")
              (System/getenv "PATH"))
             (re-pattern (System/getProperty "path.separator")))))

