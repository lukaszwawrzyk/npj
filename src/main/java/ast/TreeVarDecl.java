package ast;

public class TreeVarDecl implements Statement, VarDecl {
    private final Identifier identifier;

    public TreeVarDecl(Identifier identifier) {
        this.identifier = identifier;
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TreeVarDecl that = (TreeVarDecl) o;

        return identifier.equals(that.identifier);

    }

    @Override
    public int hashCode() {
        return identifier.hashCode();
    }

    @Override
    public String toString() {
        return "TreeVarDecl{" +
                "identifier=" + identifier +
                '}';
    }
}
