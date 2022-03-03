package jlisp;


import java.util.List;

interface LispCallable {
    //TODO: Figure out how to check parameter types
    boolean paramLimit = true;
    int arity();
    Object call(Interpreter interpreter, List<Object> arguments, int line);
}