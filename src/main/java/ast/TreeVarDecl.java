package ast;

import interpreter.Heap;
import interpreter.Output;
import interpreter.Variables;
import interpreter.structures.Allocable;
import interpreter.structures.Tree;

public class TreeVarDecl extends VarDecl {
    public TreeVarDecl(Identifier identifier) {
        super(identifier);
    }

    @Override
    public void run(Variables variables, Heap heap, Output output) {
        Allocable objectToAllocate = Tree.uninitialized();
        declareVariable(objectToAllocate, variables, heap);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TreeVarDecl that = (TreeVarDecl) o;

        return target.equals(that.target);
    }

    @Override
    public int hashCode() {
        return target.hashCode();
    }

    @Override
    public String toString() {
        return "TreeVarDecl{" +
                "target=" + target +
                '}';
    }
}
