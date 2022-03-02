package jlisp;

//because lisp is so simple, most of the builtins can be defined and added to the gloabal evn

import java.util.Map;
import java.util.HashMap;

//Standard env 

    //Begin -> empty function with endless params -> Expr... arguments;

public class Environment {
    final Environment enclosing;

    //This interpretation has different namespaces for Variables and Functions so it is a Lisp-2
    private Map<String, Object> varEnv = new HashMap<>();
    private Map<String, LispFunction> funcEnv = new HashMap<>();
    

    Environment(){
        enclosing = null;
    }

    Environment(Environment enclosing){
        this.enclosing = enclosing;
    }

    void defineVar(String name, Object value){
        varEnv.put(name, value);
    }

    void defineFunc(String name, LispFunction func){
        funcEnv.put(name, func);
    }

    Object getVar(Token name){
        //Look-up in current env 
        if(varEnv.containsKey(name.lexeme)) {
            return varEnv.get(name.lexeme);
        }

        //Look in enclosing env
        if (enclosing != null) return enclosing.getVar(name);

        Jlisp.error("Undefined variable '" + name.lexeme + "'", name.line);
        return null; //unreachable
    }

    LispFunction getFunc(Token name){
        //Look-up in current env 
        if(funcEnv.containsKey(name.lexeme)) {
            return funcEnv.get(name.lexeme);
        }

        //Look in enclosing env
        if (enclosing != null) return enclosing.getFunc(name);

        Jlisp.error("Undefined function '" + name.lexeme + "'", name.line);
        return null; //unreachable
    }
}
