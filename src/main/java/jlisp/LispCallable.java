package jlisp;


import java.util.List;

interface LispCallable {
    boolean paramLimit = true;
    int arity();
    Object call(Interpreter interpreter, List<Object> arguments);
}