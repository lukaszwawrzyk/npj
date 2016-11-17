package ast;

import interpreter.Heap;
import interpreter.Output;
import interpreter.Variables;

public class Collect implements Statement {
    @Override
    public void run(Variables variables, Heap heap, Output output) {
        heap.collect();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Collect;
    }

    @Override
    public int hashCode() {
        return 948;
    }

    @Override
    public String toString() {
        return "Collect{}";
    }
}
