# Jlisp

## Basic information
### Code locations
Project source code can be found at `src/main/java/jlisp/`
The main class for this project is `src/main/java/jlisp/Jlisp.java`

### Executing the project
This is a Maven project. The following instructions assume that you have Maven installed.

Maven version used in testing: Apache Maven 3.8.4

Execute the project with the following command: 

`make jlisp FILE=${LISP_FILE} DEBUG=debug`
* FILE: optional parameter to set the lisp file. test_file.lisp by default
* DEBUG: optional parameter that enables debug mode when set to "debug". In debug mode every top-level expression is printed.

Execute the project repl using:
`make jlisp_repl`

Important Maven lifecycles:
`mvn package` -> produce jar 
`mvn test` -> execute automated tests


Dependencies: (also listed in `pom.xml`)
* maven-shade-plugin: 3.2.4
* test dependencies 
  * maven-surefire-plugin: 3.0.0-M5
  * junit-jupiter-engine: 5.8.2
  * juinit-jupiter-api: 5.8.2

Java version: open-jdk17.0.1

While this project was tested with those dependency verisions, it would likely function properly with earlier versions. 

## Testing 
All automated tests can be executed using `mvn test`

Test code can be found at `src/test/java/jlisp`
The Lisp files used in automated testing can be found at `src/test/resources`.
In that directory there are subdirectories for every test containing:
* `input.lisp`
  * The file that is executed 
* `expected_output.lisp`
  * The expected standard output from executing the code


Most tests just verify that the given operator works as intended.
The following test cases are of more interest and use the language holistically:
* Recursion
* InsertionSort
* 0-1Knapsack


Note that all test code is executed in debug mode which means that the result of each top-level expression is printed to standard out.

Ex:
input.lisp:
```lisp
(define foo () 
    (+ 1 2)    
)
(+ 1 2)
```
expected_output.lisp
```lisp
foo
3
```

## Error reporting 
This interpreter has basic error reporting consisting of a brief message on what went wrong and the line on which it occured. 
If the error occurs within a function call, the call stack will also be printed.


## Lanaguage Extensions
The following operations have been added to the language beyond those requested:
* (list expr1 ... exprN)
  * returns a list of the given arguments 
* (quote token1 ... tokenN)
  * returns the unevaluated result of the given arguments 
* (eval expr)
  * evalutes the expression


## Limitations 
* This intepreter is quite complex. 
  * There are far more lines of code in this project than would be required to implement Lisp.
* This intepreter is quite slow.
  * Fairly simple actions such as the last operation in the 0-1Knapsack test take noticable time.
* Error reporting 
  * There is some error reporting, but at times it can be misleading.

## Citations
In-code citations are included for all directly copied code. Two sources were heavily inffluential to the development of this project:
* [Crafting Intepreters](https://craftinginterpreters.com/) by Robert Nystrom 
* [(How to Write a (Lisp) Interpreter (in Python))](https://norvig.com/lispy.html) by Peter Norvig 