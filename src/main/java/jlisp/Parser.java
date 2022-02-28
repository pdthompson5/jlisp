package jlisp;
//We are going to end up with a list of expressions rather than just 1
//If expressions are all grouped by parens then there will only be one at the highest level 
//What can we have at the highest level? Definitiley not everything 
//Maybe just atoms and lists, no function calls 

import java.util.ArrayList;
import java.util.List;

public class Parser {

    private final List<Token> tokens;
    private int current = 0;


    Parser(List<Token> tokens){
        this.tokens = tokens;
    }

    List<Expr> parse() {
        List<Expr> expressions = new ArrayList<>();
        while(current < tokens.size()){
            expressions.add(expression());
        }

        

        return expressions;
    }

    //I think that the order here doesn't acuatlly matter since evaluation always happens on the low end. Thank you for the parenthesis

    private Expr expression(){
        return sExpression();
    }

    private Expr sExpression(){

        return condition();
    }

    private Expr condition(){


        return whileLoop();
    }
    
    private Expr whileLoop(){
        return procedure();
    }

    private Expr procedure(){
        
        return variable();
    }

    private Expr variable(){
        return literal();
    }

    private Expr literal(){
        return null;
    }   

    private Token advance() {
        if (current < tokens.size()) current++;
        return previous();
    }

    private Token previous() {
        return tokens.get(current - 1);
    }

    private Token peek() {
        return tokens.get(current);
    }
}
