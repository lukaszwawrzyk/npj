package interpreter.structures;

public class TreeRightPointerReference implements Reference {
    private final Tree tree;

    public TreeRightPointerReference(Tree tree) {
        this.tree = tree;
    }

    @Override
    public int getValue() {
        return tree.getRightPointer();
    }

    @Override
    public Allocable withValue(int pointer) {
        return tree.withRightPointer(pointer);
    }

}
