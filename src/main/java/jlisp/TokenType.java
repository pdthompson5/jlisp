package jlisp;


enum TokenType {
    //Basic one characters 
    LEFT_PAREN, RIGHT_PAREN, MINUS, PLUS, STAR, SLASH, EQUAL, GREATER_THAN, LESS_THAN, 
    
    //literals
    NUMBER, IDENTIFIER,
    
    //keywords
    IF, WHILE, SET, BEGIN, CONS, CAR, CDR, NUMBER_QUEST, SYSMBOL_QUEST, LIST_QUEST, NULL_QUEST, PRINT, T, DEFINE,

    NULL,


    EOF
}
