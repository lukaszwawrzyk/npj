package ast;

public class StringVarDecl implements Statement {
    private final Identifier identifier;
    private final String value;

    public StringVarDecl(Identifier identifier, String value) {
        this.identifier = identifier;
        this.value = value;
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StringVarDecl that = (StringVarDecl) o;

        if (!identifier.equals(that.identifier)) return false;
        return value != null ? value.equals(that.value) : that.value == null;

    }

    @Override
    public int hashCode() {
        int result = identifier.hashCode();
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "StringVarDecl{" +
                "identifier=" + identifier +
                ", value='" + value + '\'' +
                '}';
    }
}
