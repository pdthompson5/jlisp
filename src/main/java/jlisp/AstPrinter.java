package jlisp;

import java.util.List;

/**
 * This class is not currently in use but I think it is pretty neat.
 * This class converts epressions into their exact String representation. 
 * The Strings could be used as input for the interpreter. 
 * Based on the AstPrinter class in CraftingIntepreters.
 */
public class AstPrinter implements Expr.Visitor<String> {

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
    
        builder.append("(define ").append(expr.name.lexeme).append(" (");

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
        if (expr.value == null) return "()";
        if (expr.value instanceof Boolean) return "t";
        return expr.value.toString();
    }
    
    @Override
    public String visitProcedureExpr(Expr.Procedure expr) {
        return parenthesize(expr.name.lexeme, expr.arguments);
    }
    
    @Override
    public String visitVariableExpr(Expr.Variable expr) {
        return expr.name.lexeme;
    }

    @Override
    public String visitVariableDefinitionExpr(Expr.VariableDefinition expr) {
        return parenthesize("set " + expr.name, expr.value);
    }

    @Override
    public String visitWhileExpr(Expr.While expr) {
        return parenthesize("while", expr.condition, expr.body);
    }

    @Override
    public String visitQuoteExpr(Expr.Quote expr) { 
        String s = "( quote ";
        for (Token token : expr.inner) {
            s += token.toString();
            s += " ";
        }
        s += ")";
        return s;
    }


    
    //recursively string print subexpressions and add parenthesis
    //This function was borrowed directly from CraftingIntepreters
    private String parenthesize(String name, Expr... exprs) {
        StringBuilder builder = new StringBuilder();
    
        builder.append("(").append(name);
        for (Expr expr : exprs) {
            builder.append(" ");
            builder.append(expr.accept(this));
        }
        builder.append(")");
    
        return builder.toString();
    }

    //Overload for real lists 
    private String parenthesize(String name, List<Expr> exprs) {
        StringBuilder builder = new StringBuilder();
    
        builder.append("(").append(name);
        for (Expr expr : exprs) {
            builder.append(" ");
            builder.append(expr.accept(this));
        }
        builder.append(")");
    
        return builder.toString();
    }
}
