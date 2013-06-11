
import java.util.regex.*;
import java.io.*;
import java.util.Scanner;

/**
 * CS 3210
 * main.java
 * Purpose: Use top-down recursion to parse a language.
 *
 * @author Jacob Sellers
 * @version 1.0
 * @since 2013-06-10
 */
class main {
    private static Scanner sc = new Scanner(System.in);
    private static String token;

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
    private static Pattern eofPattern = Pattern.compile("\\$\\$");

   /**
    * The main method for the 'main' program.
    *
    * @param args
    * @exception IOException
    *
    */
    public static void main(String args[]) throws IOException {
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
        System.out.println("expected: " + expected);
        if (sc.hasNext(expected)) {
            token = sc.next();
            System.out.println("Matched: " + token);
        }
        else {
            error("match");
        }
    }

   /**
    * Starts the program off by checking for a valid beginning to a statement list.
    *
    */
    public static void program() {
        System.out.println("Start Program:");
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
    *
    */
    public static void statementList() {
        System.out.println("Statement List:");
        if (sc.hasNext(readPattern) || sc.hasNext(writePattern) || sc.hasNext(startWhilePattern) || sc.hasNext(startForPattern) || sc.hasNext(idPattern)) {

            if (sc.hasNext(endIfPattern) || sc.hasNext(endWhilePattern) || sc.hasNext(endForPattern)) {
                return;
            }
            statement();
            statementList();
        }
        else if (sc.hasNext(endIfPattern)) {
            //match(endIfPattern);
            return;
        }
        else if (sc.hasNext(endForPattern)) {
            return;
        }
        else if (sc.hasNext(endWhilePattern)) {
            return;
        }
        else if (!sc.hasNext()) {
            return;
        }
        else {
            error("statementList");
        }
    }

   /**
    * Checks for valid statement syntax.
    *
    */
    public static void statement() {
        System.out.println("Statement:");
        if (sc.hasNext(readPattern)) {
            match(readPattern);
            match(idPattern);
        }
        else if (sc.hasNext(writePattern)) {
            match(writePattern);
            match(idPattern);
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
            condition();
            statementList();
            match(endIfPattern);
        }
        else if (sc.hasNext(idPattern)) {
            match(idPattern);
            match(assignmentPattern);
            expression();
        }
        else {
            error("statement");
        }
    }

   /**
    * Checks for valid expression syntax.
    *
    */
    public static void expression() {
        System.out.println("expression:");
        if (sc.hasNext(idPattern) || sc.hasNext(numberPattern)) {
            term();
            operation();
            term();
        }
        else {
            error("expression");
        }
    }

   /**
    * Checks for valid term syntax.
    *
    */
    public static void term() {
        System.out.println("term:");
        if (sc.hasNext(idPattern)) {
            match(idPattern);
        }
        else if (sc.hasNext(numberPattern)) {
            match(numberPattern);
        }
        else {
            error("term");
        }
    }

   /**
    * Checks for valid operation syntax.
    *
    */
    public static void operation() {
        System.out.println("operation:");
        if (sc.hasNext(operationPattern)) {
            match(operationPattern);
        }
        else {
            error("operation");
        }
    }

   /**
    * Checks for valid condition syntax.
    *
    */
    public static void condition() {
        if (sc.hasNext(idPattern)) {
            match(idPattern);
            comparison();
            term();
        }
        else {
            error("condition");
        }
    }

   /**
    * Checks for valid comparison syntax.
    *
    */
    public static void comparison() {
        if (sc.hasNext(comparisonPattern)) {
            match(comparisonPattern);
        }
        else {
            error("comparison");
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
