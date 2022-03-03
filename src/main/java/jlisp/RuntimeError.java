package jlisp;
//TODO: Not sure if I need this class
public class RuntimeError extends RuntimeException{
    final Token token;

    RuntimeError(Token token, String message){
        super(message);
        this.token = token;
    }
}
