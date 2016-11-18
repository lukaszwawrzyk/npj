package interpreter.structures;

public class TreeDataReference implements Reference {
    private final AllocableTree tree;

    public TreeDataReference(AllocableTree tree) {
        this.tree = tree;
    }

    @Override
    public int getValue() {
        return tree.getData();
    }

    @Override
    public Allocable withValue(int pointer) {
        return tree.withData(pointer);
    }

}
