package dk.dtu.compute.course02324.mini_java.model;

import dk.dtu.compute.course02324.mini_java.infrastructure.VisitAcceptor;
import dk.dtu.compute.course02324.mini_java.semantics.ProgramVisitor;

public class IfThenElse implements Statement {
    private final Expression condition;
    private final Statement thenStatement;
    private final Statement elseStatement;

    public IfThenElse(Expression condition, Statement thenStatement, Statement elseStatement) {
        this.condition = condition;
        this.thenStatement = thenStatement;
        this.elseStatement = elseStatement;
    }

    public Expression getCondition() {
        return condition;
    }

    public Statement getThenStatement() {
        return thenStatement;
    }

    public Statement getElseStatement() {
        return elseStatement;
    }

    @Override
    public void accept(ProgramVisitor visitor) {
        visitor.visit(this);
    }
} 