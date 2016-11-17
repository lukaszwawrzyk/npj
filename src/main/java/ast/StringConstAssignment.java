package ast;

import interpreter.Heap;
import interpreter.Output;
import interpreter.Variables;
import interpreter.structures.AllocableString;

public class StringConstAssignment extends Assignment {
    private final String value;

    public StringConstAssignment(Identifier target, String value) {
        super(target);
        this.value = value;
    }

    @Override
    public void run(Variables variables, Heap heap, Output output) {
        int allocatedStringPointer = allocateString(heap);
        assignValueToField(allocatedStringPointer, variables, heap);
    }

    private int allocateString(Heap heap) {
        return heap.add(new AllocableString(value));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StringConstAssignment that = (StringConstAssignment) o;

        return target.equals(that.target) && value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return 31 * target.hashCode() + value.hashCode();
    }

    @Override
    public String toString() {
        return "StringConstAssignment{" +
                "target=" + target +
                ", value='" + value + '\'' +
                '}';
    }
}
