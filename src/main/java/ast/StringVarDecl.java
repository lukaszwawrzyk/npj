package ast;

import interpreter.Heap;
import interpreter.Output;
import interpreter.Variables;
import interpreter.structures.Allocable;
import interpreter.structures.AllocableString;

public class StringVarDecl extends VarDecl {
    private final String value;

    public StringVarDecl(Identifier identifier, String value) {
        super(identifier);
        this.value = value;
    }

    @Override
    public void run(Variables variables, Heap heap, Output output) {
        Allocable objectToAllocate = getStringOrNull();
        declareVariable(objectToAllocate, variables, heap);
    }

    private Allocable getStringOrNull() {
        return value == null ? null : new AllocableString(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StringVarDecl that = (StringVarDecl) o;

        return target.equals(that.target) && (value != null ? value.equals(that.value) : that.value == null);
    }

    @Override
    public int hashCode() {
        int result = target.hashCode();
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "StringVarDecl{" +
                "target=" + target +
                ", value='" + value + '\'' +
                '}';
    }
}
