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
     * Tests a while loop that runs exactly 3 times.
     * Each time it increments the count variable.
     * Final output should be: count = 3
     */
    @Test
    public void testWhileLoopRunsFixedTimes() {
        Statement program = Sequence(
                Declaration(INT, Var("i"), Literal(0)),
                Declaration(INT, Var("count"), Literal(0)),
                WhileLoop(
                        OperatorExpression(MINUS2, Literal(3), Var("i")), // while (i < 3)
                        Sequence(
                                Assignment(Var("count"),
                                        OperatorExpression(PLUS2, Var("count"), Literal(1))
                                ),
                                Assignment(Var("i"),
                                        OperatorExpression(PLUS2, Var("i"), Literal(1))
                                )
                        )
                ),
                PrintStatement("count = ", Var("count"))
        );

        runAndCheckNoTypeErrors(program);
    }

    /**
     * Tests a while loop where the condition is false from the start.
     * The loop body should never execute.
     * Final output should be: sum = 0
     */
    @Test
    public void testWhileLoopNeverRuns() {
        Statement program = Sequence(
                Declaration(INT, Var("i"), Literal(5)),
                Declaration(INT, Var("sum"), Literal(0)),
                WhileLoop(
                        OperatorExpression(MINUS2, Literal(3), Var("i")), // while (5 < 3) → false
                        Assignment(Var("sum"),
                                OperatorExpression(PLUS2, Var("sum"), Var("i"))
                        )
                ),
                PrintStatement("sum = ", Var("sum"))
        );

        runAndCheckNoTypeErrors(program);
    }

    /**
     * Tests a while loop that counts down from 3 to 0.
     * Each time it adds the value of i to sum.
     * Final output should be: sum = 6 (3 + 2 + 1)
     */
    @Test
    public void testWhileLoopDecrementStops() {
        Statement program = Sequence(
                Declaration(INT, Var("i"), Literal(3)),
                Declaration(INT, Var("sum"), Literal(0)),
                WhileLoop(
                        Var("i"), // while i != 0 (interpreted as i >= 0)
                        Sequence(
                                Assignment(Var("sum"),
                                        OperatorExpression(PLUS2, Var("sum"), Var("i"))
                                ),
                                Assignment(Var("i"),
                                        OperatorExpression(MINUS2, Var("i"), Literal(1))
                                )
                        )
                ),
                PrintStatement("sum = ", Var("sum"))
        );

        runAndCheckNoTypeErrors(program);
    }

    /**
     * Tests a conditional where the condition is >= 0 (true).
     * The 'then' branch should run and set res = 10.
     * Final output should be: res = 10
     */
    @Test
    public void testIfThenBranchExecutes() {
        Statement program = Sequence(
                Declaration(INT, Var("x"), Literal(2)),
                Declaration(INT, Var("res"), Literal(0)),
                new IfThenElse(
                        Var("x"),  // >= 0 → true
                        Assignment(Var("res"), Literal(10)),
                        Assignment(Var("res"), Literal(20))
                ),
                PrintStatement("res = ", Var("res"))
        );
        runAndCheckNoTypeErrors(program);
    }

    /**
     * Tests a conditional where the condition is < 0 (false).
     * The 'else' branch should run and set res = 20.
     * Final output should be: res = 20
     */
    @Test
    public void testIfElseBranchExecutes() {
        Statement program = Sequence(
                Declaration(INT, Var("x"), Literal(-1)),
                Declaration(INT, Var("res"), Literal(0)),
                new IfThenElse(
                        Var("x"),  // < 0 → false
                        Assignment(Var("res"), Literal(10)),
                        Assignment(Var("res"), Literal(20))
                ),
                PrintStatement("res = ", Var("res"))
        );
        runAndCheckNoTypeErrors(program);
    }

    /**
     * Tests a conditional with a float condition instead of an int.
     * This should trigger a type-checking error.
     */
    @Test
    public void testIfConditionTypeError() {
        Statement program = new IfThenElse(
                new FloatLiteral(1.5f),
                Assignment(Var("res"), Literal(1)),
                Assignment(Var("res"), Literal(0))
        );
        ptv.visit(program);
        assertFalse(ptv.problems.isEmpty(), "Expected type error for non-integer condition");
    }

    /**
     * Type-checks and executes a MiniJava program.
     * <p>
     * First, this method uses {@code ProgramTypeVisitor} to check for type errors.
     * If the program has any type issues, the test will fail and show the problems.
     * </p>
     * <p>
     * If type-checking passes, it then uses {@code ProgramExecutorVisitor} to run
     * the program and print the result (if there are print statements).
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
