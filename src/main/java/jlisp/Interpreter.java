package jlisp;


//While loops, conditionals and functions do not eval all of their sub expressions
public class Interpreter implements Expr.Visitor<Object>{

    Object evaluate (Expr expr) {
        return expr.accept(this);
    }

    @Override
    public Object visitConditionalExpr(Expr.Conditional expr) {
        return null;
    }
    
    @Override
    public Object visitFunctionDefinitionExpr(Expr.FunctionDefinition expr) {
        return null;
    }
    
    @Override
    public Object visitLiteralExpr(Expr.Literal expr) {
        return null;
    }
    
    @Override
    public Object visitProcedureExpr(Expr.Procedure expr) {
        return null;
    }
    
    @Override
    public Object visitVariableExpr(Expr.Variable expr){
        return null;
    }

    @Override
    public Object visitVariableDefinitionExpr(Expr.VariableDefinition expr){
        return null;
    }

    @Override
    public String visitWhileExpr(Expr.While expr){
        return null;
    }
    
}
