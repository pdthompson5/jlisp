package jlisp;


enum TokenType {
    //Basic one characters 
    LEFT_PAREN, RIGHT_PAREN, 
    
    //literals
    NUMBER, IDENTIFIER,
    
    //keywords
    IF, WHILE, SET, T, DEFINE,


    EOF
}
