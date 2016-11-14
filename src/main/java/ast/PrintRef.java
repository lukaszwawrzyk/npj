package ast;

public class PrintRef implements Statement {
    private final Identifier identifier;

    public PrintRef(Identifier identifier) {
        this.identifier = identifier;
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PrintRef printRef = (PrintRef) o;

        return identifier.equals(printRef.identifier);

    }

    @Override
    public int hashCode() {
        return identifier.hashCode();
    }

    @Override
    public String toString() {
        return "PrintRef{" +
                "identifier=" + identifier +
                '}';
    }
}
