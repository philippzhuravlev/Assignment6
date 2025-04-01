# MiniJava Type Checker and Interpreter - Assignment 6

This project implements a **type checker** and **interpreter** for a simplified version of Java called MiniJava. It is part of the coursework for Advanced Programming (Course 02324) at DTU.

---

## 📌 Project Overview

This MiniJava tool supports:

- ✅ Static type checking using the **Visitor Pattern** (`ProgramTypeVisitor`)
- ✅ Runtime execution using a second visitor (`ProgramExecutorVisitor`)
- ✅ Core language constructs:
  - Variable declarations and assignments
  - Integer and float arithmetic
  - Loops (`while`)
  - Nested expressions
  - Print statements

---

## 📚 Contents

- ✅ **Assignment 6a**: Type checking using `ProgramTypeVisitor`
- ✅ **Assignment 6b**: Program execution using `ProgramExecutorVisitor`
- ✅ Optional features ... TODO!!!

---

## 🚀 How It Works

MiniJava programs are parsed into an **Abstract Syntax Tree (AST)**. Two visitors process the AST:

- **ProgramTypeVisitor**: statically checks for type errors (e.g., mismatched types, undeclared variables)
- **ProgramExecutorVisitor**: evaluates and runs the MiniJava program

Both use the Visitor Pattern to walk the AST and perform their logic.

---

## 🧠 Design Patterns Used

### Visitor Pattern
Used to define operations (`visit(...)`) over different node types without modifying the AST classes.

### Composite Pattern
Used to structure the AST itself as a tree of `Statement`, `Expression`, and `Var` nodes.

---

## 🧪 Tests Included

All logic is tested in `TestMiniJava.java`. Test cases include:

- ✔️ Variable declarations and assignments
- ✔️ Arithmetic expressions (int & float)
- ✔️ Nested loops and conditionals
- ✔️ Division by zero (runtime error test)
- ✔️ Simulated `if` using `while`
- ✔️ Float operations and chained expressions

---

## 📁 Project Structure

```text
Assignment6/
├── pom.xml                      # Maven configuration
├── README.md                    # Project description
├── src/
│   ├── main/
│   │   └── java/
│   │       └── dk/dtu/compute/course02324/mini_java/
│   │           ├── MiniJavaRun.java
│   │           ├── infrastructure/
│   │           │   └── VisitAcceptor.java
│   │           ├── semantics/
│   │           │   ├── ProgramTypeVisitor.java         # Type checking visitor
│   │           │   ├── ProgramExecutorVisitor.java     # Execution visitor
│   │           │   ├── ProgramSerializerVisitor.java   # (Optional) serializing code
│   │           │   └── ProgramVisitor.java             # Visitor base class
│   │           ├── model/
│   │           │   ├── Statement.java, Expression.java, Var.java, ...
│   │           └── utils/
│   │               └── Shortcuts.java                  # Factory methods for building ASTs
│   └── test/
│       └── java/dk/dtu/compute/course02324/mini_java/
│           └── TestMiniJava.java                       # JUnit tests for type and runtime behavior
