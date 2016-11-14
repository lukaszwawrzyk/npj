package ast;

public class IntConstAssignment implements Statement {
    private final Identifier target;
    private final int value;

    public IntConstAssignment(Identifier target, int value) {
        this.target = target;
        this.value = value;
    }

    public Identifier getTarget() {
        return target;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IntConstAssignment that = (IntConstAssignment) o;

        if (value != that.value) return false;
        return target.equals(that.target);

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
