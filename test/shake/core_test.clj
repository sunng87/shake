(ns shake.core-test
  (:use clojure.test
        shake.core))

(deftest a-test
  (testing "a deadly simple test to make sure it initialized"
    (let [proc (uname -s -n -m)]
      (is (not (nil? proc)))
      (println (slurp (.getInputStream proc))))))

