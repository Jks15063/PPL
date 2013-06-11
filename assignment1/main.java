
import java.util.regex.*;
import java.io.*;
import java.util.Scanner;

/**
 * CS 3210
 * main.java
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
 * @version 1.1 refactored to parse files without end of file tokens.
 * @since 2013-06-10
 */
class main {
    private static Scanner sc = new Scanner(System.in);
    private static String token;

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
    * Start and exit point of the program, starts off by checking for a valid beginning to a statement list.
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
    * Exit point for loops and conditionals.
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


//Tests:
//My tests started of very simple to test for individual cases such as:

/*
 * a := a + 1
 */

//then:

/*
 * if a < b
 *     a := a + 1
 * fi
 */

//then:

/*
 * while a < 10
 *     if a < b
 *         a := a + 1
 *     fi
 * elihw
 */

//and finally:

/*
 * read input
 * write filename
 *
 * for a 1 10
 *     while v >= t
 *         if x <= u
 *             if x != 0
 *                 asdf := x + 1
 *                 Y := x / 2
 *                 z := x * 5
 *                 i := x - 1
 *             fi
 *         fi
 *     elihw
 *
 *     while b == r
 *         if b < 5
 *             b := b - 1
 *         fi
 *         if b > 3
 *             for var 2 26
 *                 if varName == 123456
 *                     b := b + 3
 *                     write fileName
 *                 fi
 *             rof
 *             varName := 45 + 34
 *         fi
 *     elihw
 * rof
 */

/*
 * I made sure there were lots of nested statements of different types
 * and tried to use all the possible cases.  After this was being parsed
 * successfully, I started removing and adding things or just mixing them up
 * to check that not only was it catching the bad syntax but catching it for
 * the right reasons and at the right places.  For example: I found at one point
 * that I could remove all the 'fi's and still get a successful parse, and after
 * fixing that problem I found that I could remove the if statements
 * themselves but leave in the 'fi's and still get a successful parse.
 */
