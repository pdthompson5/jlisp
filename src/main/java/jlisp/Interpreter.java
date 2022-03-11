package jlisp;


import java.util.List;
import java.util.ListIterator;

import static jlisp.TokenType.*;

import java.util.ArrayList;

//While loops, conditionals and functions do not eval all of their sub expressions
public class Interpreter implements Expr.Visitor<Object>{
    Environment globals = new Environment();

    Object evaluate(Expr expr) {
        // AstPrinter printer = new AstPrinter();
        // System.out.println("Evaluating Expression:" + printer.print(expr));
        try{
            return expr.accept(this);
        }
        catch(RuntimeError error){
            Jlisp.error(error.getMessage(), error.token.line);
        }
        return null; //unreachable
    }

    @Override
    public Object visitConditionalExpr(Expr.Conditional expr) {
        Object condition = evaluate(expr.condition);

        if(isTruthy(condition, expr.keyword)){
            return evaluate(expr.ifTrue);
        }
        else{
            return evaluate(expr.ifFalse);
        }
    }
    
    @Override
    public Object visitFunctionDefinitionExpr(Expr.FunctionDefinition expr) {
        LispFunction func = new LispFunction(expr, globals);
        globals.defineFunc(expr.name.lexeme, func);
        return expr.name.lexeme;
    }
    
    @Override
    public Object visitLiteralExpr(Expr.Literal expr) {
        return expr.value;
    }
    
    @Override
    public Object visitProcedureExpr(Expr.Procedure expr) {
        //Evaluate arguments 
        List<Object> values = new ArrayList<>();
        for(Expr argument : expr.arguments){
            Object value = evaluate(argument);
            //if argument is a list, deep copy that list 
            if(value instanceof List){
                value = deepCopyList((List<Object>)value);
            }
            values.add(value);
        }

        //Fetch the function
        LispCallable func = globals.getFunc(expr.name);

        //Test the arity if the function has limited parameters 
        if (expr.arguments.size() != func.arity() && func.arity() > -1) {
            throw new RuntimeError(expr.name, "Expected " +
                func.arity() + " arguments for function: " + expr.name.lexeme + " but got " +
                expr.arguments.size() + ".");
          }

        //Return the result of the function
        try{
            return func.call(this, values, expr.name.line);
        } catch (ClassCastException error){
            String args = "Arguments recieved: ";
            for(Object val : values){
                args += stringify(val);
                args += " ";
            }
            throw new RuntimeError(expr.name, "Invalid argument type for function '" + expr.name.lexeme + "'. " + args + "");
        } 
    }
    
    @Override
    public Object visitVariableExpr(Expr.Variable expr){
        return globals.getVar(expr.name);
    }

    @Override
    public Object visitVariableDefinitionExpr(Expr.VariableDefinition expr){
        Object value = evaluate(expr.value);
        //deep copy lists
        if(value instanceof List){
            value = deepCopyList((List<Object>) value);
        }
        globals.defineVar(expr.name.lexeme, evaluate(expr.value));
        return expr.name.lexeme;
    }

    @Override
    public Object visitWhileExpr(Expr.While expr){
        Object condition = evaluate(expr.condition);
        Object val = null;
        while(isTruthy(condition, expr.keyword)){
            val = evaluate(expr.body);
            condition = evaluate(expr.condition);
        }

        return val;
    }

    //To make quote work lists can either be an actual java list or a String. This method converts a String to a java list.
    //I am willing to conceed that there may be a better way to implement quote, but as far as I can tell this works.
    public List<Object> toList(String source){
        LispScanner scanner = new LispScanner(source);
        List<Token> tokens = scanner.scanTokens();


        //Peel parenthesis off the list 
        if(tokens.get(0).type == LEFT_PAREN){
            tokens.remove(0);
            //elimainate eof and right paren
            tokens.remove(tokens.size()-1);
            tokens.remove(tokens.size()-1);
        }

        List<Object> l = new ArrayList<>();
        for(int i = 0; i < tokens.size(); i++){
            //evaluate null 
            Token t = tokens.get(i);
            if(i < tokens.size()-1 && t.type == LEFT_PAREN && tokens.get(i+1).type == RIGHT_PAREN){
                l.add(null);
            } 
            //Add nested expression/list to list as a string 
            if(t.type == LEFT_PAREN){
                String s = "";
                s += "("; i++; //consume left paren
                while(tokens.get(i).type != RIGHT_PAREN){
                    s += tokens.get(i).toString() + " ";
                    i++;
                }
                s += ")"; //consume right paren
                l.add(s);
            }
            //evaluate number 
            else if(t.type == NUMBER){
                l.add(t.literal);
            }  
            //evaluate t
            else if(t.type == T){
                l.add(true);
            } 
            else{
                l.add(t.toString() + " ");
            }
        }
        return l;
    }
    
    // @Override
    // public Object visitQuoteExpr(Expr.Quote expr){
    //     //Quote will either return a literal, a String or a list of literals 
    //     //Still having issues: If it is a list then we will only interpret literals?
    //     //Idea: Store + 1 2 as a list 
    //     //only ever return a string if it is an indentifier?

    //     //if I ever have more than one expression then punt it as a string 

    //     List<Token> tokens = expr.inner;

    //     //Peel parenthesis off of a list of literals so the parser will recognize the expression
    //     if(tokens.get(0).type == LEFT_PAREN && tokens.get(1).type == NUMBER){
    //         tokens.remove(0);
    //         tokens.remove(tokens.size()-1);
    //     }
        
    //     //Add EOF token so parser knows when to stop
    //     tokens.add(new Token(EOF, "end-of-file", null, tokens.get(tokens.size()-1).line));
        

    //     Parser parser = new Parser(tokens);
    //     List<Expr> inner = parser.parse();
    //     AstPrinter printer = new AstPrinter();

    //     //I only want to evaluate the highest expression in the list, everything else will remain strings 


    //     if(inner.size() > 1){
    //         List<Object> l = new ArrayList<>();
            
    //         for(Expr e : inner){
    //             if(e == null){
    //                 l.add(null);
    //             }
    //             //Evaluate literals
    //             else if(e instanceof Expr.Literal){
    //                 l.add(evaluate(e));
    //             }
    //             else{
    //                 l.add(printer.print(e));
    //             }
    //         }

    //         return l;
    //     }
    //     else{   
    //         if(inner.get(0) == null) return null;
    //         if(inner.get(0) instanceof Expr.Literal) return evaluate(inner.get(0)); 
    //         return printer.print(inner.get(0));
    //     }
    // }




    @Override
    public Object visitQuoteExpr(Expr.Quote expr){
        List<Token> tokens = expr.inner;

        //evaluate null 
        if(expr.inner.size() > 1 && expr.inner.get(0).type == LEFT_PAREN && expr.inner.get(1).type == RIGHT_PAREN) return null;
        //evaluate number 
        if(expr.inner.get(0).type == NUMBER)  return expr.inner.get(0).literal; 
        //evaluate t
        if(expr.inner.get(0).type == T) return true;

        //Save the expression as an unevaluated String
        String s = "";
        for(int i = 0; i < tokens.size(); i++){
            Token t = tokens.get(i);
            if(i == tokens.size() - 1){
                s += t.toString();
            }
            else{
                s += t.toString() + " ";
            }
            
        }
        return s;
    }

    //Helper functions 
    private boolean isTruthy(Object val, Token t){
        if(val == null){
            return false;
        }
        if(val instanceof Boolean){
            return (Boolean)val;
        }

        throw new RuntimeError(t, "Invalid conditional" + stringify(val) + ". Valid conditionals: t, ()");
    }

    public static String stringify(Object object) {
        String convert = "";

        //Our null is nil   
        if (object == null) convert = "()";
        
        if (object instanceof Boolean){
            convert = "t";
        }

        //We don't want to print excessive .0s
        if (object instanceof Double) {
            convert = stringifyDouble((Double)object);
        }

        if (object instanceof List){
            convert = stringifyList((List<Object>)object);
        }

        if(convert.equals("")){
            convert = object.toString();
        }

        return convert;
    }

    private static String stringifyDouble(Double dbl){
        String text = dbl.toString();
        if (text.endsWith(".0")) {
            text = text.substring(0, text.length() - 2);
        }
        return text;
    }

    private static String stringifyList(List<Object> l){
        String convert = "";
        convert += "(";
        for(int i = 0; i < l.size(); i++){
            if(!(i==0)) convert+=" ";
            convert += stringify(l.get(i));
        }
        convert += ")";
        return convert;
    }
    

    public Object evaluateBody(Expr body, Environment env){
        //Swap out env for the length of the function execution
        Environment old = globals;
        globals = env;

        //Eval function body 
        Object val = evaluate(body);

        //revert env
        globals = old;
        return val;
    }


    //This function creates a deep copy of a "Lispy" list. Our lists can only contain cons cells, doubles, strings and lists 
    public static List<Object> deepCopyList(List<Object> list){
        List<Object> copy = new ArrayList<Object>();
        for(Object i : list){

            //deep copy sublists
            if(i instanceof List){
                copy.add(deepCopyList((List<Object>)i));
            }
            else{
                //Strings, Double and ConsCells are immutable so we can just add them 
                copy.add(i);
            }
        }
        return copy;
    }



    
}
