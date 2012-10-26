(ns shake.core
  (:use [clojure.java.io :only [IOFactory default-streams-impl]])
  (:import [java.io File FileFilter]))

(def ^:dynamic *print-output* false)

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

(defn -var-missing [sym]
  (create-shake-exec-var sym))


