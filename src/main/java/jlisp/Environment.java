package jlisp;


import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

 
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
                if(Double.compare((Double)arguments.get(0), (Double)arguments.get(1)) > 0){
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
                if(Double.compare((Double)arguments.get(0), (Double)arguments.get(1)) < 0){
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
                if((arguments.get(0) instanceof List) || (arguments.get(1) instanceof List) || 
                    arguments.get(0) instanceof String || arguments.get(1) instanceof String){
                    return null;
                }
                if(Double.compare((Double)arguments.get(0), (Double)arguments.get(1)) == 0 ){
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
        standardEnv.put("begin", new LispCallable() {
            @Override
            public int arity() {
                return -1; //unlimited parameters
            }

            @Override
            public Object call(Interpreter interpreter, List<Object> arguments, int line) {
                return arguments.get(arguments.size()-1);
            }
        });
        standardEnv.put("cons", new LispCallable() {
            @Override
            public int arity() {
                return 2; 
            }
            //TODO: Implement deep copies of arrays 
            @Override
            public Object call(Interpreter interpreter, List<Object> arguments, int line) {
                if(arguments.get(1) == null){
                    List<Object> l = new ArrayList<Object>();
                    l.add(arguments.get(0));
                    return l;
                }
                Object arg1 = arguments.get(1);
                if(arg1 instanceof String){
                    arg1 = interpreter.toList((String)arg1);
                }
                if(arg1 instanceof List){
                    List<Object> l = (List<Object>)arg1;
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
                Object arg = arguments.get(0);
                if(arguments.get(0) instanceof String){
                    arg = interpreter.toList((String)arg);
                }
                if(arguments.get(0) instanceof ConsCell){
                    return ((ConsCell)arguments.get(0)).car;
                }   
                else{
                    List<Object> l = (List<Object>)arg;
                    if(l == null) throw new ClassCastException(); //Empty list is an invalid argument
                    return ((List<Object>)arg).get(0);
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
                Object arg = arguments.get(0);
                if(arguments.get(0) instanceof String){
                    arg = interpreter.toList((String)arg);
                }
                if(arguments.get(0) instanceof ConsCell){
                    return ((ConsCell)arguments.get(0)).cdr;
                }   
                else{
                    List<Object> l = (List<Object>)arg;
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
        standardEnv.put("symbol?", new LispCallable() {
            @Override
            public int arity() {
                return 1; 
            }

            @Override
            public Object call(Interpreter interpreter, List<Object> arguments, int line){
                if(arguments.get(0) instanceof String){
                    //if true not a symbol, it is an unevaluated expression/list produced by quote
                    if(((String)arguments.get(0)).contains("(")){
                        return null;
                    }
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
                else if (arguments.get(0) instanceof String && ((String)arguments.get(0)).contains("(")){
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

        standardEnv.put("eval", new LispCallable() {
            @Override
            public int arity() {
                return 1; 
            }

            @Override
            public Object call(Interpreter interpreter, List<Object> arguments, int line){
                if(arguments.get(0) instanceof String){
                    LispScanner scanner = new LispScanner((String)arguments.get(0));
                    Parser parser = new Parser(scanner.scanTokens());
                    //There should only ever be one parsed top level expression
                    return interpreter.evaluate(parser.parse().get(0));
                }
                return arguments.get(0);
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