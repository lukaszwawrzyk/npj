package interpreter.structures;

import interpreter.AnalysisResult;

public class AllocableTree implements Allocable {
    private static final int HEADER = 0xcafebabe;

    private final int leftPointer;
    private final int rightPointer;
    private final int data;

    private AllocableTree() {
        leftPointer = 0;
        rightPointer = 0;
        data = 0;
    }

    public AllocableTree(int leftPointer, int rightPointer, int data) {
        this.leftPointer = leftPointer;
        this.rightPointer = rightPointer;
        this.data = data;
    }

    public static AllocableTree uninitialized() {
        return new AllocableTree();
    }

    public static AllocableTree tryLoadFromHeap(int[] heap, int pointer) {
        if (heap[pointer] == HEADER) {
            int leftPointer = heap[pointer + 1];
            int rightPointer = heap[pointer + 2];
            int data = heap[pointer + 3];
            return new AllocableTree(leftPointer, rightPointer, data);
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
            case 2:
                return new TreeDataReference(this);
            default:
                throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public Reference getReferenceToFieldNamed(String name) {
        switch (name) {
            case "f1":
                return getReferenceAt(0);
            case "f2":
                return getReferenceAt(1);
            case "data":
                return getReferenceAt(2);
            default:
                throw new IllegalArgumentException(name);
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

    public AllocableTree withLeftPointer(int leftPointer) {
        return new AllocableTree(leftPointer, rightPointer, data);
    }

    public AllocableTree withRightPointer(int rightPointer) {
        return new AllocableTree(leftPointer, rightPointer, data);
    }

    public AllocableTree withData(int data) {
        return new AllocableTree(leftPointer, rightPointer, data);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AllocableTree tree = (AllocableTree) o;

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
        return "AllocableTree{" +
                "leftPointer=" + leftPointer +
                ", rightPointer=" + rightPointer +
                ", data=" + data +
                '}';
    }
}
