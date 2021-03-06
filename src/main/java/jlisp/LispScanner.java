package jlisp;

import static jlisp.TokenType.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Scanner for this project. 
 * Input: String containing all code to run
 * Output: List of Tokens 
 * Based on the Scanner class from CraftingInterpreters
 */

public class LispScanner {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private int line = 1;

    LispScanner(String source) {
        this.source = source;
    }

    List<Token> scanTokens() {
        ArrayList<String> splitStrings = splitBySpace();
        for (int i = 0; i < splitStrings.size(); i++) {
            String tokenString = splitStrings.get(i);

            //Split whitespace characters
            if (tokenString.isBlank() && tokenString.length() > 1) {
                for (char c : tokenString.toCharArray()) {
                    scanToken(Character.toString(c));
                }
                continue;
            }

            //Ignore rest of line if comment
            if (tokenString.contains(";")) {
                //Ignore text until new line or eof 
                while (!(tokenString.equals("\n") || i == splitStrings.size()-1)) {
                    i++;
                    tokenString = splitStrings.get(i);
                }
                line++;
                continue;
            }

            scanToken(tokenString);
        }
        addToken(EOF, "end-of-file", null);
        return tokens;
    }


    private static final Map<String, TokenType> keywords;

    //Keywords map
    static {
        keywords = new HashMap<>();
        keywords.put("if",       IF);
        keywords.put("while",    WHILE);
        keywords.put("set",      SET);
        keywords.put("t",        T);
        keywords.put("define",   DEFINE);
        keywords.put("quote",    QUOTE);
    }


    ArrayList<String> splitBySpace() {
        //Most of this function is borrowed from: http://norvig.com/lispy.html
        String sourceWithSpaces = source.replaceAll("\\(", " ( ").replaceAll("\\)", " ) ");
        sourceWithSpaces = sourceWithSpaces.replaceAll("\n", " \n ");
        return new ArrayList<String>(Arrays.asList(sourceWithSpaces.split(" ")));
    }

    void scanToken(String tokenString){
        switch (tokenString) {
            case "": break;
            case "(": addToken(LEFT_PAREN, tokenString, null); break;
            case ")": addToken(RIGHT_PAREN, tokenString, null); break;
            case "t": addToken(T, tokenString, true); break;
            case "\n": line++; break;
            case "\t": break;
            case "\r": break;
    
            default:
                //Normal number literals
                if (isDigit(tokenString.charAt(0))) {
                    addToken(NUMBER, tokenString, Double.parseDouble(tokenString.trim()));
                } 
                //Negative number literals
                else if (tokenString.length() > 1 
                            && tokenString.charAt(0) == '-' 
                            && isDigit(tokenString.charAt(1))) {
                    addToken(NUMBER, tokenString, Double.parseDouble(tokenString.trim()));
                } else {
                    keyword(tokenString);
                }
        }
    }

    private void keyword(String token) {
        //Try to match to a keyword 
        TokenType type = keywords.get(token);
        if (type == null) {
            //Add identifier if no keyword match
            identifier(token);
        } else {
            addToken(type, token, null);
        }
    }

    private void identifier(String token) {
        if (!validIdentifier(token)) Jlisp.error("Invalid Identifier: \"" + token + "\"", line);
        addToken(IDENTIFIER, token.trim(), null);
    }

    //The following helper functions are mostly directly borrowed from CraftingInterpreters

    private boolean validIdentifier(String token) {
        if (keywords.containsKey(token)) return false;
        return true;
    }

    private boolean isDigit(char c){
        return c >= '0' && c <= '9';
    }

    //Add token to tokens list with extracted text 
    private void addToken(TokenType type, String text, Object literal) {
        tokens.add(new Token(type, text, literal, line));
    }
}
