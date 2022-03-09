
FILE := test_file.lisp

.Phony: jlisp
jlisp:
	mvn clean compile 
	mvn exec:java -Dexec.args="$(FILE)"

.PHONY: jlisp_repl
jlisp_repl:
	mvn clean compile exec:java