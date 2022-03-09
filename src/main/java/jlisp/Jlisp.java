package jlisp;

import java.io.IOException;
import java.util.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Jlisp{
    private static final Interpreter interpreter = new Interpreter();

    //When debug is true, results of top level expressions will be printed. 
    //REPL default: debug==true
    //File default: debug==false
    private static boolean debug = true;
    public static void main(String[] args) throws IOException{
        if (args.length > 2){
            System.out.println("Usage: make jlisp [script]");
            System.exit(64);
        } else if (args.length >= 1){
            if(args.length == 2){
                if(args[1].equals("debug")) debug = true;
            }
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
        System.err.println("Error on line " + line  + ": " + message);
        System.exit(65);
    }



    private static void runREPL(){
        Scanner reader = new Scanner(System.in);
        debug = true;

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

        // for(Token token : tokens){
        //     System.out.println(token.type.toString() + token);
            
        // }

        Parser parser = new Parser(tokens);
        List<Expr> tree = parser.parse();


        // AstPrinter printer = new AstPrinter();
        // for(Expr expr: tree){
        //     System.out.println(printer.print(expr));
        // }

        for(Expr expr : tree){
            Object eval = interpreter.evaluate(expr);
            if(debug){
                System.out.println(Interpreter.stringify(eval));
            }
        }
    }
}