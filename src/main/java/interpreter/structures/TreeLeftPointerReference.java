package interpreter.structures;

public class TreeLeftPointerReference implements Reference {
    private final Tree tree;

    public TreeLeftPointerReference(Tree tree) {
        this.tree = tree;
    }

    @Override
    public int getPointer() {
        return tree.getLeftPointer();
    }

    @Override
    public Allocable setPointer(int pointer) {
        return tree.withLeftPointer(pointer);
    }

}
