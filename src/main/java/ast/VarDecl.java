package ast;

import interpreter.Heap;
import interpreter.Variables;
import interpreter.structures.Allocable;

public abstract class VarDecl implements Statement {
    protected final Identifier target;

    protected VarDecl(Identifier target) {
        this.target = target;
    }

    protected void declareVariable(Allocable objectToAllocate, Variables variables, Heap heap) {
        int allocatedObjectPointer = heap.add(objectToAllocate);
        String targetVariableName = target.getValue();
        variables.put(targetVariableName, allocatedObjectPointer);
    }
}
