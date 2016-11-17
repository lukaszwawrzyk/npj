package interpreter.structures;

public class TreeDataReference implements Reference {
    private final Tree tree;

    public TreeDataReference(Tree tree) {
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
