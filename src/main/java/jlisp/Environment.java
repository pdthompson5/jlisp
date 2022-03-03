package jlisp;

//because lisp is so simple, most of the builtins can be defined and added to the gloabal evn

import java.util.Map;

import java.util.HashMap;
import java.util.List;
import java.lang.*;



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

            @Override
            public Object call(Interpreter interpreter, List<Object> arguments, int line) {
                return (Double)arguments.get(0) + (Double)arguments.get(1);
            }
        });

        standardEnv.put("-", new LispCallable() {
            @Override
            public int arity() {
                return 2;
            }

            @Override
            public Object call(Interpreter interpreter, List<Object> arguments, int line) {
                return (Double)arguments.get(0) - (Double)arguments.get(1);
            }
        });

        standardEnv.put("/", new LispCallable() {
            @Override
            public int arity() {
                return 2;
            }

            //TODO: Do I need int line here?
            @Override
            public Object call(Interpreter interpreter, List<Object> arguments, int line) {
                if((Double) arguments.get(1) == 0){
                    
                    Jlisp.error("Divided by zero error", line);
                }
                return (Double)arguments.get(0) / (Double)arguments.get(1);
            }


        });
        standardEnv.put("*", new LispCallable() {
            @Override
            public int arity() {
                return 2;
            }

            @Override
            public Object call(Interpreter interpreter, List<Object> arguments, int line) {
                return (Double)arguments.get(0) * (Double)arguments.get(1);
            }


        });
        standardEnv.put(">", new LispCallable() {
            @Override
            public int arity() {
                return 2; 
            }

            @Override
            public Object call(Interpreter interpreter, List<Object> arguments, int line) {
                if((Double)arguments.get(0) > (Double)arguments.get(1)){
                    return true;
                } else{
                    return null;
                }
            }
        });
        standardEnv.put("<", new LispCallable() {
            @Override
            public int arity() {
                return 2; 
            }

            @Override
            public Object call(Interpreter interpreter, List<Object> arguments, int line) {
                if((Double)arguments.get(0) < (Double)arguments.get(1)){
                    return true;
                } else{
                    return null;
                }
            }
        });
        standardEnv.put("=", new LispCallable() {
            @Override
            public int arity() {
                return -1; //unlimited parameters
            }

            @Override
            public Object call(Interpreter interpreter, List<Object> arguments, int line) {
                return arguments;
            }
        });
        standardEnv.put("list", new LispCallable() {
            @Override
            public int arity() {
                return -1; //unlimited parameters
            }

            @Override
            public Object call(Interpreter interpreter, List<Object> arguments, int line) {
                return arguments;
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

    void printEnv(){
        System.out.println(varEnv.toString());
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
