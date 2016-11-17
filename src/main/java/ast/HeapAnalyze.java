package ast;

import interpreter.Heap;
import interpreter.Output;
import interpreter.Variables;

public class HeapAnalyze implements Statement {
    @Override
    public void run(Variables variables, Heap heap, Output output) {
        heap.analyze();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof HeapAnalyze;
    }

    @Override
    public int hashCode() {
        return 13;
    }

    @Override
    public String toString() {
        return "HeapAnalyze{}";
    }
}
