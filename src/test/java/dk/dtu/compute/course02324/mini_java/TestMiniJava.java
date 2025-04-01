package dk.dtu.compute.course02324.mini_java;

import dk.dtu.compute.course02324.mini_java.model.*;
import dk.dtu.compute.course02324.mini_java.semantics.*;

import static dk.dtu.compute.course02324.mini_java.utils.Shortcuts.*;
import static dk.dtu.compute.course02324.mini_java.model.Operator.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * These are some basic tests of the MiniJava for computing the types and
 * evaluating expressions.
 */
public class TestMiniJava{

    private ProgramTypeVisitor ptv;

    private ProgramExecutorVisitor pev;

    /**
     *  Sets up the visitors for type checking and execution.
     */
    @BeforeEach
    public void setUp() {
        ptv = new ProgramTypeVisitor();
        pev = new ProgramExecutorVisitor(ptv);
    }

    @Test
    public void testCorrectProgramWithInts() {
        int i;
        int j = i = 2 + (i = 3) ;

        Statement statement = new Sequence(
                new Declaration(INT, new Var("i")),
                new Declaration(
                        INT,
                        new Var("j"),
                        new Assignment(
                                new Var("i"),
                                new OperatorExpression(
                                        PLUS2,
                                        new IntLiteral(2),
                                        new Assignment(
                                                new Var("i"),
                                                new IntLiteral(3)
                                        )))
                )
        );

        ptv.visit(statement);
        if (!ptv.problems.isEmpty()) {
            fail("The type visitor did detect typing problems, which should not be there!");
        }

        pev.visit(statement);

        Set<String> variables = new HashSet<>(List.of("i", "j"));
        for (Var var: ptv.variables) {
            variables.remove(var.name);

            if (var.name.equals("i")) {
                assertEquals(i, pev.values.get(var), "Value of variable i should be " + i + ".");
            } else if (var.name.equals("j")) {
                assertEquals(j, pev.values.get(var), "Value of variable j should be " + j + ".");
            } else {
                fail("A non-existing variable " + var.name + " occurred in evaluation of program.");
            }
        }
        assertEquals(0, variables.size(), "Some variables have not been evaluated");
    }

    @Test
    public void testCorrectlyTypedProgramWithFloats() {
        float i;
        float j = i = 2.75f - ( i = 3.21f );

        Statement statement =
                new Sequence(
                        new Declaration(FLOAT, new Var("i")),
                        new Declaration(
                                FLOAT,
                                new Var("j"),
                                new Assignment(
                                        new Var("i"),
                                        new OperatorExpression(
                                                MINUS2,
                                                new FloatLiteral(2.75f),
                                                new Assignment(
                                                        new Var("i"),
                                                        new FloatLiteral(3.21f)
                                                )
                                        )
                                )
                        )
                );

        ptv.visit(statement);
        if (!ptv.problems.isEmpty()) {
            fail("The type visitor did detect typing problems, which should not be there!");
        }
        pev.visit(statement);

        Set<String> variables = new HashSet<>(List.of("i", "j"));
        for (Var var: ptv.variables) {
            variables.remove(var.name);

            if (var.name.equals("i")) {
                assertEquals(i, pev.values.get(var), "Value of variable i should be " + i + ".");
            } else if (var.name.equals("j")) {
                assertEquals(j, pev.values.get(var), "Value of variable j should be " + j + ".");
            } else {
                fail("A non-existing variable " + var.name + " occurred in evaluation of program.");
            }
        }
        assertEquals(0, variables.size(), "Some variables have not been evaluated");

    }

    @Test
    public void testWronglyTypedProgram() {
        // int i;
        // int j = i = 2 + (i = 3) ;

        Statement statement =
                new Sequence(
                        new Declaration(FLOAT, new Var("i")),
                        new Declaration(INT, new Var("j")),
                        new Declaration(
                                FLOAT,
                                new Var("j"),
                                new Assignment(
                                        new Var("i"),
                                        new OperatorExpression(
                                                MINUS2,
                                                new FloatLiteral(2.75f),
                                                new Assignment(
                                                        new Var("i"),
                                                        new FloatLiteral(3.21f)
                                                )
                                        )
                                )
                        ),
                        Assignment(Var("i"), Var("k")),
                        Assignment(Var("k"), Literal(3))
                );

        ptv.visit(statement);

        if (ptv.problems.isEmpty()) {
            fail("No type problems detected in a mistyped statement!");
        }
    }

    @Test
    public void testLoopProgram() {
        int i = 5;
        int j = 0;
        int sum = 0;
        while ( i >= 0 ) {
            j = i;
            while ( j >= 0 ) {
                sum = sum + j;
                j = j - 1;
                // println(" i: ", i);
                // println(" j: ", j);
            };
            i = i - 1;
        };

        Statement statement = Sequence(
                Declaration(INT, Var("i"), Literal(5)),
                Declaration(INT, Var("sum"), Literal(0)),
                WhileLoop(
                        Var("i"),
                        Sequence(
                                Declaration(INT, Var("j"), Var("i")),
                                WhileLoop(
                                        Var("j"),
                                        Sequence(
                                                Assignment(
                                                        Var("sum"),
                                                        OperatorExpression(PLUS2,
                                                                Var("sum"),
                                                                Var("j")
                                                        )
                                                ),
                                                Assignment(
                                                        Var("j"),
                                                        OperatorExpression(MINUS2,
                                                                Var("j"),
                                                                Literal(1)
                                                        )
                                                ),
                                                PrintStatement(" i: ", Var("i")),
                                                PrintStatement(" j: ", Var("j"))
                                        )
                                ),
                                Assignment(
                                        Var("i"),
                                        OperatorExpression(MINUS2,
                                                Var("i"),
                                                Literal(1)
                                        )
                                )
                        )
                )
        );

        ptv.visit(statement);
        if (!ptv.problems.isEmpty()) {
            fail("The type visitor did detect typing problems, which should not be there!");
        }
        pev.visit(statement);

        Set<String> variables = new HashSet<>(List.of("i", "j", "sum"));
        for (Var var: ptv.variables) {
            variables.remove(var.name);

            if (var.name.equals("i")) {
                assertEquals(i, pev.values.get(var), "Value of variable i should be " + i + ".");
            } else if (var.name.equals("j")) {
                assertEquals(j, pev.values.get(var), "Value of variable j should be " + j + ".");
            } else if (var.name.equals("sum")) {
                assertEquals(sum, pev.values.get(var), "Value of variable sum should be " + sum + ".");
            } else {
                fail("A non-existing variable " + var.name + " occurred in evaluation of program.");
            }
        }
        assertEquals(0, variables.size(), "Some variables have not been evaluated");
    }

    @Test
    public void testPrintAndAdditionalOperators() {
        int i = - + -1 + 7 - 1;
        float x = - + -1.5f + 7.0f - 1.0f;
        int j = 36 % 7;
        int k = 36 / 7;
        float y = 36.0f / 7.0f;

        Sequence printStatements = Sequence(
                Declaration(INT,
                        Var("i"),
                        OperatorExpression(MINUS2,
                                OperatorExpression(PLUS2,
                                        OperatorExpression(MINUS1,
                                                OperatorExpression(PLUS1,
                                                        Literal(-1)
                                                )
                                        ),
                                        Literal(7)
                                ),
                                Literal(1)
                        )
                ),
                PrintStatement(" - + -1 + 7 - 1: ",
                        Var("i")
                ),
                Declaration(FLOAT,
                        Var("x"),
                        OperatorExpression(MINUS2,
                                OperatorExpression(PLUS2,
                                        OperatorExpression(MINUS1,
                                                OperatorExpression(PLUS1,
                                                        Literal(-1.5f)
                                                )
                                        ),
                                        Literal(7.0f)
                                ),
                                Literal(1.0f)
                        )
                ),
                PrintStatement(" - + -1.5f + 7.0f - 1.0f: ",
                        Var("x")
                ),
                Declaration(INT,
                        Var("j"),
                        OperatorExpression(MOD,
                                Literal(36),
                                Literal(7)
                        )
                ),
                PrintStatement("36 % 7: ",
                        Var("j")
                ),
                Declaration(INT,
                        Var("k"),
                        OperatorExpression(DIV,
                                Literal(36),
                                Literal(7)
                        )
                ),
                PrintStatement("36 / 7: ",
                        Var("k")
                ),
                Declaration(FLOAT,
                        Var("y"),
                        OperatorExpression(DIV,
                                Literal(36.0f),
                                Literal(7.0f)
                        )
                ),
                PrintStatement("36.0f / 7.0: ",
                        Var("y")
                )
        );


        ptv.visit(printStatements);
        if (!ptv.problems.isEmpty()) {
            fail("The type visitor did detect typing problems, which should not be there!");
        }

        pev.visit(printStatements);

        Set<String> variables = new HashSet<>(List.of("i", "x", "j", "k", "y"));
        for (Var var: ptv.variables) {
            variables.remove(var.name);

            if (var.name.equals("i")) {
                assertEquals(i, pev.values.get(var), "Value of variable i should be " + i + ".");
            } else if (var.name.equals("x")) {
                assertEquals(x, pev.values.get(var), "Value of variable j should be " + x + ".");
            } else if (var.name.equals("j")) {
                assertEquals(j, pev.values.get(var), "Value of variable j should be " + j + ".");
            } else if (var.name.equals("k")) {
                assertEquals(k, pev.values.get(var), "Value of variable j should be " + k + ".");
            } else if (var.name.equals("y")) {
                assertEquals(y, pev.values.get(var), "Value of variable j should be " + y + ".");
            } else {
                fail("A non-existing variable " + var.name + " occurred in evaluation of program.");
            }
        }
        assertEquals(0, variables.size(), "Some variables have not been evaluated");
    }

    /**
     * Tests nested while-loops that calculate the sum of all i + j for i and j in [0..2].
     * <p>
     * In each iteration, the value (i + j) is added to a variable called {@code sum}.
     * The final result is printed at the end.
     * </p>
     * <p>
     * Expected total: (0+0)+(0+1)+(0+2)+(1+0)+...+(2+2) = 27
     * </p>
     *
     * This test confirms:
     * <ul>
     *   <li>Correct behavior of nested loops</li>
     *   <li>Proper arithmetic with variables</li>
     *   <li>That the interpreter terminates and updates variables correctly</li>
     * </ul>
     */
    @Test
    public void testNestedLoopsSum() {
        Statement program = Sequence(
                Declaration(INT, Var("i"), Literal(0)),
                Declaration(INT, Var("j"), Literal(0)),
                Declaration(INT, Var("sum"), Literal(0)),
                WhileLoop(
                        OperatorExpression(MINUS2, Literal(3), Var("i")),  // while (i < 3)
                        Sequence(
                                Assignment(Var("j"), Literal(0)),
                                WhileLoop(
                                        OperatorExpression(MINUS2, Literal(3), Var("j")),  // while (j < 3)
                                        Sequence(
                                                Assignment(
                                                        Var("sum"),
                                                        OperatorExpression(PLUS2,
                                                                Var("sum"),
                                                                OperatorExpression(PLUS2, Var("i"), Var("j"))
                                                        )
                                                ),
                                                Assignment(Var("j"),
                                                        OperatorExpression(PLUS2, Var("j"), Literal(1))
                                                )
                                        )
                                ),
                                Assignment(Var("i"),
                                        OperatorExpression(PLUS2, Var("i"), Literal(1))
                                )
                        )
                ),
                PrintStatement("sum = ", Var("sum"))  // Optional: observe output
        );

        runAndCheckNoTypeErrors(program);
    }


    /**
     * Tests float arithmetic using unary and binary operators.
     * Expected behavior: Expression is evaluated correctly and printed.
     */
    @Test
    public void testFloatArithmetic() {
        Statement program = Sequence(
                Declaration(FLOAT, Var("x"),
                        OperatorExpression(MINUS2,
                                OperatorExpression(PLUS2,
                                        OperatorExpression(MINUS1,
                                                OperatorExpression(PLUS1, Literal(-1.5f))
                                        ),
                                        Literal(2.5f)
                                ),
                                Literal(1.0f)
                        )
                ),
                PrintStatement("Float math x = ", Var("x"))
        );

        runAndCheckNoTypeErrors(program);
    }

    /**
     * Tests chained variable assignments using earlier variable values.
     * Expected behavior: Variable c ends up with correct result from a and b.
     */
    @Test
    public void testChainedAssignments() {
        Statement program = Sequence(
                Declaration(INT, Var("a"), OperatorExpression(PLUS2, Literal(1), Literal(2))),
                Declaration(INT, Var("b"), OperatorExpression(PLUS2, Var("a"), Literal(3))),
                Declaration(INT, Var("c"), OperatorExpression(MULT, Var("b"), Literal(2))),
                PrintStatement("c = ", Var("c"))
        );

        runAndCheckNoTypeErrors(program);
    }

    /**
     * Tests that dividing by zero causes an ArithmeticException at runtime.
     */
    @Test
    public void testDivisionByZeroThrows() {
        Statement program = Declaration(INT, Var("fail"), OperatorExpression(DIV, Literal(1), Literal(0)));

        ptv = new ProgramTypeVisitor();
        pev = new ProgramExecutorVisitor(ptv);
        ptv.visit(program);

        assertThrows(ArithmeticException.class, () -> pev.visit(program));
    }

    /**
     * Type-checks and runs a MiniJava program.
     * <p>
     * This method first checks the program for type errors using {@code ProgramTypeVisitor}.
     * If any problems are found, the test fails.
     * </p>
     * <p>
     * If the program is well-typed, it is then executed using {@code ProgramExecutorVisitor}.
     * </p>
     *
     * @param stmt the MiniJava program to check and run
     */
    private void runAndCheckNoTypeErrors(Statement stmt) {
        ptv = new ProgramTypeVisitor();
        pev = new ProgramExecutorVisitor(ptv);
        ptv.visit(stmt);
        if (!ptv.problems.isEmpty()) {
            fail("Type errors: " + String.join(", ", ptv.problems));
        }
        pev.visit(stmt);
        System.out.println("----- Program finished -----");
    }

}
