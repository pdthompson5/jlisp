package jlisp;


import java.util.HashMap;
import java.util.Map;


import java.util.List;

import static jlisp.TokenType.*;

import java.util.ArrayList;
import java.util.Arrays;

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
    for(int i = 0; i < splitStrings.size(); i++){
      String tokenString = splitStrings.get(i);

      //Split whitespace characters
      if(tokenString.isBlank() && tokenString.length() > 1){
        for(char c : tokenString.toCharArray()){
          scanToken(Character.toString(c));
        }
        continue;
      }

      //Ignore rest of line if comment
      if(tokenString.contains(";")){
        //Ignore text until new line or eof 
        while(!(tokenString.equals("\n") || i == splitStrings.size()-1)){
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


  ArrayList<String> splitBySpace(){
    String sourceWithSpaces = source.replaceAll("\\(", " ( ").replaceAll("\\)", " ) ");
    sourceWithSpaces = sourceWithSpaces.replaceAll("\n", " \n ");
    return new ArrayList<String>(Arrays.asList(sourceWithSpaces.split(" ")));
  }

  void scanToken(String tokenString){
    switch (tokenString) {
      case "": break;
      case "(": addToken(LEFT_PAREN, tokenString, null); break;
      case ")": addToken(RIGHT_PAREN, tokenString, null); break;
      // case "-": addToken(MINUS); break;   
      // case "+": addToken(PLUS); break;   
      // case "*": addToken(STAR); break;   
      // case "/": addToken(SLASH); break;   
      // case "=": addToken(EQUAL); break;   
      // case ">": addToken(GREATER_THAN); break;   
      // case "<": addToken(LESS_THAN); break;
      case "t": addToken(T, null, true); break;
      case ";": comment(); break; 
      case "\n": line++; break;
      case "\t": break;
      case "\r": break;
    
      default:
      if(isDigit(tokenString.charAt(0))){
        addToken(NUMBER, tokenString, Double.parseDouble(tokenString.trim()));
      }
      else{
        keyword(tokenString);
      }
        
    }
  }

  //Skip rest of the line 
  private void comment(){

  }

  private void keyword(String token){
    //Try to match to a keyword 
    TokenType type = keywords.get(token);
    if (type == null){
      //Add identifier if no keyword match
      identifier(token);
    } else{
      addToken(type, token, null);
    }
  }


  private void identifier(String token){
    if(!validIdentifier(token)) Jlisp.error("Invalid Identifier: \"" + token + "\"", line);
    addToken(IDENTIFIER, token.trim(), null);
  }

  //TODO: Figure out what valid identifiers are in LISP
  //for now just return true
  private boolean validIdentifier(String token){
    if(keywords.containsKey(token)) return false;
    return true;
  }

  private boolean isDigit(char c){
    return c >= '0' && c <= '9';
  }


  //Add token to tokens with extracted text 
  private void addToken(TokenType type, String text, Object literal) {
    tokens.add(new Token(type, text, literal, line));
  }


}
