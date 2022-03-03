package jlisp;

import java.util.List;

public class LispFunction implements LispCallable {
    private final Expr.FunctionDefinition definition;
    private final Environment global;

    LispFunction(Expr.FunctionDefinition definition, Environment global){
        this.definition = definition;
        this.global = global;
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments, int line){
        //Add params to new env 
        Environment environment = new Environment(global);
        for (int i = 0; i < definition.parameters.size(); i++){
            environment.defineVar(definition.parameters.get(i).lexeme, arguments.get(i));
        }
        return interpreter.evaluateBody(this.definition.body, environment);
    }

    

    @Override
    public int arity(){
        return definition.parameters.size();
    }
}
