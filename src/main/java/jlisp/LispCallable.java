package jlisp;


import java.util.List;
interface LispCallable {
    int arity();
    Object call(Interpreter interpreter, List<Object> arguments, int line);
}