
import java.util.regex.*;
import java.util.Scanner;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.io.*;

/**
 * CS 3210
 * Main.java
 * Purpose: Use top-down recursion to parse a language.
 * Approach: The structure of the program closely resembles the structure
 * of the grammar that it parses. The goal according to the book is to create a set of
 * subroutines that correspond 1:1 to the non-terminals of the grammar,
 * with no loops or if statements.
 * There are no loops in the program but that are many if statements,
 * perhaps the author simply prefers case statements as seen in the
 * pseudo code.
 *
 * @author Jacob Sellers
 * @version 2.0 refactored to parse files without end of file tokens.
 * @since 2013-06-10
 */
class Main {
    private static Scanner sc = new Scanner(System.in);
    private static String token;
    private static Hashtable<String, Integer> variables = new Hashtable<String, Integer>();
    private static boolean execute = true;
    private static Integer ifCount = 0;

    //The token pattern matches white space, and unprintable chars such as a newline.
    private static Pattern tokenPattern = Pattern.compile("[ \r\n\t]+");
    private static Pattern idPattern = Pattern.compile("[a-zA-Z]+");
    private static Pattern numberPattern = Pattern.compile("[0-9]+");
    private static Pattern operationPattern = Pattern.compile("[-\\+\\*/]+");
    private static Pattern startWhilePattern = Pattern.compile("while");
    private static Pattern endWhilePattern = Pattern.compile("elihw");
    private static Pattern startForPattern = Pattern.compile("for");
    private static Pattern endForPattern = Pattern.compile("rof");
    private static Pattern readPattern = Pattern.compile("read");
    private static Pattern writePattern = Pattern.compile("write");
    private static Pattern startIfPattern = Pattern.compile("if");
    private static Pattern endIfPattern = Pattern.compile("fi");
    private static Pattern assignmentPattern = Pattern.compile(":=");
    private static Pattern comparisonPattern = Pattern.compile("==|\\!=|<=|>=|<|>");

   /**
    * The main method for the 'main' program.
    *
    * @param args
    * @exception IOException
    *
    */
    public static void main(String args[]) throws IOException, java.io.FileNotFoundException {
/*
 *        File file = new File (args[0]);
 *
 *        FileInputStream fis = new FileInputStream (file);
 *
 *        byte buffer[] = new byte[(int) file.length()];
 *        fis.read (buffer, 0, (int) file.length());
 *
 *        String s = new String (buffer);
 *
 *        String[] result = s.split("\\s+");
 *        for (int x=0; x<result.length; x++)
 *            System.out.println(result[x]);
 */

        sc.useDelimiter(tokenPattern);
        program();
    }

   /**
    * Check the next token against the expected value and
    * consume the token if the match is successful.
    *
    * @param Pattern the regular expression used to validate the next token.
    */
    public static void match(Pattern expected) {
        //System.out.println("expected: " + expected);
        if (sc.hasNext(expected)) {
            token = sc.next();
            //System.out.println("Matched: " + token);
        }
        else {
            error("match");
        }
    }

   /**
    * Start and exit point of the program, starts off by checking for a valid beginning to a statement list.
    *
    */
    public static void program() {
        //System.out.println("Start Program:");
        if (sc.hasNext(readPattern) || sc.hasNext(writePattern) || sc.hasNext(startWhilePattern) || sc.hasNext(startForPattern) || sc.hasNext(idPattern) || !sc.hasNext()) {
            statementList();
            if (!sc.hasNext()) {
                System.out.println("No syntax errors detected.");
            }
            else {
                error("program");
            }
        }
        else {
            error("program");
        }
    }

   /**
    * Checks for a valid beginning to a statement list and if it finds one,
    * calls the statement method and itself. Also checks for the end of file token and
    * returns if it is found.
    * Exit point for loops and conditionals.
    *
    */
    public static void statementList() {
        //System.out.println("Statement List:");
        if (sc.hasNext(readPattern) || sc.hasNext(writePattern) || sc.hasNext(startWhilePattern) || sc.hasNext(startForPattern) || sc.hasNext(idPattern)) {

            if (sc.hasNext(endIfPattern) || sc.hasNext(endWhilePattern) || sc.hasNext(endForPattern)) {
                return;
            }
            statement();
            statementList();
        }
        else if (!sc.hasNext()) {
            return;
        }
        else {
            error("statementList");
        }
    }

   /**
    * Checks for valid statement syntax and makes further
    * method calls.
    *
    */
    public static void statement() {
        //System.out.println("Statement:");
        if (sc.hasNext(readPattern)) {
            match(readPattern);
            match(idPattern);
        }
        else if (sc.hasNext(writePattern)) {
            match(writePattern);
            match(idPattern);
            if (execute) {
                System.out.println(token + " is equal to: " + variables.get(token));
            }
        }
        else if (sc.hasNext(startWhilePattern)) {
            match(startWhilePattern);
            condition();
            statementList();
            match(endWhilePattern);
        }
        else if (sc.hasNext(startForPattern)) {
            match(startForPattern);
            match(idPattern);
            match(numberPattern);
            match(numberPattern);
            statementList();
            match(endForPattern);
        }
        else if (sc.hasNext(startIfPattern)) {
            match(startIfPattern);
            execute = condition();
            if (!execute) {
                ifCount++;
            }
            statementList();
            match(endIfPattern);
            if (!execute) {
                ifCount--;
            }
            if (ifCount == 0) {
                execute = true;
            }
        }
        else if (sc.hasNext(idPattern)) {
            match(idPattern);
            String tempID = token;
            match(assignmentPattern);
            Integer tempInt = expression();
            if (execute) {
                variables.put(tempID, tempInt);
            }
        }
        else {
            error("statement");
        }
    }

   /**
    * Checks for valid expression syntax.
    *
    */
    public static Integer expression() {
        //System.out.println("expression:");
        if (sc.hasNext(idPattern) || sc.hasNext(numberPattern)) {
            Integer termA = term();
            String op = operation();
            Integer termB = term();
            if (op.equals("+")) {
                return termA + termB;
            }
            if (op.equals("-")) {
                return termA - termB;
            }
            if (op.equals("*")) {
                return termA * termB;
            }
            if (op.equals("/")) {
                return termA / termB;
            }
        }
        else {
            error("expression");
        }
        return null;
    }

   /**
    * Checks for valid term syntax.
    *
    */
    public static Integer term() {
        //System.out.println("term:");
        if (sc.hasNext(idPattern)) {
            match(idPattern);
            return variables.get(token);
        }
        else if (sc.hasNext(numberPattern)) {
            match(numberPattern);
            return Integer.parseInt(token);
        }
        else {
            error("term");
            return null;
        }
    }

   /**
    * Checks for valid operation syntax.
    *
    */
    public static String operation() {
        //System.out.println("operation:");
        if (sc.hasNext(operationPattern)) {
            match(operationPattern);
            return token;
        }
        else {
            error("operation");
            return null;
        }
    }

   /**
    * Checks for valid condition syntax.
    *
    */
    public static boolean condition() {
        if (sc.hasNext(idPattern)) {
            match(idPattern);
            Integer tempInt = variables.get(token);
            String compOp = comparison();
            Integer termC = term();
            if (execute) {
                if (compOp.equals("==")) {
                    if (tempInt == termC) {
                        return true;
                    }
                }
                if (compOp.equals("!=")) {
                    if (tempInt != termC) {
                        return true;
                    }
                }
                if (compOp.equals("<")) {
                    if (tempInt < termC) {
                        return true;
                    }
                }
                if (compOp.equals(">")) {
                    if (tempInt > termC) {
                        return true;
                    }
                }
                if (compOp.equals("<=")) {
                    if (tempInt <= termC) {
                        return true;
                    }
                }
                if (compOp.equals(">=")) {
                    if (tempInt >= termC) {
                        return true;
                    }
                }
            }
        }
        else {
            error("condition");
        }
        return false;
    }

   /**
    * Checks for valid comparison syntax.
    *
    */
    public static String comparison() {
        if (sc.hasNext(comparisonPattern)) {
            match(comparisonPattern);
            return token;
        }
        else {
            error("comparison");
            return null;
        }
    }

   /**
    * Called when the expected token and the next token do not match,
    * prints an error message with the name of the method that called it.
    *
    * @param String the name of the method that found the syntax error.
    */
    public static void error(String location) {
        System.out.println("Syntax error found during: " + location);
        System.exit(-1);
    }
}
