package interpreter.structures;

public class Tree implements Allocable {
    private final int leftPointer;
    private final int rightPointer;
    private final int data;

    private Tree() {
        leftPointer = 0;
        rightPointer = 0;
        data = 0;
    }

    public Tree(int leftPointer, int rightPointer, int data) {
        this.leftPointer = leftPointer;
        this.rightPointer = rightPointer;
        this.data = data;
    }

    public static Tree uninitialized() {
        return new Tree();
    }

    public int getLeftPointer() {
        return leftPointer;
    }

    public int getRightPointer() {
        return rightPointer;
    }

    public Tree withLeftPointer(int leftPointer) {
        return new Tree(leftPointer, rightPointer, data);
    }

    public Tree withRightPointer(int rightPointer) {
        return new Tree(leftPointer, rightPointer, data);
    }

    public Tree withData(int data) {
        return new Tree(leftPointer, rightPointer, data);
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

    @Override
    public String toString() {
        return "Tree{" +
                "leftPointer=" + leftPointer +
                ", rightPointer=" + rightPointer +
                ", data=" + data +
                '}';
    }
}
