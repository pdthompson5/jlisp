package jlisp;

//because lisp is so simple, most of the builtins can be defined and added to the gloabal evn

import java.util.Map;

import java.util.HashMap;
import java.util.List;



    //Begin -> empty function with endless params -> Expr... arguments;
 
public class Environment {
        //Standard env 
    private final static Map<String, LispCallable> standardEnv = new HashMap<>();

    static {
        standardEnv.put("+", new LispCallable() {
            @Override
            public int arity() {
                return 2;
            }
            //TODO: Figure out how to do casting errors
            //I think I can do it with adding required types to LispCallable
            @Override
            public Object call(Interpreter interpreter, List<Object> arguments, int line) {
                return (Double)arguments.get(0) + (Double)arguments.get(1);
            }
        });
    }
    final Environment enclosing;

    //This interpretation has different namespaces for Variables and Functions so it is a Lisp-2
    private Map<String, Object> varEnv = new HashMap<>();
    private Map<String, LispCallable> funcEnv;
    

    Environment(){
        //When the global env is created, add the standardEnv
        funcEnv = new HashMap<>(standardEnv);
        enclosing = null;
    }

    Environment(Environment enclosing){
        this.enclosing = enclosing;
        funcEnv = new HashMap<>();
    }

    void defineVar(String name, Object value){
        varEnv.put(name, value);
    }

    void defineFunc(String name, LispCallable func){
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

    LispCallable getFunc(Token name){
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
