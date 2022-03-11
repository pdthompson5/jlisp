package jlisp;

import java.util.List;

/**
 * Classes that implement this interface can be called in a procedure expression.
 * Structure influenced by LoxCallable in CraftingInterpreters
 */

interface LispCallable {
    int arity();
    
    Object call(Interpreter interpreter, List<Object> arguments);
}