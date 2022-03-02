package jlisp;

import java.util.*;

public class AstPrinter implements Expr.Visitor<String>{
      String print(Expr expr) {
        return expr.accept(this);
      }
      @Override
      public String visitConditionalExpr(Expr.Conditional expr) {
        return parenthesize("if", expr.condition,
                            expr.ifTrue, expr.ifFalse);
      }
    
      @Override
      public String visitFunctionDefinitionExpr(Expr.FunctionDefinition expr) {
        StringBuilder builder = new StringBuilder();
    
        builder.append("(").append(expr.name.lexeme).append(" (");


        for (Token token : expr.parameters) {
          builder.append(" ");
          //recursive call to parenthesize all subexpressions
          builder.append(token.lexeme);
        }

        builder.append(") ").append(expr.body.accept(this)).append(")");


    
        return builder.toString();
      }
    
      @Override
      public String visitLiteralExpr(Expr.Literal expr) {
        if (expr.value == null) return "nil";
        return expr.value.toString();
      }
    
      //This only displays the first argument
      //TODO: Fix these so they will print with any number of arguments
      @Override
      public String visitProcedureExpr(Expr.Procedure expr) {
        return parenthesize(expr.name.lexeme, expr.arguments);
      }
    
      @Override
      public String visitVariableExpr(Expr.Variable expr){
        return expr.name.lexeme;
      }

      @Override
      public String visitVariableDefinitionExpr(Expr.VariableDefinition expr){
        return parenthesize("set" + expr.name, expr.value);
      }

      @Override
      public String visitWhileExpr(Expr.While expr){
        return parenthesize("while", expr.body);
      }


    
      //All this does is put parenes around the subexpressions 
      private String parenthesize(String name, Expr... exprs) {
        StringBuilder builder = new StringBuilder();
    
        builder.append("(").append(name);
        for (Expr expr : exprs) {
          builder.append(" ");
          //recursive call to parenthesize all subexpressions
          builder.append(expr.accept(this));
        }
        builder.append(")");
    
        return builder.toString();
      }

      // //All this does is put parenes around the subexpressions 
      private String parenthesize(String name, List<Expr> exprs) {
        StringBuilder builder = new StringBuilder();
    
        builder.append("(").append(name);
        for (Expr expr : exprs) {
          builder.append(" ");
          //recursive call to parenthesize all subexpressions
          builder.append(expr.accept(this));
        }
        builder.append(")");
    
        return builder.toString();
      }
    

    
}
