package ast;

public class RefAssignment implements Statement {
    private final Identifier target;
    private final Identifier source;

    public RefAssignment(Identifier target, Identifier sourceReference) {
        this.target = target;
        this.source = sourceReference;
    }

    public Identifier getTarget() {
        return target;
    }

    public Identifier getSource() {
        return source;
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
