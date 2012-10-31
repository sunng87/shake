(ns shake.dynamic
  (:use [shake.core]))

(defn -var-missing [sym]
  (create-shake-exec-var "shake.dynamic" sym))

