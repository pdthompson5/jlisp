package jlisp;

//because lisp is so simple, most of the builtins can be defined and added to the gloabal evn

import java.util.Map;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;



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
                return 2; 
            }

            @Override
            public Object call(Interpreter interpreter, List<Object> arguments, int line) {
                if(arguments.get(0) instanceof List || arguments.get(1) instanceof List){
                    return null;
                }
                double epsilon = 0.000001d;
                if(Math.abs((Double)arguments.get(0) - (Double)arguments.get(1)) < epsilon){
                    return true;
                } 
                else{
                    return null;
                }
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
        standardEnv.put("cons", new LispCallable() {
            @Override
            public int arity() {
                return 2; 
            }

            @Override
            public Object call(Interpreter interpreter, List<Object> arguments, int line) {
                if(arguments.get(1) == null){
                    List<Object> l = new ArrayList<Object>();
                    l.add(arguments.get(0));
                    return l;
                }
                if(arguments.get(1) instanceof List){
                    List<Object> l = (ArrayList<Object>)arguments.get(1);
                    l.add(0, arguments.get(0));
                    return l;
                }
                return new ConsCell(arguments.get(0), arguments.get(1));
            }
        });
        standardEnv.put("car", new LispCallable() {
            @Override
            public int arity() {
                return 1; 
            }

            @Override
            public Object call(Interpreter interpreter, List<Object> arguments, int line) {
                if(arguments.get(0) instanceof ConsCell){
                    return ((ConsCell)arguments.get(0)).car;
                }   
                else{
                    List<Object> l = (List<Object>)arguments.get(0);
                    if(l == null) throw new ClassCastException(); //Empty list is an invalid argument
                    return ((List<Object>)arguments.get(0)).get(0);
                }
            }
        });
        standardEnv.put("cdr", new LispCallable() {
            @Override
            public int arity() {
                return 1; 
            }

            @Override
            public Object call(Interpreter interpreter, List<Object> arguments, int line){
                if(arguments.get(0) instanceof ConsCell){
                    return ((ConsCell)arguments.get(0)).cdr;
                }   
                else{
                    List<Object> l = (List<Object>)arguments.get(0);
                    if(l == null) throw new ClassCastException(); //Empty list is an invalid argument
                    if(l.size() == 1) return null;
                    
                    return (l.subList(1, l.size()));
                }
            }
        });

        standardEnv.put("number?", new LispCallable() {
            @Override
            public int arity() {
                return 1; 
            }

            @Override
            public Object call(Interpreter interpreter, List<Object> arguments, int line){
                if(arguments.get(0) instanceof Double){
                    return true;
                }
                else{
                    return null;
                }
            }
        });
        //TODO: Implement
        standardEnv.put("symbol?", new LispCallable() {
            @Override
            public int arity() {
                return 1; 
            }

            @Override
            public Object call(Interpreter interpreter, List<Object> arguments, int line){
                if(arguments.get(0) instanceof Double){
                    return true;
                }
                else{
                    return null;
                }
            }
        });

        standardEnv.put("list?", new LispCallable() {
            @Override
            public int arity() {
                return 1; 
            }

            @Override
            public Object call(Interpreter interpreter, List<Object> arguments, int line){
                if(arguments.get(0) instanceof List){
                    return true;
                }
                else{
                    return null;
                }
            }
        });

        standardEnv.put("null?", new LispCallable() {
            @Override
            public int arity() {
                return 1; 
            }

            @Override
            public Object call(Interpreter interpreter, List<Object> arguments, int line){
                if(arguments.get(0) == null){
                    return true;
                }
                else{
                    return null;
                }
            }
        });

        standardEnv.put("print", new LispCallable() {
            @Override
            public int arity() {
                return 1; 
            }

            @Override
            public Object call(Interpreter interpreter, List<Object> arguments, int line){
                String str = Interpreter.stringify(arguments.get(0));
                System.out.println(str);
                return str;
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