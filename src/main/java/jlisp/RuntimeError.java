package jlisp;

/**
 * Runtime exception class for use within the Interpreter class.
 * Code copied from CraftingInterpeters RuntimeError class.
 */

public class RuntimeError extends RuntimeException {
    final Token token;

    RuntimeError(Token token, String message) {
        super(message);
        this.token = token;
    }
}
