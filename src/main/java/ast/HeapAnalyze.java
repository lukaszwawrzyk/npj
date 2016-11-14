package ast;

public class HeapAnalyze implements Statement {
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
