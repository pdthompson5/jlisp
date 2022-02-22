package src.main.java.jlisp;

import java.io.IOException;
import java.util.Scanner;
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


    private static void runREPL(){
        Scanner reader = new Scanner(System.in);

        while(true){
            System.out.print(">>> ");
            String line = reader.nextLine();
            if(line==null) break;
            run(line);
        }
    }


    private static void run(String source){
        System.out.println("Running" + source);
    }
}