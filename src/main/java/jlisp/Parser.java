package jlisp;
//We are going to end up with a list of expressions rather than just 1
//If expressions are all grouped by parens then there will only be one at the highest level 
//What can we have at the highest level? Definitiley not everything 
//Maybe just atoms and lists, no function calls 

import java.util.ArrayList;
import java.util.List;

import static jlisp.TokenType.*;

public class Parser {

    private final List<Token> tokens;
    private int current = 0;


    Parser(List<Token> tokens){
        this.tokens = tokens;
    }

    List<Expr> parse() {
        List<Expr> expressions = new ArrayList<>();
        while(!isAtEnd()){
            expressions.add(expression());
        }

        return expressions;
    }

    //I think that the order here doesn't acuatlly matter since evaluation always happens on the low end. Thank you for the parenthesis

    private Expr expression(){
        if(match(LEFT_PAREN)){
            return sExpression();
        }
        else{
            //TODO: Implement the funky non sexpression allowable top level expressions 
            return null;
        }
    }



    private Expr sExpression(){
        Expr cond = functionDefinition();
        consume(RIGHT_PAREN, "Expect ')'");
        return cond;
    }

    //Where can people define variables and such? anywhere? I think so
    private Expr functionDefinition(){
        if(match(DEFINE)){
            Token name = advance();

            //Read in parameter list 
            List<Token> parameters = new ArrayList<>(); 
            while(peek().type != RIGHT_PAREN){
                parameters.add(advance());
            }
            consume(RIGHT_PAREN, "Expect ')' after parameter list");

            Expr body = expression();

            return new Expr.FunctionDefinition(name, parameters, body);
        }
        return variableDefinition();
    }

    private Expr variableDefinition(){
        if(match(SET)){
            Token name = advance();
            Expr value = expression();
            return new Expr.VariableDefinition(name, value);
        }

        return condition();
    }

    private Expr condition(){
        if(match(IF)){
            Expr condition = expression();
            Expr ifTrue = expression();
            Expr ifFalse = expression();
            return new Expr.Conditional(condition, ifTrue, ifFalse);
        }

        return whileLoop();
    }
    
    private Expr whileLoop(){
        if(match(WHILE)){
            Expr condition = expression();
            Expr body = expression();
            return new Expr.While(condition, body);
        }
        return procedure();
    }

    //How do I tell if it is a procedure, variable or literal 
    //procedures can only occur at the start of an sexpression
    private Expr procedure(){
        if(previous().type == LEFT_PAREN){
            Token name = advance();

            //TODO: Figure out what to do with Null
            if(name.type == RIGHT_PAREN){
                return null;
            }

            List<Expr> arguments = new ArrayList<>();
            while(peek().type != RIGHT_PAREN){
                arguments.add(expression());
            }
            return new Expr.Procedure(name, arguments);

        }
        return variable();
    }

    //TODO: Tell the difference between a variable and a literal 
    //Maybe if you can't find the varible in the anv, return the literal version
    //Right now everything is a variable
    private Expr variable(){
        if(peek().type == IDENTIFIER){
            Token name = advance();
            return new Expr.Variable(name);
        }
        return literal();
    }

    private Expr literal(){
        Token lit = advance();
        return new Expr.Literal(lit.literal);
    }   

    private boolean match(TokenType... types) {
        for (TokenType type : types) {
          if (check(type)) {
                advance();
                return true;
            }
        }
    
        return false;
    }

    
  //Attempts to match a type, if not ERROR
    private Token consume(TokenType type, String message) {
        if (check(type)) {
            return advance();
        }
        else {
            error(peek(), message);
            return null; //Unreachable
        }

        
    }
    

    private boolean check(TokenType type) {
        if (isAtEnd()) return false;
            return peek().type == type;
        }

    private Token advance() {
        if (!isAtEnd()) current++;
            return previous();
        }

    private Token previous() {
        return tokens.get(current - 1);
    }

    private Token peek() {
        return tokens.get(current);
    }

    private boolean isAtEnd() {
        return peek().type == EOF;
    }

    private void error(Token token, String message){
        Jlisp.error(message, token.line);
    }   
}
