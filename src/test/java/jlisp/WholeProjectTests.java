package jlisp;


import java.util.Collection;
import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.PrintStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.function.Executable;

public class WholeProjectTests {
    //Basic framework: Input -> File with lisp code 
    //Run the lisp code 
    //compare it to a file with the expected output

    private String runLispFile(String testType){
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream temp = System.out;
        System.setOut(new PrintStream(output));

        String[] args = new String[] {"src/test/resources/" + testType + "/input.lisp", "debug"};
        try{
            Jlisp.main(args);
        }
        catch(Exception e){
        }

        System.setOut(temp);
        return output.toString();
    }

    private void verifyOutput(String testType, String output){
        Path path = Path.of("src/test/resources/" + testType + "/expected_output.lisp");
        String expected = "";
        try{
            expected = Files.readString(path);
        }
        catch(IOException e){
            System.err.println(e.getMessage());
            System.exit(65);
        }

        assertEquals(expected, output);
    }

    private void runAndVerify(String testType){
        System.out.println("Starting Test:" + testType);
        verifyOutput(testType, runLispFile(testType));
    }


    @Test
    void testArithmetic(){
        runAndVerify("BasicArithmetic");
    }

    @Test
    void testComparisons(){
        runAndVerify("BasicComparisons");
    }

    @Test
    void testBegin(){
        runAndVerify("Begin");
    }
    @Test
    void testCarAndCdr(){
        runAndVerify("CarAndCdr");
    }
    @Test
    void testCons(){
        runAndVerify("Cons");
    }
    @Test
    void testFunctions(){
        runAndVerify("Functions");
    }
    @Test
    void testIfStatments(){
        runAndVerify("IfStatements");
    }
    @Test
    void testListQuest(){
        runAndVerify("ListQuest");
    }
    @Test
    void testNullQuest(){
        runAndVerify("NullQuest");
    }
    @Test
    void testNumberQuest(){
        runAndVerify("NumberQuest");
    }
    @Test
    void testPrint(){
        runAndVerify("Print");
    }
    @Test
    void testSet(){
        runAndVerify("Set");
    }
    @Test
    void testSymbolQuest(){
        runAndVerify("SymbolQuest");
    }
    @Test
    void testWhileLoops(){
        runAndVerify("WhileLoops");
    }

    



    // @TestFactory
    // Collection<DynamicTest> All(){
    //     File resources = new File("src/test/resources/");
    //     String tests[] = resources.list();
    //     Collection<DynamicTest> dynamicTests = new ArrayList<>();
        
        
    //     for(String test : tests){
    //         Executable exe = () -> runAndVerify(test);
    //         DynamicTest d = DynamicTest.dynamicTest("Test: " + test, exe);

    //         dynamicTests.add(d);
    //     }

    //     return dynamicTests;
    // }
}
