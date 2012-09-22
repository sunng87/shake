# shake

A Clojure library shakes your shell.

## Usage

### Leiningen

```
[shake "0.2.0"]
```

### Just That Simple

```clojure
(require '[shake.core :as sh])

;; any shell command ...
(sh/uname -a) ;;returns a #<UNIXProcess java.lang.UNIXProcess@1833160>

;; using clojure variables (vars, local bindings) in shake
(let [home "/home/sunng87"]
  (sh/ls -l $home))

;; if you just want to see the output:
(binding [*print-output* true]
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

### Too Slow to Load (Verbose Clojure Warnings) ?

By default, *shake* indexes all executables under your **PATH**. To
override this behavior, set environment variable `SHAKE_PATH`
**before** you initialize the namespace `shake.core`.

## License

Copyright © 2012 Sun Ning <sunng@about.me>

Distributed under the Eclipse Public License, the same as Clojure.

