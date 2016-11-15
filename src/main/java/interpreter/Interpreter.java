package interpreter;

import ast.*;
import interpreter.structures.Allocable;
import interpreter.structures.AllocableString;
import interpreter.structures.Tree;

import java.util.Iterator;
import java.util.List;

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
            int pointer = heap.add(stringVarDecl.getValue() == null ? null : new AllocableString(stringVarDecl.getValue()));
            variables.put(stringVarDecl.getIdentifier().getValue(), pointer);
        } else if (statement instanceof PrintRef) {
            PrintRef printRef = (PrintRef) statement;
            int pointer = variables.get(printRef.getIdentifier().getValue());
            AllocableString string = (AllocableString) heap.get(pointer);
            output.printLine(string == null ? null : string.getValue());
        } else if (statement instanceof NullAssignment) {
            NullAssignment nullAssignment = (NullAssignment) statement;
            int nullPointer = 0;
            variables.put(nullAssignment.getTarget().getValue(), nullPointer);
        } else if (statement instanceof IntConstAssignment) {
            IntConstAssignment intConstAssignment = (IntConstAssignment) statement;
            Identifier reference = intConstAssignment.getTarget();
            List<String> components = reference.getComponents();
            List<String> intermediateComponents = components.subList(1, components.size() - 1);
            int objectToPutIntoPointer = variables.get(reference.getValue());
            Tree objectToPutInto = (Tree) heap.get(objectToPutIntoPointer);
            for (String component : intermediateComponents) {
                if ("f1".equals(component)) {
                    objectToPutIntoPointer = objectToPutInto.getLeftPointer();
                    objectToPutInto = (Tree) heap.get(objectToPutIntoPointer);
                } else if ("f2".equals(component)) {
                    objectToPutIntoPointer = objectToPutInto.getRightPointer();
                    objectToPutInto = (Tree) heap.get(objectToPutIntoPointer);
                } else {
                    // fixme
                    throw new OutOfMemoryError(":(");
                }
            }
            String lastComponent = components.get(components.size() - 1);
            Tree updated;
            if ("data".equals(lastComponent)) {
                updated = objectToPutInto.withData(intConstAssignment.getValue());
            else {
                throw new OutOfMemoryError(":S");
            }
            heap.put(objectToPutIntoPointer, updated);
        } else if (statement instanceof StringConstAssignment) {
            StringConstAssignment stringConstAssignment = (StringConstAssignment) statement;
            Identifier identifier = stringConstAssignment.getTarget();
            int allocatedStringPointer = heap.add(new AllocableString(stringConstAssignment.getValue()));
            variables.put(identifier.getValue(), allocatedStringPointer);
        } else if (statement instanceof RefAssignment) {
            RefAssignment refAssignment = (RefAssignment) statement;
            int refToAssign = getRefToAssign(refAssignment);

            Identifier identifier = refAssignment.getTarget();
            List<String> components = identifier.getComponents();

            String targetVariable = components.get(0);
            if (components.size() == 1) {
                variables.put(targetVariable, refToAssign);
            } else {
                List<String> intermediateComponents = components.subList(1, components.size() - 1);
                int objectToPutIntoPointer = variables.get(targetVariable);
                Tree objectToPutInto = (Tree) heap.get(objectToPutIntoPointer);
                for (String component : intermediateComponents) {
                    if ("f1".equals(component)) {
                        objectToPutIntoPointer = objectToPutInto.getLeftPointer();
                        objectToPutInto = (Tree) heap.get(objectToPutIntoPointer);
                    } else if ("f2".equals(component)) {
                        objectToPutIntoPointer = objectToPutInto.getRightPointer();
                        objectToPutInto = (Tree) heap.get(objectToPutIntoPointer);
                    } else {
                        // fixme
                        throw new OutOfMemoryError(":(");
                    }
                }
                String lastComponent = components.get(components.size() - 1);
                Tree updated;
                if ("f1".equals(lastComponent)) {
                    updated = objectToPutInto.withLeftPointer(refToAssign);
                } else if ("f2".equals(lastComponent)) {
                    updated = objectToPutInto.withRightPointer(refToAssign);
                } else {
                    // fixme
                    throw new OutOfMemoryError(":S");
                }
                heap.put(objectToPutIntoPointer, updated);

            }























        }
    }

    private int getRefToAssign(RefAssignment refAssignment) {
        Identifier identifier = refAssignment.getSource();
        List<String> components = identifier.getComponents();
        List<String> nestedComponents = components.subList(1, components.size());


        int refToAssign = variables.get(components.get(0));
        Allocable currentObject = heap.get(refToAssign);
        for (String component : nestedComponents) {
            if ("f1".equals(component)) {
                Tree tree = (Tree) currentObject;
                refToAssign = tree.getLeftPointer();
                currentObject = heap.get(refToAssign);
            } else if ("f2".equals(component)) {
                Tree tree = (Tree) currentObject;
                refToAssign = tree.getRightPointer();
                currentObject = heap.get(refToAssign);
            } else {
                // fixme
                throw new OutOfMemoryError("asd");
            }
        }
        return refToAssign;
    }
}
