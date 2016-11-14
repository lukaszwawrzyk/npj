package ast;

public class Collect implements Statement {
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
