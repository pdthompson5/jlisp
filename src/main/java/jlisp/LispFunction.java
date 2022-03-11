package jlisp;

import java.util.List;

/**
 * Function class.
 * A lisp function refers to a user defined function.
 * Structure influenced by LoxFunction in CraftingInterpreters
 */

public class LispFunction implements LispCallable {
    private final Expr.FunctionDefinition definition;
    private final Environment global;

    LispFunction(Expr.FunctionDefinition definition, Environment global){
        this.definition = definition;
        this.global = global;
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        //Create new local environment 
        Environment environment = new Environment(global);

        //Add parameters to local environment
        for (int i = 0; i < definition.parameters.size(); i++) {
            environment.defineVar(definition.parameters.get(i).lexeme, arguments.get(i));
        }
        
        //Evaluate the function body 
        return interpreter.evaluateBody(this.definition.body, environment);
    }

    @Override
    public int arity() {
        return definition.parameters.size();
    }
}
