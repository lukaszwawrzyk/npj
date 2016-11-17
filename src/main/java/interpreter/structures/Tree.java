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

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tree tree = (Tree) o;

        return leftPointer == tree.leftPointer &&
               rightPointer == tree.rightPointer &&
               data == tree.data;
    }

    @Override public int hashCode() {
        return 31 * (31 * leftPointer + rightPointer) + data;
    }

    @Override public String toString() {
        return "Tree{" +
                "leftPointer=" + leftPointer +
                ", rightPointer=" + rightPointer +
                ", data=" + data +
                '}';
    }

    @Override public void putInto(int[] heap, int offset) {
        heap[offset] = HEADER;
        heap[offset + 1] = leftPointer;
        heap[offset + 2] = rightPointer;
        heap[offset + 3] = data;
    }

    @Override public int size() {
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
    public Reference getReference(int index) {
        switch (index) {
            case 0:
                return new Reference() {
                    @Override
                    public int getPointer() {
                        return leftPointer;
                    }

                    @Override
                    public Allocable setPointer(int pointer) {
                        return withLeftPointer(pointer);
                    }
                };
            case 1:
                return new Reference() {
                    @Override
                    public int getPointer() {
                        return rightPointer;
                    }

                    @Override
                    public Allocable setPointer(int pointer) {
                        return withRightPointer(pointer);
                    }
                };
            default:
                throw new IndexOutOfBoundsException();
        }
    }

    public static Tree tryLoadFromHeap(int[] heap, int offset) {
        if (heap[offset] == HEADER) {
            int leftPointer = heap[offset + 1];
            int rightPointer = heap[offset + 2];
            int data = heap[offset + 3];
            return new Tree(leftPointer, rightPointer, data);
        } else {
            return null;
        }
    }
}
