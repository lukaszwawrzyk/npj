package ast;

import interpreter.Heap;
import interpreter.Output;
import interpreter.Variables;

import java.util.List;

public class RefAssignment extends Assignment {
    private final Identifier source;

    public RefAssignment(Identifier target, Identifier sourceReference) {
        super(target);
        this.source = sourceReference;
    }

    @Override
    public void run(Variables variables, Heap heap, Output output) {
        int pointerToAssign = resolvePointerToAssign(variables, heap);
        assignValueToField(pointerToAssign, variables, heap);
    }

    private int resolvePointerToAssign(Variables variables, Heap heap) {
        List<String> components = source.nestedComponents();
        return navigateTreePointers(source, components, variables, heap);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RefAssignment that = (RefAssignment) o;

        return target.equals(that.target) &&
                source.equals(that.source);
    }

    @Override
    public int hashCode() {
        return 31 * target.hashCode() + source.hashCode();
    }

    @Override
    public String toString() {
        return "RefAssignment{" +
                "target=" + target +
                ", source=" + source +
                '}';
    }
}
