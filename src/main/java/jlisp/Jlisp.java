package jlisp;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

/**
 * Main class for this project. 
 * Arguments: [(filename) , (debug)], both are optional 
 * Output: Evaluation result. If debug is enabled, each top level expression is printed 
 * Based on the Jlox class from CraftingInterpreters
 */

public class Jlisp {

    //When debug is true, results of top level expressions will be printed. 
    //REPL default: debug==true
    //File default: debug==false
    private static boolean debug = false;

    public static void main(String[] args) throws IOException {
        if (args.length > 2) {
            System.out.println("Usage: make jlisp [script]");
            System.exit(64);
        } else if (args.length >= 1) {
            if (args.length == 2) {
                if(args[1].equals("debug")) debug = true;
            }
            runFile(args[0]);
        } else {
            runREPL();
        }
    }


    private static void runFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        Interpreter interpreter = new Interpreter();
        run(new String(bytes, Charset.defaultCharset()), interpreter);
    }

    public static void error(String message, int line) {
        System.err.println("Error on line " + line  + ": " + message);
        System.exit(65);
    }


    //Borrowed from CraftingInterpreters
    private static void runREPL() {
        Scanner reader = new Scanner(System.in);
        Interpreter interpreter = new Interpreter();
        debug = true;

        while (true) {
            System.out.print(">>> ");
            String line = reader.nextLine();
            if(line==null) break;
            run(line, interpreter);
        }

        reader.close();
    }

    

    //Scans the string, parses the tokens, evaluates the expressions
    private static void run(String source, Interpreter interpreter) {
        LispScanner scanner = new LispScanner(source);
        List<Token> tokens = scanner.scanTokens();

        Parser parser = new Parser(tokens);
        List<Expr> tree = parser.parse();

        for (Expr expr : tree) {
            Object eval = interpreter.evaluate(expr);
            if (debug) {
                System.out.println(Interpreter.stringify(eval));
            }
        }
    }
}