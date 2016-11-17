package ast;

public class StringConstAssignment implements Statement, Assignment {
    private final Identifier target;
    private final String value;

    public StringConstAssignment(Identifier target, String value) {
        this.target = target;
        this.value = value;
    }

    public Identifier getTarget() {
        return target;
    }

    public String getValue() {
        return value;
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
