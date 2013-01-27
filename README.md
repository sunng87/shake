# shake

A Clojure library that shakes your shell.

## Usage

### Leiningen

```
[shake "0.4.0"]
```

### Just That Simple

```clojure
(require '[shake.static :as sh])

;; any shell command ...
(sh/uname -a) ;;returns a #<UNIXProcess java.lang.UNIXProcess@1833160>

;; using clojure variables (vars, local bindings) in shake
(let [home "/home/sunng87"]
  (sh/ls -l $home))

;; using clojure forms in shake
(sh/curl $(format "https://github.com/%s" "sunng87"))
```

### Too slow to initialize ?

The dynamic shake creates vars on demand. It only works in
scripts/repl mode because it's by hacking the compiler.

```clojure
(require '[shake.dynamic :as sh])

;; any shell command ...
(sh/uname -a) ;;returns a #<UNIXProcess java.lang.UNIXProcess@1833160>

;; using clojure variables (vars, local bindings) in shake
(let [home "/home/sunng87"]
  (sh/ls -l $home))

;; using clojure forms in shake
(sh/curl $(format "https://github.com/%s" "sunng87"))
```

### Could not compile (no such var)

The fallback solution, declare executable before you are using it.

```clojure
(require '[shake.core :as sh])

(sh/declare-exec "uname")
(sh/declare-exec "ls")
(sh/declare-exec "curl")

;; any shell command ...
(sh/uname -a) ;;returns a #<UNIXProcess java.lang.UNIXProcess@1833160>

;; using clojure variables (vars, local bindings) in shake
(let [home "/home/sunng87"]
  (sh/ls -l $home))

;; using clojure forms in shake
(sh/curl $(format "https://github.com/%s" "sunng87"))
```


### I/O

shake extends `Process` with clojure's IOFactory. So you can
`(input-stream)` or `(output-stream)` the process to get a streamed
I/O.

```clojure
;; print output of `uname -a`
(print (slurp (input-stream (sh/uname -a))))
```

## License

Copyright © 2012 Sun Ning <sunng@about.me>

Distributed under the Eclipse Public License, the same as Clojure.

