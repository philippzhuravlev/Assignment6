# Mini Java Interpreter

github repo: https://github.com/philippzhuravlev/Assignment6

## Assignment 6: Expression and Statement Implementation

This project builds upon a handout to create a simplified version of Java, "MiniJava", using abstract syntax trees (abbr. "AST"). It also uses lambdas, recursion (e.g. for while loops implementation) and two design patterns: composites and especially the visitor pattern.

## Project Structure

```
Assignment6/
├── pom.xml                                           # maven test config
├── README.md                                         # you are here!
└── src/
    ├── main/
    │   └── java/dk/dtu/compute/course02324/mini_java/
    │           ├── MiniJavaRun.java                  # main file
    │           ├── infrastructure/                   # accept(Visitor) interface
    │           │   └── VisitAcceptor.java
    │           ├── semantics/
    │           │   ├── ProgramTypeVisitor.java       # type checking
    │           │   ├── ProgramExecutorVisitor.java   # executes statements
    │           │   ├── ProgramSerializerVisitor.java # "serializes" code to java
    │           │   └── ProgramVisitor.java           # visitor base class
    │           ├── model/                            # statement base classes
    │           │   ├── Statement.java, Expression.java, Var.java, ...
    │           └── utils/
    │               └── Shortcuts.java                # to more easily manually build ASTs
    └── test/
        └── java/dk/dtu/compute/course02324/mini_jsava/
            └── TestMiniJava.java                     # JUnit testing
```

## Assignment 6a: Operators

First we imported our previous answers from assignment 6a into the handout, "assignment 6 new". This includes

- Binaries: `+`, `-`, (labelled "PLUS2" and "MINUS2") `*`, `/`, `%` and Unaries: `+`, `-` (labelled "PLUS1" and "MINUS1"). These were similar to the already-existing implementations, e.g. PLUS2
- Added binaries and unaries to operatorFunctions and operatorTypes Maps
- More exemplary ASTs (statements 7-9) by mimicking statements 1-6
- Finished the implementation of printStatement. Though "assignment 6a new" implemented much of this, our original implementation was:
  - Added misc visit() methods, e.g. Shortcut, VisitCoordinator and ProgramVisitor
  - Writing out PrintStatement.java, modelled after the other java classes provided to us
  - Did the actual printing logic in what is now called ProgramExecutorVisitor.java, just using Java's println
  - Added functionality to ProgramSerializerVisitor, mostly modelled after other visit() methods but again using System.out.println

## Assignment 6b: Control Flow

Then we worked on the java control flows, including a decision-making statement (if-then-else) and the optional looping statement (a while loop). There are no booleans, so true/false statements are positive/negative INTs

First we used the java while loop file provided to us, and then did the TODOs:
  - Implemented type checking so it only works for INTs, our proxy for booleans (in ProgramTypeVisitor)
  - Looping logic via recursion of visit() method, following the instructions and TODO (in ProgramExecutorVisitor)

## Assignment 6b: Optional Task
Then we created IfThenElse java file. This was modelled after the while loop file, though needed the statement to be split between a thenStatement and an elseStatement
  - Implemented misc visit() method in ProgramVisitor and Shortcut
  - Then implemented type checking in a similar vein as while loop (also in ProgramTypeVisitor)
  - After that, we implemented the if-then-else logic, by simply assigning the condition and statements into Java's if else structure (also in ProgramExecutorVisitor)
  - Lastly, created a "serializer" that writes out the actual "if ( [condition] ) { [thenStatement] } else { [elseStatement] }", also build like the while loop (in ProgramSerializerVisitor)
  - Added associated tests, which run without issues

## Testing

As for testing, this includes:
- Type checking (INT with INT, FLOAT with FLOAT)
- Operator evaluation (e.g. correct use of PLUS2, MOD etc)
- Control flow execution (IF-THEN-ELSE)
- Error handling (pass FLOAT instead of INT)
