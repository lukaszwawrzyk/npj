package interpreter.structures;

public class TreeLeftPointerReference implements Reference {
    private final AllocableTree tree;

    public TreeLeftPointerReference(AllocableTree tree) {
        this.tree = tree;
    }

    @Override
    public int getValue() {
        return tree.getLeftPointer();
    }

    @Override
    public Allocable withValue(int pointer) {
        return tree.withLeftPointer(pointer);
    }

}
