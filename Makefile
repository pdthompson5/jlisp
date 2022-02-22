.Phony: jlox
jlox:
	mvn clean compile exec:java -Dexec.args="test_file.lisp"

.PHONY: jlox_repl
jlox_repl:
	mvn clean compile exec:java