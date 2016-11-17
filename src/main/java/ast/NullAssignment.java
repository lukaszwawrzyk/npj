package ast;

import interpreter.Heap;
import interpreter.Output;
import interpreter.Variables;

public class NullAssignment extends Assignment {
    private static final int NULL_POINTER = 0;

    public NullAssignment(Identifier target) {
        super(target);
    }


    @Override
    public void run(Variables variables, Heap heap, Output output) {
        assignValueToField(NULL_POINTER, variables, heap);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NullAssignment that = (NullAssignment) o;

        return target.equals(that.target);
    }

    @Override
    public int hashCode() {
        return target.hashCode();
    }

    @Override
    public String toString() {
        return "NullAssignment{" +
                "target=" + target +
                '}';
    }
}
