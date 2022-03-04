package jlisp;


import java.util.List;
import java.util.ArrayList;

//While loops, conditionals and functions do not eval all of their sub expressions
public class Interpreter implements Expr.Visitor<Object>{
    Environment globals = new Environment();

    Object evaluate(Expr expr) {
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
            values.add(evaluate(argument));
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
            throw new RuntimeError(expr.name, "Invalid argument type for function " + expr.name.lexeme);
        } 
    }
    
    @Override
    public Object visitVariableExpr(Expr.Variable expr){
        return globals.getVar(expr.name);
    }

    @Override
    public Object visitVariableDefinitionExpr(Expr.VariableDefinition expr){
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
    
}
