package jlisp;

import java.io.IOException;
import java.util.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Jlisp{
    public static void main(String[] args) throws IOException{
        if (args.length > 1){
            System.out.println("Usage: make jlisp [script]");
            System.exit(64);
        } else if (args.length == 1){
            runFile(args[0]);
        } else{
            runREPL();
        }
    }


    private static void runFile(String path) throws IOException{
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()));
    }

    public static void error(String message, int line){
        System.out.println("Error on line " + line  + ":" + message);
        System.exit(65);
    }


    private static void runREPL(){
        Scanner reader = new Scanner(System.in);

        while(true){
            System.out.print(">>> ");
            String line = reader.nextLine();
            if(line==null) break;
            run(line);
        }

        reader.close();
    }

    


    private static void run(String source){
        //The path: Source String -> Scanner -> List of tokens -> Parser -> AST -> Interpreter -> output 
        LispScanner scanner = new LispScanner(source);
        List<Token> tokens = scanner.scanTokens();

        for(Token token : tokens){
            System.out.println(token);
        }

        // Parser parser = new Parser();
        // Expr tree = parser.parse(tokens);

        // Interpreter interpreter = new Interpreter();
        // interpreter.interpret(tree);


    }
}