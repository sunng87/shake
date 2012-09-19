(ns shake.core-test
  (:use clojure.test
        shake.core))

(deftest dummy-test
  (testing "a deadly simple test to make sure it initialized"
    (let [proc (uname -s -n -m)]
      (is (not (nil? proc))))))

(deftest print-test
  (testing "a binding that shows ouput directly"
    (binding [*print-output* true]
      (ps aux))))

