(ns shake.core-test
  (:use [clojure.test])
  (:use [clojure.java.io])
  (:require [shake.core :as sh]))

(deftest dummy-test
  (testing "a deadly simple test to make sure it initialized"
    (let [proc (sh/uname -s -n -m)]
      (is (not (nil? proc))))))

(deftest variable-test
  (testing "test when working with clojure variables in shake macros"
    (let [x "./project.clj"]
      (is (.startsWith (slurp (input-stream (sh/cat $x)))
                       "(defproject")))))

(deftest print-test
  (testing "a binding that shows ouput directly"
    (binding [sh/*print-output* true]
      (sh/ps))))

