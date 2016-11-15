package interpreter;

import ast.*;
import interpreter.structures.Allocable;
import interpreter.structures.AllocableString;
import interpreter.structures.Tree;
import parsing.ParseException;

import java.util.List;

public class Interpreter {
    private final List<Statement> statements;
    private final Output output;
    private final Variables variables;
    private final Heap heap;

    public Interpreter(Program program, Output output, Variables variables, Heap heap) {
        this.output = output;
        this.variables = variables;
        this.heap = heap;
        statements = program.getStatements();
    }

    public void runProgram() {
        for (Statement statement : statements) {
            runStatement(statement);
        }
    }

    private void runStatement(Statement statement) {
        tryHandlePrints(statement);
        tryHandleDeclarations(statement);
        tryHandleAssignments(statement);
        tryHandleGC(statement);
    }

    private void tryHandlePrints(Statement statement) {
        if (statement instanceof PrintConst) {
            printConstant((PrintConst) statement);
        } else if (statement instanceof PrintRef) {
            printReference((PrintRef) statement);
        }
    }

    private void tryHandleDeclarations(Statement statement) {
        if (statement instanceof StringVarDecl) {
            declareStringVariable((StringVarDecl) statement);
        } else if (statement instanceof TreeVarDecl) {
            declareTreeVariable((TreeVarDecl) statement);
        }
    }

    private void tryHandleAssignments(Statement statement) {
        if (statement instanceof NullAssignment) {
            nullAssignment((NullAssignment) statement);
        } else if (statement instanceof StringConstAssignment) {
            stringConstantAssignment((StringConstAssignment) statement);
        } else if (statement instanceof RefAssignment) {
            referenceAssignment((RefAssignment) statement);
        } else if (statement instanceof IntConstAssignment) {
            intConstantAssignment((IntConstAssignment) statement);
        }
    }

    private void tryHandleGC(Statement statement) {
        if (statement instanceof HeapAnalyze) {
            heapAnalyze();
        } else if (statement instanceof Collect) {
            collect();
        }
    }

    // PRINTS

    private void printConstant(PrintConst printConst) {
        output.printLine(printConst.getValue());
    }

    private void printReference(PrintRef printRef) {
        String sourceVariableName = printRef.getIdentifier().getValue();
        int stringPointer = variables.get(sourceVariableName);
        AllocableString string = (AllocableString) heap.get(stringPointer);
        String stringValueOrNull = string == null ? null : string.getValue();
        output.printLine(stringValueOrNull);
    }

    // DECLARATIONS

    private void declareStringVariable(StringVarDecl stringVarDecl) {
        String stringValueOrNull = stringVarDecl.getValue();
        Allocable objectToAllocate = stringValueOrNull == null ? null : new AllocableString(stringValueOrNull);
        declareVariable(stringVarDecl, objectToAllocate);
    }

    private void declareTreeVariable(TreeVarDecl treeVarDecl) {
        Allocable objectToAllocate = Tree.uninitialized();
        declareVariable(treeVarDecl, objectToAllocate);
    }

    private void declareVariable(VarDecl declaration, Allocable objectToAllocate) {
        int allocatedObjectPointer = heap.add(objectToAllocate);
        String targetVariableName = declaration.getIdentifier().getValue();
        variables.put(targetVariableName, allocatedObjectPointer);
    }

    // ASSIGNMENTS

    private void nullAssignment(NullAssignment nullAssignment) {
        int nullPointer = 0;
        assignReference(nullAssignment, nullPointer);
    }

    private void stringConstantAssignment(StringConstAssignment stringConstAssignment) {
        int allocatedStringPointer = heap.add(new AllocableString(stringConstAssignment.getValue()));
        assignReference(stringConstAssignment, allocatedStringPointer);
    }

    private void referenceAssignment(RefAssignment refAssignment) {
        int pointerToAssign = resolvePointerToAssign(refAssignment);
        assignReference(refAssignment, pointerToAssign);
    }

    private void assignReference(Assignment assignment, int pointerToAssign) {
        Identifier targetIdentifier = assignment.getTarget();

        if (targetIdentifier.hasSingleComponent()) {
            String targetVariable = targetIdentifier.firstComponent();
            variables.put(targetVariable, pointerToAssign);
        } else {
            int pointerToTargetObject = resolvePointerToObjectThatNeedsUpdating(targetIdentifier);
            Tree updated = getUpdatedTreeWithPointer(pointerToAssign, targetIdentifier, pointerToTargetObject);
            heap.put(pointerToTargetObject, updated);
        }
    }

    private void intConstantAssignment(IntConstAssignment intConstAssignment) {
        Identifier targetIdentifier = intConstAssignment.getTarget();

        int pointerToTargetObject = resolvePointerToObjectThatNeedsUpdating(targetIdentifier);
        Tree updated = getUpdatedTreeWithData(intConstAssignment.getValue(), targetIdentifier, pointerToTargetObject);
        heap.put(pointerToTargetObject, updated);
    }

    private Tree getUpdatedTreeWithPointer(int pointerToAssign, Identifier targetIdentifier, int pointerToObjectToUpdate) {
        String lastComponent = targetIdentifier.lastComponent();
        if ("f1".equals(lastComponent)) {
            return ((Tree) heap.get(pointerToObjectToUpdate)).withLeftPointer(pointerToAssign);
        } else if ("f2".equals(lastComponent)) {
            return ((Tree) heap.get(pointerToObjectToUpdate)).withRightPointer(pointerToAssign);
        } else {
            throw new ParseException("Reference assignment to field of wrong type " + targetIdentifier);
        }
    }

    private Tree getUpdatedTreeWithData(int valueToAssign, Identifier targetIdentifier, int pointerToObjectToUpdate) {
        String lastComponent = targetIdentifier.lastComponent();
        if ("data".equals(lastComponent)) {
            return ((Tree) heap.get(pointerToObjectToUpdate)).withData(valueToAssign);
        } else {
            throw new ParseException("Int assignment to field of wrong type " + targetIdentifier);
        }
    }

    private int resolvePointerToObjectThatNeedsUpdating(Identifier reference) {
        List<String> components = reference.intermediateComponents();
        return navigateTreePointers(reference, components);
    }

    private int resolvePointerToAssign(RefAssignment refAssignment) {
        Identifier reference = refAssignment.getSource();
        List<String> components = reference.nestedComponents();
        return navigateTreePointers(reference, components);
    }

    private int navigateTreePointers(Identifier reference, List<String> components) {
        int pointer = variables.get(reference.firstComponent());
        for (String component : components) {
            if ("f1".equals(component)) {
                pointer = ((Tree) heap.get(pointer)).getLeftPointer();
            } else if ("f2".equals(component)) {
                pointer = ((Tree) heap.get(pointer)).getRightPointer();
            } else {
                throw new ParseException("Invalid dereference " + reference);
            }
        }
        return pointer;
    }

    // GC

    private void heapAnalyze() {
        heap.analyze();
    }

    private void collect() {
        heap.collect(variables);
    }
}
