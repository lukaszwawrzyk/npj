package ast;

public class NullAssignment implements Statement, Assignment {
    private final Identifier target;

    public NullAssignment(Identifier target) {
        this.target = target;
    }

    public Identifier getTarget() {
        return target;
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
