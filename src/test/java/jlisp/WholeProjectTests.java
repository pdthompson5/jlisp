package jlisp;

import java.nio.file.Files;
import java.nio.file.Path;
import java.io.PrintStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.TestFactory;
// import org.junit.jupiter.api.DynamicTest;
// import org.junit.jupiter.api.function.Executable;
// import java.util.Collection;
// import java.util.ArrayList;

/**
 * Test class for the entire project (black box testing)
 * Executed using Junit5 
 * Every test in this class takes an input lisp file (input.lisp), runs it then asserts
 * that the output is equivalent to a sample output file(expected_output.lisp)
 * Those two files can be found in src/test/resources/${TestName}/
 * These files are run with debug enabled so the output of each top-level expression is printed 
 */
public class WholeProjectTests {

    //Run lisp file and return output
    private String runLispFile(String testType) { 
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream temp = System.out;
        //Capture standard output
        System.setOut(new PrintStream(output));

        //Args -> [file_name, debug]
        String[] args = new String[] {"src/test/resources/" + testType + "/input.lisp", "debug"};
        try {
            //Run project with args
            Jlisp.main(args);
        } catch (Exception e) { //IO exception, should never occur
        }

        System.setOut(temp);
        return output.toString();
    }

    //Asserts that output is equal to expected_output.lisp
    private void verifyOutput(String testType, String output) {
        //Load in output file 
        Path path = Path.of("src/test/resources/" + testType + "/expected_output.lisp");
        String expected = "";
        try {
            expected = Files.readString(path);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(65);
        }

        assertEquals(expected, output);
    }

    //Run file for test and verify results
    private void runAndVerify(String testType) {
        verifyOutput(testType, runLispFile(testType));
    }


    @Test
    void testArithmetic() {
        runAndVerify("BasicArithmetic");
    }

    @Test
    void testComparisons() {
        runAndVerify("BasicComparisons");
    }

    @Test
    void testBegin() {
        runAndVerify("Begin");
    }

    @Test
    void testCarAndCdr() {
        runAndVerify("CarAndCdr");
    }

    @Test
    void testCons() {
        runAndVerify("Cons");
    }

    @Test
    void testFunctions() {
        runAndVerify("Functions");
    }

    @Test
    void testIfStatments() {
        runAndVerify("IfStatements");
    }

    @Test
    void testListQuest() {
        runAndVerify("ListQuest");
    }

    @Test
    void testNullQuest() {
        runAndVerify("NullQuest");
    }

    @Test
    void testNumberQuest() {
        runAndVerify("NumberQuest");
    }

    @Test
    void testPrint() {
        runAndVerify("Print");
    }

    @Test
    void testSet() {
        runAndVerify("Set");
    }

    @Test
    void testSymbolQuest() {
        runAndVerify("SymbolQuest");
    }

    @Test
    void testWhileLoops() {
        runAndVerify("WhileLoops");
    }

    @Test
    void testRecursion() {
        runAndVerify("Recursion");
    }

    @Test
    void testInsertionSort() {
        runAndVerify("InsertionSort");
    }

    @Test
    void testQuoteAndEval() {
        runAndVerify("QuoteAndEval");
    }

    @Test
    void testPassByValue() {
        runAndVerify("PassByValue");
    }

    @Test
    void test01Knapsack() {
        runAndVerify("0-1Knapsack");
    }
    //This test is commented out since the code will result in a system exit. 
    //Regardless, a sample run can be found in the resources folder.
    // @Test
    // void testCallStack(){
    //     runAndVerify("CallStack");
    // }


    //The following commented out function does the same thing as the above test cases, and it does it more elegantly 
    //However, my maven junit test runner does not associate junit names with test failures so it is difficult to determine which test failed 

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
