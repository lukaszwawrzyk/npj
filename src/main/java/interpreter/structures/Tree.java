package interpreter.structures;

public class Tree implements Allocable {
    public int leftPointer;
    public int rightPointer;
    public int data;

    public static Tree uninitialized() {
        return new Tree();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tree tree = (Tree) o;

        return leftPointer == tree.leftPointer &&
               rightPointer == tree.rightPointer &&
               data == tree.data;

    }

    @Override
    public int hashCode() {
        return 31 * (31 * leftPointer + rightPointer) + data;
    }
}
