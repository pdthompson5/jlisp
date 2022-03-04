// package jlisp;

// import java.util.List;

// import static jlisp.TokenType.*;
// //Current issue: I'm getting a list of tokens when I'm expecting a literal
// //Symbols are a list of tokens always. If i use quote on a number or a list you just get that number or list, not a symbol
// public class Symbol {
//     //It returns an unevaluated expression or a list 
//     //How do i tell the difference? If the first token is a number?
//     //I would need a quote list expressions :(
//     List<Token> tokens;

//     Symbol(List<Token> tokens){
//         this.tokens = tokens;
//     }

//     public Object eval(Interpreter interpreter){
//         Parser parser = new Parser(tokens);
//         Expr expr = parser.parse().get(0);
//         return interpreter.evaluate(expr);
//     }

//     public Object symbolize(){
//         if(tokens.get(0).type == NUMBER || tokens.get(0).type == T) return tokens.get(0).literal;

//         List<Object> list = list();
//         if(list == null){

//         }
//     }

//     private List<Object> list(){
//         //This list can contain symbols
//         //How do I tell the difference between a list and an s-expression?
//         //If there is a perenthesis treat it as a list. If a token is a literal then evalute it. If it is not then create another symbol?
//         //I will need to convert the literals back to tokens if they are to be evaluated 
//         int current = 0;

//     }


//     @Override
//     public String toString() {
//         String str = "";
//         for (Token t: tokens){
//             str += t.lexeme;
//             str += " ";
//         }
//         return str;
//     }




//     //if it is a literal or a list it is just that 
// }
