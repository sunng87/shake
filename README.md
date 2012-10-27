# shake

A Clojure library shakes your shell.

## Usage

### Leiningen

```
[shake "0.3.0"]
```

### Just That Simple

```clojure
(require '[shake.core :as sh])

;; any shell command ...
(sh/uname -a) ;;returns a #<UNIXProcess java.lang.UNIXProcess@1833160>

;; using clojure variables (vars, local bindings) in shake
(let [home "/home/sunng87"]
  (sh/ls -l $home))

;; using clojure forms in shake
(sh/curl $(format "https://github.com/%s" "sunng87"))

;; if you just want to see the output:
(binding [sh/*print-output* true]
  (sh/uname -a))
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

