package jlisp;


import java.util.List;

//The ideas for expressions types borrowed from: http://norvig.com/lispy.html

abstract class Expr {
    interface Visitor<R> {
        R visitVariableExpr(Variable expr);
        R visitLiteralExpr(Literal expr);
        R visitConditionalExpr(Conditional expr);
        R visitFunctionDefinitionExpr(FunctionDefinition expr);
        R visitVariableDefinitionExpr(VariableDefinition expr);
        R visitProcedureExpr(Procedure expr);
        R visitWhileExpr(While expr);
        R visitQuoteExpr(Quote expr);
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
        final Token keyword;
        final Expr condition;
        final Expr ifTrue;
        final Expr ifFalse;
        Conditional(Token keyword, Expr condition, Expr ifTrue, Expr ifFalse){
            this.keyword = keyword;
            this.condition = condition;
            this.ifTrue = ifTrue;
            this.ifFalse = ifFalse;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
          return visitor.visitConditionalExpr(this);
        }
    }

    static class FunctionDefinition extends Expr{
      final Token name;
      final List<Token> parameters;
      final Expr body;
      FunctionDefinition(Token name, List<Token> parameters, Expr body){   
          this.name = name;
          this.parameters = parameters;
          this.body = body;
      }

      @Override
      <R> R accept(Visitor<R> visitor) {
        return visitor.visitFunctionDefinitionExpr(this);
      }
    }
    

    static class VariableDefinition extends Expr{
        final Token name;
        final Expr value;
        VariableDefinition(Token name, Expr value){   
            this.name = name;
            this.value = value;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
          return visitor.visitVariableDefinitionExpr(this);
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
        Token keyword;
        Expr condition;
        Expr body;
        While(Token keyword, Expr condition, Expr body){
            this.keyword = keyword;
            this.condition = condition;
            this.body = body;
        }
        @Override
        <R> R accept(Visitor<R> visitor) {
          return visitor.visitWhileExpr(this);
        }
    }
    static class Quote extends Expr{
      Token keyword;
      List<Token> inner;
      Quote(Token keyword, List<Token> inner){
          this.keyword = keyword;
          this.inner = inner;
      }
      @Override
      <R> R accept(Visitor<R> visitor) {
        return visitor.visitQuoteExpr(this);
      }
  }
    
}
