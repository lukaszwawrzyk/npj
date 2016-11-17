package interpreter.structures;

import interpreter.AnalysisResult;

public class Tree implements Allocable {
    private static final int HEADER = 0xcafebabe;

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

    public static Tree tryLoadFromHeap(int[] heap, int pointer) {
        if (heap[pointer] == HEADER) {
            int leftPointer = heap[pointer + 1];
            int rightPointer = heap[pointer + 2];
            int data = heap[pointer + 3];
            return new Tree(leftPointer, rightPointer, data);
        } else {
            return null;
        }
    }

    @Override
    public void copyToHeap(int[] heap, int pointer) {
        heap[pointer] = HEADER;
        heap[pointer + 1] = leftPointer;
        heap[pointer + 2] = rightPointer;
        heap[pointer + 3] = data;
    }

    @Override
    public int size() {
        return 4;
    }

    @Override
    public void appendToResult(AnalysisResult analysisResult) {
        analysisResult.getTreeData().add(data);
    }

    @Override
    public int getNumberOfReferences() {
        return 2;
    }

    @Override
    public Reference getReferenceAt(int index) {
        switch (index) {
            case 0:
                return new TreeLeftPointerReference(this);
            case 1:
                return new TreeRightPointerReference(this);
            default:
                throw new IndexOutOfBoundsException();
        }
    }


    public int getLeftPointer() {
        return leftPointer;
    }

    public int getRightPointer() {
        return rightPointer;
    }

    public int getData() {
        return data;
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
