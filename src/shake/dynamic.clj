(ns shake.dynamic
  (:use [shake.core :only [create-shake-exec-var]]))

(defn -var-missing [sym]
  (create-shake-exec-var "shake.dynamic" sym))

