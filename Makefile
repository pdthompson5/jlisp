ifeq (jlisp,$(firstword $(MAKECMDGOALS)))
  # use the rest as arguments for "run"
  RUN_ARGS := $(wordlist 2,$(words $(MAKECMDGOALS)),$(MAKECMDGOALS))
  # ...and turn them into do-nothing targets
  $(eval $(RUN_ARGS):;@:)
endif

BUILD_DIR := build

SOURCES := $(wildcard com/app/jlisp/*.java)
CLASSES := $(SOURCES:.java=.class)

JAVA_OPTIONS := -Werror

.PHONY: jlisp
jlisp: $(CLASSES)
	java com.app.jlisp.Jlisp $(RUN_ARGS)

com/app/jlisp/%.class: com/app/jlisp/%.java
	javac $<

.PHONY: clean
clean:
	-rm com/app/jlisp/*.class

