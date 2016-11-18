package interpreter.structures;

public class TreeRightPointerReference implements Reference {
    private final AllocableTree tree;

    public TreeRightPointerReference(AllocableTree tree) {
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
