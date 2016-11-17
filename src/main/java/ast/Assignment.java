package ast;

import interpreter.Heap;
import interpreter.Variables;
import interpreter.structures.Allocable;
import interpreter.structures.Reference;
import parsing.ParseException;

import java.util.List;

public abstract class Assignment implements Statement {
    protected final Identifier target;

    public Assignment(Identifier target) {
        this.target = target;
    }

    protected void assignValueToField(int valueToAssign, Variables variables, Heap heap) {
        if (target.hasSingleComponent()) {
            assignToVariableRoot(valueToAssign, variables);
        } else {
            assignToNestedObject(valueToAssign, variables, heap);
        }
    }

    private void assignToVariableRoot(int valueToAssign, Variables variables) {
        String targetVariable = target.firstComponent();
        variables.put(targetVariable, valueToAssign);
    }

    private void assignToNestedObject(int valueToAssign, Variables variables, Heap heap) {
        int pointerToTargetObject = resolvePointerToObjectThatNeedsUpdating(variables, heap);
        Allocable updated = getUpdatedTreeWithPointer(valueToAssign, target, pointerToTargetObject, heap);
        heap.put(pointerToTargetObject, updated);
    }

    private int resolvePointerToObjectThatNeedsUpdating(Variables variables, Heap heap) {
        List<String> components = target.intermediateComponents();
        return navigateTreePointers(target, components, variables, heap);
    }

    private Allocable getUpdatedTreeWithPointer(int valueToAssign, Identifier target, int pointerToObjectToUpdate, Heap heap) {
        String lastComponent = target.lastComponent();
        Reference reference = getReference(heap, pointerToObjectToUpdate, lastComponent);
        return reference.withValue(valueToAssign);
    }

    protected int navigateTreePointers(Identifier referenceIdentifier, List<String> components, Variables variables, Heap heap) {
        int pointer = variables.get(referenceIdentifier.firstComponent());
        for (String component : components) {
            pointer = getReference(heap, pointer, component).getValue();
        }
        return pointer;
    }

    private Reference getReference(Heap heap, int pointer, String component) {
        Reference reference = heap.get(pointer).getReferenceToFieldNamed(component);
        if (reference == null) throw new ParseException("Invalid reference: " + component);
        return reference;
    }
}
