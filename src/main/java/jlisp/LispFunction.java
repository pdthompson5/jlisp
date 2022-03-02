package jlisp;

import java.util.List;

public class LispFunction {
    private final Expr.FunctionDefinition definition;
    private final Environment global;

    LispFunction(Expr.FunctionDefinition definition, Environment global){
        this.definition = definition;
        this.global = global;
    }

    // public Object call(Interpreter interpreter, List<Object> arguments){
    //     //Add params to new env 
    //     Environment environment = new Environment(global);
    //     for (int i = 0; i < definition.parameters.size(); i++){
    //         environment.define(definition.parameters.get(i).lexeme, arguments.get(i));
    //     }

    //     //Return execution 
    //     return Interpreter.evaluateBody(this.definition.body, global);
    // }

    public int arity(){
        return definition.parameters.size();
    }
}
