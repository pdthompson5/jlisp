
FILE := test_file.lisp
DEBUG :=

.Phony: jlisp
jlisp:
	mvn clean compile 
	mvn exec:java -Dexec.args="$(FILE) $(DEBUG)"

.PHONY: jlisp_repl
jlisp_repl:
	mvn clean compile exec:java