Assignment 1
============
Create a recursive-descent parser in Java that parses the following language:

+ program -> statementList

+ statementList -> statement statementList | ε

+ statement -> "read" id | "write" id  | forLoop | whileLoop | conditional | id ":=" expression

+ expression -> term operation term

+ term -> id | number

+ id -> "[a-zA-Z]+"

+ number -> “[0-9]+”

+ operation -> "+" | "-" | "/" | "*"

+ forLoop ->  "for" id number number statementList "rof"

+ whileLoop -> "while" condition statementList "elihw" |

+ conditional -> "if" condition statementList "fi" |

+ condition -> id comparison term

+ comparison -> "==" | "!=" | "<" | ">" | "<=" | ">="

Read from System.in. Your parser must be able to parse sufficiently complicated programs in this language such as:

    while a != b
        if a > b
            a := a − b
        fi
        if a <= b
            b := b – a
        fi
    elihw
