package interpreter;

import ast.*;
import interpreter.structures.AllocalbeString;
import interpreter.structures.Tree;

import java.util.Iterator;

public class Interpreter {
    private final Output output;
    private final Iterator<Statement> statementsIterator;
    private final Variables variables;
    private final Heap heap;

    public Interpreter(Program program, Output output, Variables variables, Heap heap) {
        this.output = output;
        this.variables = variables;
        this.heap = heap;
        statementsIterator = program.getStatements().iterator();
    }

    public boolean hasNext() {
        return statementsIterator.hasNext();
    }

    public void runNext() {
        Statement statement = statementsIterator.next();
        if (statement instanceof PrintConst) {
            PrintConst printConst = (PrintConst) statement;
            output.printLine(printConst.getValue());
        } else if (statement instanceof TreeVarDecl) {
            TreeVarDecl treeVarDecl = (TreeVarDecl) statement;
            int pointer = heap.add(Tree.uninitialized());
            variables.put(treeVarDecl.getIdentifier().getValue(), pointer);
        } else if (statement instanceof StringVarDecl) {
            StringVarDecl stringVarDecl = (StringVarDecl) statement;
            int pointer = heap.add(stringVarDecl.getValue() == null ? null : AllocalbeString.of(stringVarDecl.getValue()));
            variables.put(stringVarDecl.getIdentifier().getValue(), pointer);
        } else if (statement instanceof PrintRef) {
            PrintRef printRef = (PrintRef) statement;
            int pointer = variables.get(printRef.getIdentifier().getValue());
            AllocalbeString string = (AllocalbeString) heap.get(pointer);
            output.printLine(string == null ? null : string.getValue());
        } else if (statement instanceof NullAssignment) {
            NullAssignment nullAssignment = (NullAssignment) statement;
            int nullPointer = 0;
            variables.put(nullAssignment.getTarget().getValue(), nullPointer);
        }
    }
}
