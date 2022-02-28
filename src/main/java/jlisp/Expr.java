package jlisp;


//Possible list expressions: Variable expression, literal expression, conditional expression, definition, call
//All expressions of surounded by () forthe most part?

//Between each perens there is a list of tokens 
//if the length of the list is zero, return null (maybe null expression?)
import java.util.List;

abstract class Expr {
    interface Visitor<R> {
        R visitVariableExpr(Variable expr);
        R visitLiteralExpr(Literal expr);
        R visitConditionalExpr(Conditional expr);
        R visitDefinitionExpr(Definition expr);
        R visitProcedureExpr(Procedure expr);
        R visitWhileExpr(While expr);
      }
    abstract <R> R accept(Visitor<R> visitor);
   
    static class Variable extends Expr{
        final Token name;
        Variable(Token name){
            this.name = name;
        }
        @Override
        <R> R accept(Visitor<R> visitor) {
          return visitor.visitVariableExpr(this);
        }
    }

    static class Literal extends Expr{
        final Object value; 
        Literal(Object value){
            this.value = value;
        }
        @Override
        <R> R accept(Visitor<R> visitor) {
          return visitor.visitLiteralExpr(this);
        }
    }

    static class Conditional extends Expr{
        final Expr condition;
        final Expr ifTrue;
        final Expr ifFalse;
        Conditional(Expr condition, Expr ifTrue, Expr ifFalse){
            this.condition = condition;
            this.ifTrue = ifTrue;
            this.ifFalse = ifFalse;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
          return visitor.visitConditionalExpr(this);
        }
    }

    static class Definition extends Expr{
        final Token name;
        final Expr value;
        Definition(Token name, Expr value){   
            this.name = name;
            this.value = value;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
          return visitor.visitDefinitionExpr(this);
        }


    }

    static class Procedure extends Expr{
        final Token name;
        final List<Expr> arguments;
        Procedure(Token name, List<Expr> arguments){
            this.name = name;
            this.arguments = arguments;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
          return visitor.visitProcedureExpr(this);
        }
    }

    static class While extends Expr{
        Expr test;
        Expr body;
        While(Expr test, Expr body){
            this.test = test;
            this.body = body;
        }
        @Override
        <R> R accept(Visitor<R> visitor) {
          return visitor.visitWhileExpr(this);
        }
    }
    
}
