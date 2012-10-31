(ns shake.core
  (:use [clojure.java.io :only [IOFactory default-streams-impl]]))

(def ^:dynamic *print-output* false)

(extend Process
  IOFactory
  (assoc default-streams-impl
    :make-input-stream (fn [^Process x opts] (.getInputStream x))
    :make-output-stream (fn [^Process x opts] (.getOutputStream x))))

(defn create-shake-exec-var [ns-name n]
  (binding [*ns* (the-ns (symbol ns-name))]
    (eval `(defmacro ~(symbol n) [& args#]
             (let [str-args#
                   (for [x# args#
                         :let [y# (cond
                                   (= (str x#) "$") nil
                                   (list? x#) x#
                                   (.startsWith (str x#) "$") (symbol (subs (str x#) 1))
                                   :else (str x#))]
                         :when y#] y#)
                   proc-builder-args# (conj str-args# ~n)
                   proc-sym# (gensym "proc")]
               `(let [~proc-sym# (.start (ProcessBuilder.
                                          (list ~@proc-builder-args#)))]
                  (if *print-output*
                    (print (slurp (.getInputStream ~proc-sym#)))
                    ~proc-sym#)))))))

(defn declare-exec [n]
  (create-shake-exec-var "shake.core" n))

