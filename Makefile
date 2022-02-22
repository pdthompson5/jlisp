.Phony: jlisp
jlisp:
	mvn clean compile exec:java -Dexec.args="test_file.lisp"

.PHONY: jlisp_repl
jlisp_repl:
	mvn clean compile exec:java