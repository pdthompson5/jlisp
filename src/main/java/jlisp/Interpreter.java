package jlisp;


import java.util.List;
import java.util.ArrayList;

//While loops, conditionals and functions do not eval all of their sub expressions
public class Interpreter implements Expr.Visitor<Object>{
    final Environment globals = new Environment();

    Object evaluate (Expr expr) {
        return expr.accept(this);
    }

    @Override
    public Object visitConditionalExpr(Expr.Conditional expr) {
        Object condition = evaluate(expr.condition);

        if(isTruthy(condition)){
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

        //Return the result of the function
        return func.call(this, values, expr.name.line);
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
        while(isTruthy(condition)){
            val = evaluate(expr.body);
            condition = evaluate(expr.condition);
        }

        return val;
    }

    public void checkNumberOperand(Token operator, Object operand, int line){
        if (operand instanceof Double) return;
        Jlisp.error("Expect number for operator: " + operator, line);
    }

    private boolean isTruthy(Object val){
        if(val == null){
            return false;
        }
        if(val instanceof Boolean){
            return (Boolean)val;
        }

        //TODO: Implement Runtime errors to fix this
        Jlisp.error("Unexpected conditional value. Conditional values : () or t", -10);
        return false; //unreachable
    }
    
}
