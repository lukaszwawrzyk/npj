package interpreter.structures;

public class TreeRightPointerReference implements Reference {
    private final Tree tree;

    public TreeRightPointerReference(Tree tree) {
        this.tree = tree;
    }

    @Override
    public int getPointer() {
        return tree.getRightPointer();
    }

    @Override
    public Allocable setPointer(int pointer) {
        return tree.withRightPointer(pointer);
    }

}
