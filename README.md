# Mini Java Interpreter
## Assignment 6: Expression and Statement Implementation

This project builds upon a handout to create a simplified version of Java, "MiniJava", using abstract syntax trees (abbr. "AST"), because we do not know how to represent them as lines. It also uses lambdas, recursion (e.g. for while loops implementation) and two design patterns: composite and especially the visitor pattern.

## Project Structure

```
Assignment6/
├── pom.xml                                           # maven config
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
    │           │   ├── ProgramSerializerVisitor.java # serializes code to java
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

First we imported our previous answers from assignment 6a into the handout, "assignment 6 new". This included

- Binaries: `+`, `-`, (labelled "PLUS2" and "MINUS2") `*`, `/`, `%`.
- Unaries: `+`, `-` (labelled "PLUS1" and "MINUS1") 
- Added binaries and unaries to operatorFunctions and operatorTypes Maps
- More exemplary ASTs (statements 7-9) by mimicking statements 1-6
- Finished the implementation of printStatement 

## Assignment 6b: Control Flow + Optional Task

Then we worked on the java control flows, including a decision-making statement (if-then-else) and the optional looping statement (a while loop). There are no booleans, so true/false statements are positive/negative INTs respectively

- First we created a while loop java file
  - Implemented type checking (in ProgramTypeVisitor)
  - Looping logic via recursion of visit() method (in ProgramExecutorVisitor)

- OPTIONAL TASK: Then we created IfThenElse java file
  - Implemented misc visit() in ProgramVisitor and Shortcut 
  - Then implemented type checking in a similar vein as while loop (also in ProgramTypeVisitor)
  - Then if then else logic (by simply using if else) (also in ProgramExecutorVisitor)
  - Lastly, created a "serializer" that writes the actual "if ( [condition] ) { [thenStatement] } else { [elseStatement] }" (in ProgramSerializerVisitor)
  - Added associated tests

## Testing

As for testing, this includes:
- Type checking (INT with INT, FLOAT with FLOAT)
- Operator evaluation (e.g. correct use of PLUS2, MOD etc)
- Control flow execution (IF-THEN-ELSE)
- Error handling (pass FLOAT instead of INT)
