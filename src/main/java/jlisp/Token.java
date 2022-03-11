package jlisp;

/**
 * This class represents a basic token.
 * Based on the Token class from CraftingInterpreters.
 */
public class Token {
    final TokenType type;
    final String lexeme;

    final Object literal;
    final int line; 

    Token(TokenType type, String lexeme, Object literal, int line) {
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.line = line;
    }

    public String toString() {
        return lexeme;
    }
}
