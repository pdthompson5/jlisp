package jlisp;

import static jlisp.TokenType.*;

import java.util.ArrayList;
import java.util.List;

public class Parser {

    private final List<Token> tokens;
    private int current = 0;
    private boolean noMatch = false;


    Parser(List<Token> tokens){
        this.tokens = tokens;
    }

    List<Expr> parse() {
        List<Expr> expressions = new ArrayList<>();
        while(!isAtEnd()){
            try{
                expressions.add(expression());
            }
            catch(OutOfMemoryError e){
                //This handles the case of accidentally using c-style function calls i.e.: (foo (1 2 3))
                Jlisp.error("Expected Expression", peek().line);
            }
            
        }

        return expressions;
    }

    private Expr expression(){
        if(peek().type == LEFT_PAREN){
            return sExpression();
        }
        return variable();
    }



    private Expr sExpression(){
        if(match(LEFT_PAREN)){
            Expr cond = functionDefinition();

            //If no other matches it must be an S expression or not an expression
            if(cond == null && peek().type == LEFT_PAREN && noMatch){
                cond = sExpression();
            }
            else if(noMatch){
                Jlisp.error("Expect expression", peek().line);
            }
            

            consume(RIGHT_PAREN, "Expect ')'");
            return cond;
        }

        return functionDefinition();
    }

    //Where can people define variables and such? anywhere? I think so
    private Expr functionDefinition(){
        
        if(match(DEFINE)){
            Token name = advance();
            //Read in parameter list 
            consume(LEFT_PAREN, "Expect '(' after function name");
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
            return new Expr.Conditional(previous(), condition, ifTrue, ifFalse);
        }

        return whileLoop();
    }
    
    private Expr whileLoop(){
        if(match(WHILE)){
            Expr condition = expression();
            Expr body = expression();
            return new Expr.While(previous(), condition, body);
        }
        return quote();
    }

    private Expr quote(){
        if(match(QUOTE)){
            Token keyword = previous();
            List<Token> list = new ArrayList<>();
            //Add sequence of tokens
            if(peek().type == LEFT_PAREN){
                list.add(advance());
                
                int rightParensNeeded = 1;
                while(rightParensNeeded > 0){
                    if(peek().type == RIGHT_PAREN){
                        rightParensNeeded--;
                    }
                    if(peek().type == LEFT_PAREN){
                        rightParensNeeded++;
                    }
                    list.add(advance());
                }
            }
            else{
                //Add single token
                list.add(advance());
            }
           
            
            return new Expr.Quote(keyword, list);
        }
        return procedure();
    }


    private Expr procedure(){
        //Procedures can only occur at the beginning of an s expression
        //Ignore number in the case of a quote list: (quote (1 2 3))
        if(previous().type == LEFT_PAREN && peek().type != NUMBER){
            if(peek().type == RIGHT_PAREN){
                return new Expr.Literal(null);
            }

            Token name = advance();

            List<Expr> arguments = new ArrayList<>();
            while(peek().type != RIGHT_PAREN){
                arguments.add(expression());
            }
            return new Expr.Procedure(name, arguments);

        }
        return variable();
    }

    
    
    private Expr variable(){
        //Variables will always be identifiers
        if(match(IDENTIFIER)){
            Token name = previous();
            return new Expr.Variable(name);
        }
        return literal();
    }

    //Only supported literals are number and t
    private Expr literal(){
        if(match(NUMBER, T)){
            Token lit = previous();
            return new Expr.Literal(lit.literal);
        }
        noMatch = true;
        return null;
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
