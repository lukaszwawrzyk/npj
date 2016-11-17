package ast;

import interpreter.Heap;
import interpreter.Output;
import interpreter.Variables;

import java.util.List;

public class Program implements Statement {
    private final List<Statement> statements;

    public Program(List<Statement> statements) {
        this.statements = statements;
    }

    @Override
    public void run(Variables variables, Heap heap, Output output) {
        for (Statement statement : statements) {
            statement.run(variables, heap, output);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Program program = (Program) o;

        return statements.equals(program.statements);
    }

    @Override
    public int hashCode() {
        return statements.hashCode();
    }

    @Override
    public String toString() {
        return "Program{" +
                "statements=" + statements +
                '}';
    }
}
