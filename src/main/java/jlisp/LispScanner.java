package jlisp;


import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import static jlisp.TokenType.*;

//Whitespace is ocasionally important to us as spaces are deliminators in s-expressions 
//Can I take care of this issue during Scanning? I think so
public class LispScanner {
  private final String source;
  private final List<Token> tokens = new ArrayList<>();
  private int line=1;

  LispScanner(String source){
    this.source = source;
  }

  List<Token> scanTokens(){
    ArrayList<String> splitStrings = splitBySpace();
    for(String tokenString : splitStrings){
      scanToken(tokenString);
    }
    addToken(EOF);
    return tokens;
  }


  private static final Map<String, TokenType> keywords;

  //Keywords map
  static {
    keywords = new HashMap<>();
    keywords.put("if",       IF);
    keywords.put("while",    WHILE);
    keywords.put("set",      SET);
    keywords.put("begin",    BEGIN);
    keywords.put("cons",     CONS);
    keywords.put("car",      CAR);
    keywords.put("cdr",      CDR);
    keywords.put("number?",  NUMBER_QUEST);
    keywords.put("sysmbol?", SYSMBOL_QUEST);
    keywords.put("list?",    LIST_QUEST);
    keywords.put("null?",    NULL_QUEST);
    keywords.put("print",    PRINT);
    keywords.put("t",        T);
    keywords.put("define",   DEFINE);
  }


  ArrayList<String> splitBySpace(){
    String sourceWithSpaces = source.replaceAll("\\(", " ( ").replaceAll("\\)", " ) ");
    return new ArrayList<String>(Arrays.asList(sourceWithSpaces.split(" ")));
  }

  void scanToken(String tokenString){
    switch (tokenString) {
      case "": break;
      case "(": addToken(LEFT_PAREN); break;
      case ")": addToken(RIGHT_PAREN); break;
      case "-": addToken(MINUS); break;   
      case "+": addToken(PLUS); break;   
      case "*": addToken(STAR); break;   
      case "/": addToken(SLASH); break;   
      case "=": addToken(EQUAL); break;   
      case ">": addToken(GREATER_THAN); break;   
      case "<": addToken(LESS_THAN); break;
      case "\n": line++; break;
    
      default:
      if(isDigit(tokenString.charAt(0))){
        addToken(NUMBER, tokenString, Double.parseDouble(tokenString));
      }
      else{
        keyword(tokenString);
      }
        
    }
  }


  private void keyword(String token){
    //Try to match to a keyword 
    TokenType type = keywords.get(token);
    if (type == null){
      //Add identifier if no keyword match
      identifier(token);
    } else{
      addToken(type);
    }
  }


  private void identifier(String token){
    if(!validIdentifier(token)) Jlisp.error("Invalid Identifier: \"" + token + "\"", line);
    addToken(IDENTIFIER, token, null);
  }

  private boolean validIdentifier(String token){
    for(char c : token.toCharArray()){
      if(!isAlphaNumeric(c)){
        return false;
      }
    }
    return true;
  }

  private boolean isDigit(char c){
    return c >= '0' && c <= '9';
  }

  private boolean isAlpha(char c) {
    return (c >= 'a' && c <= 'z') ||
           (c >= 'A' && c <= 'Z') ||
            c == '_';
  }

  private boolean isAlphaNumeric(char c) {
    return isAlpha(c) || isDigit(c);
  }

  //Add token if literal is null
  private void addToken(TokenType type) {
      addToken(type, "", null);
  }


  //Add token to tokens with extracted text 
  private void addToken(TokenType type, String text, Object literal) {
    tokens.add(new Token(type, text, literal, line));
  }


}
