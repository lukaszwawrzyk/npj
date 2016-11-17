package interpreter;

import interpreter.structures.Allocable;
import interpreter.structures.AllocableString;
import interpreter.structures.Reference;
import interpreter.structures.Tree;

public class IntArrayHeap {
    private final int[] heap;
    private final Variables variables;
    private final int heapHalveLength;
    private int allocPtr;
    private int toSpace;
    private int fromSpace;

    public IntArrayHeap(int heapSize, Variables variables) {
        this.variables = variables;
        this.heap = new int[heapSize];
        this.heapHalveLength = (heapSize - 1) / 2;
        this.fromSpace = 1;
        this.toSpace = (heapSize-1) / 2 + 1;
        this.allocPtr = fromSpace;
    }

    public Allocable get(int pointer) {
        if (pointer == 0) return null;
        Tree tree = Tree.tryLoadFromHeap(heap, pointer);
        if (tree != null) return tree;
        AllocableString string = AllocableString.tryLoadFromHeap(heap, pointer);
        if (string != null) return string;

        throw new RuntimeException("Pointer access to invalid header " + pointer);
    }

    public int allocate(Allocable object) {
        if (object == null) {
            return 0;
        } else {
            if (allocPtr + object.size() > fromSpace + heapHalveLength) {
                collect();
                if (allocPtr + object.size() > fromSpace + heapHalveLength) {
                    throw new OutOfMemoryError();
                }
            }
            object.copyToHeap(heap, allocPtr);
            int allocatedObjectIndex = allocPtr;
            allocPtr += object.size();
            return allocatedObjectIndex;
        }
    }

    public void update(int pointer, Allocable object) {
        object.copyToHeap(heap, pointer);
    }

    public AnalysisResult analyze() {
        AnalysisResult analysisResult = new AnalysisResult();
        int ptr = fromSpace;
        while (ptr < allocPtr) {
            Allocable object = get(ptr);
            object.appendToResult(analysisResult);
            ptr += object.size();
        }
        return analysisResult;
    }

    public void collect() {
        int scanPtr = toSpace;
        allocPtr = toSpace;

        for (Variable root : variables.getAll()) {
            variables.put(root.getName(), copy(root.getPointer()));
        }

        while (scanPtr < allocPtr) {
            Allocable object = get(scanPtr);
            for (int i = 0; i < object.getNumberOfReferences(); i++) {
                Reference reference = object.getReferenceAt(i);
                int copiedObjectPointer = copy(reference.getValue());
                object = reference.withValue(copiedObjectPointer);
            }
            update(scanPtr, object);
            scanPtr += object.size();
        }

        int tmp = fromSpace;
        fromSpace = toSpace;
        toSpace = tmp;
    }

    private static final int FORWARD_POINTER = 0xfeedface;
    private int copy(int pointer) {
        if (pointer == 0) {
            return 0;
        }
        if (heap[pointer] == FORWARD_POINTER) {
            return heap[pointer + 1];
        }
        Allocable object = get(pointer);
        int destinationPointer = allocPtr;
        allocPtr += object.size();
        update(destinationPointer, object);
        heap[pointer] = FORWARD_POINTER;
        heap[pointer + 1] = destinationPointer;

        return destinationPointer;
    }
}
