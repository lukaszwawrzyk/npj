package ast;

import interpreter.Heap;
import interpreter.Output;
import interpreter.Variables;

public class IntConstAssignment extends Assignment {
    private final int value;

    public IntConstAssignment(Identifier target, int value) {
        super(target);
        this.value = value;
    }

    @Override
    public void run(Variables variables, Heap heap, Output output) {
        assignValueToField(value, variables, heap);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IntConstAssignment that = (IntConstAssignment) o;

        return value == that.value && target.equals(that.target);
    }

    @Override
    public int hashCode() {
        return 31 * target.hashCode() + value;
    }

    @Override
    public String toString() {
        return "IntConstAssignment{" +
                "target=" + target +
                ", value=" + value +
                '}';
    }
}
