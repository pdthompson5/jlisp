package jlisp;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static jlisp.TokenType.*;

/**
 * Interpreter for this project. 
 * Input: Expression
 * Output: Object that is the evaluation of the expresssion
 * Based on the Interpreter class from CraftingInterpreters
 * Utilizes the visitor design pattern for expression evaluation
 */

public class Interpreter implements Expr.Visitor<Object> {
    Environment globals = new Environment();
    Stack<String> callStack = new Stack<String>();


    Object evaluate(Expr expr) {
        try {
            return expr.accept(this);
        } catch (RuntimeError error) {
            String message = error.getMessage();
            if (!callStack.isEmpty()) {
                message += "\n";
            }
            //Add contents of call stack to error message
            while (!callStack.isEmpty()) {
                message += callStack.pop() + "\n";
            }
            Jlisp.error(message, error.token.line);
        }
        return null; //unreachable
    }

    @Override
    public Object visitConditionalExpr(Expr.Conditional expr) {
        Object condition = evaluate(expr.condition);

        if (isTruthy(condition, expr.keyword)) {
            return evaluate(expr.ifTrue);
        } else {
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
        callStack.push("   at " + expr.name.lexeme + " (Line: " + expr.name.line + ")");

        //Evaluate arguments 
        List<Object> values = new ArrayList<>();
        for (Expr argument : expr.arguments) {
            Object value = evaluate(argument);

            //if argument is a list, deep copy that list 
            if (value instanceof List) {
                value = deepCopyList((List<Object>) value);
            }

            values.add(value);
        }

        //Fetch the function
        LispCallable func = globals.getFunc(expr.name);

        //Test the arity if the function has limited parameters 
        if (expr.arguments.size() != func.arity() && func.arity() > -1) {
            throw new RuntimeError(expr.name, "Expected " 
                + func.arity() + " arguments for function: " + expr.name.lexeme + " but got " 
                + expr.arguments.size() + ".");
        }

        try {
            //Return the result of the function
            Object value = func.call(this, values);
            callStack.pop();
            return value;
        } catch (ClassCastException error) { 
            //ClassCastException always refers to incorrect arguments
            //Type checking arguments only occurs in native functions
            String args = "Arguments recieved: ";
            for (Object val : values) {
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
        if (value instanceof List) {
            value = deepCopyList((List<Object>) value);
        }

        Object val = evaluate(expr.value);
     
        boolean updateSucess = globals.updateVar(expr.name, val);
      
        //create new variable 
        if (!updateSucess) {
            globals.defineVar(expr.name.lexeme, val);
        }
        
        
        return expr.name.lexeme;
    }

    @Override
    public Object visitWhileExpr(Expr.While expr) {
        Object condition = evaluate(expr.condition);
        Object val = null;
        while (isTruthy(condition, expr.keyword)) {
            val = evaluate(expr.body);
            condition = evaluate(expr.condition);
        }

        return val;
    }
    /**
     * Convert from String to java list
     * To make quote work, lists can either be an actual java list or a String.
     * I am willing to conceed that there is a better way to implement quote, but as far as I can tell this works.
     */
    public List<Object> toList(String source) {
        LispScanner scanner = new LispScanner(source);
        List<Token> tokens = scanner.scanTokens();


        //Peel parenthesis off the list 
        if (tokens.get(0).type == LEFT_PAREN) {
            tokens.remove(0);
            //elimainate eof and right paren
            tokens.remove(tokens.size() - 1);
            tokens.remove(tokens.size() - 1);
        }

        List<Object> l = new ArrayList<>();
        for (int i = 0; i < tokens.size(); i++) {
            //evaluate null 
            Token t = tokens.get(i);
            if (i < tokens.size()-1 && t.type == LEFT_PAREN && tokens.get(i+1).type == RIGHT_PAREN) {
                l.add(null);
            } 
            //Add nested expression/list to list as a string 
            if (t.type == LEFT_PAREN) {
                String s = "";
                s += "("; i++; //consume left paren
                while (tokens.get(i).type != RIGHT_PAREN) {
                    s += tokens.get(i).toString() + " ";
                    i++;
                }
                s += ")"; //consume right paren
                l.add(s);
            }
            //evaluate number 
            else if (t.type == NUMBER) {
                l.add(t.literal);
            }  
            //evaluate t
            else if (t.type == T) {
                l.add(true);
            } else {
                l.add(t.toString() + " ");
            }
        }
        return l;
    }

    @Override
    public Object visitQuoteExpr(Expr.Quote expr) {
        List<Token> tokens = expr.inner;

        //evaluate null 
        if (tokens.size() > 1 && tokens.get(0).type == LEFT_PAREN && tokens.get(1).type == RIGHT_PAREN) return null;
        //evaluate number 
        if (tokens.get(0).type == NUMBER)  return tokens.get(0).literal; 
        //evaluate t
        if (tokens.get(0).type == T) return true;

        //Save the expression as an unevaluated String
        String s = "";
        for (int i = 0; i < tokens.size(); i++) {
            Token t = tokens.get(i);
            if (i == tokens.size() - 1) {
                s += t.toString();
            } else {
                s += t.toString() + " ";
            }
            
        }
        return s;
    }

    //Helper functions. Heavily influenced by CraftingInterpreters

    private boolean isTruthy(Object val, Token t) {
        if (val == null) {
            return false;
        }
        if (val instanceof Boolean) {
            return (Boolean) val;
        }

        throw new RuntimeError(t, "Invalid conditional" + stringify(val) + ". Valid conditionals: t, ()");
    }


    /**
     * Converts from Object to String.
     * This is effecivley a toString() method that is consistent with Lisp representations.
     */
    public static String stringify(Object object) {
        String convert = "";

        //Our null is nil   
        if (object == null) convert = "()";
        
        if (object instanceof Boolean){
            convert = "t";
        }

        if (object instanceof Double) {
            convert = stringifyDouble((Double) object);
        }

        if (object instanceof List){
            convert = stringifyList((List<Object>) object);
        }

        if (convert.equals("")) {
            convert = object.toString();
        }

        return convert;
    }

    private static String stringifyDouble(Double dbl) {
        String text = dbl.toString();
        //Trim off excessive zeros
        if (text.endsWith(".0")) {
            text = text.substring(0, text.length() - 2);
        }
        return text;
    }

    private static String stringifyList(List<Object> l) {
        String convert = "";
        convert += "(";
        for (int i = 0; i < l.size(); i++) {
            if (!(i == 0)) convert += " ";
            convert += stringify(l.get(i));
        }
        convert += ")";
        return convert;
    }
    
    /**
     * Evaluate the given expression in the given environment. 
     */
    public Object evaluateBody(Expr body, Environment env) {
        //Swap out env for the length of the function execution
        Environment old = globals;
        globals = env;

        //Eval function body 
        Object val = evaluate(body);

        //revert env
        globals = old;
        return val;
    }

    /**
     * This function creates a deep copy of a "Lispy" list.
     * Our lists can only contain cons cells, doubles, strings and lists.
     */
    public static List<Object> deepCopyList(List<Object> list) {
        List<Object> copy = new ArrayList<Object>();
        for (Object i : list) {

            //deep copy sublists
            if (i instanceof List) {
                copy.add(deepCopyList((List<Object>) i));
            } else {
                //Strings, Double and ConsCells are immutable so we can just add them 
                copy.add(i);
            }
        }
        return copy;
    }
}
