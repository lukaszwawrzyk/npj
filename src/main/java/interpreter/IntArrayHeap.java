package interpreter;

import interpreter.structures.Allocable;
import interpreter.structures.AllocableString;
import interpreter.structures.AllocableTree;
import interpreter.structures.Reference;

public class IntArrayHeap {
    private static final int FORWARD_POINTER = 0xfeedface;
    public static final int NULL_POINTER = 0;
    private final int[] heap;
    private final Variables variables;
    private final int heapHalveLength;
    private int freeMemoryStart;
    private int destinationSpaceStart;
    private int sourceSpaceStart;

    public IntArrayHeap(int heapSize, Variables variables) {
        this.variables = variables;
        this.heap = new int[heapSize];
        this.heapHalveLength = (heapSize - 1) / 2;
        this.sourceSpaceStart = 1;
        this.destinationSpaceStart = (heapSize - 1) / 2 + 1;
        this.freeMemoryStart = sourceSpaceStart;
    }


    public int allocate(Allocable object) {
        if (object == null) {
            return NULL_POINTER;
        } else {
            ensureSpaceToAllocate(object);
            int allocatedObjectPointer = freeMemoryStart;
            object.copyToHeap(heap, allocatedObjectPointer);
            advanceFreeMemoryPointer(object);
            return allocatedObjectPointer;
        }
    }

    private void ensureSpaceToAllocate(Allocable object) {
        if (noMoreMemoryToAllocate(object)) {
            collect();
            if (noMoreMemoryToAllocate(object)) {
                throw new OutOfMemoryError();
            }
        }
    }

    private boolean noMoreMemoryToAllocate(Allocable object) {
        int lastWordIndexAfterAllocation = freeMemoryStart + object.size();
        int endOfCurrentSpace = sourceSpaceStart + heapHalveLength;
        return lastWordIndexAfterAllocation > endOfCurrentSpace;
    }

    public Allocable get(int pointer) {
        if (pointer == 0) return null;
        AllocableTree tree = AllocableTree.tryLoadFromHeap(heap, pointer);
        if (tree != null) return tree;
        AllocableString string = AllocableString.tryLoadFromHeap(heap, pointer);
        if (string != null) return string;

        throw new RuntimeException("Pointer access to invalid header " + pointer);
    }

    public void put(int pointer, Allocable object) {
        object.copyToHeap(heap, pointer);
    }

    public void collect() {
        setDestinationSpaceAsAllocationTarget();
        copyRootObjects();
        copyReferencedObjects();
        swapSpaces();
    }

    private void setDestinationSpaceAsAllocationTarget() {
        freeMemoryStart = destinationSpaceStart;
    }

    private void copyRootObjects() {
        for (Variable root : variables.getAll()) {
            int copiedObjectPointer = copy(root.getPointer());
            variables.put(root.getName(), copiedObjectPointer);
        }
    }

    private void copyReferencedObjects() {
        int currentObjectPointer = destinationSpaceStart;
        while (currentObjectPointer < freeMemoryStart) {
            Allocable updatedObject = copyAllReferencedObjectsAndUpdatePointers(currentObjectPointer);
            put(currentObjectPointer, updatedObject);
            currentObjectPointer += updatedObject.size();
        }
    }

    private Allocable copyAllReferencedObjectsAndUpdatePointers(int objectToUpdatePointer) {
        Allocable object = get(objectToUpdatePointer);
        for (int i = 0; i < object.getNumberOfReferences(); i++) {
            Reference reference = object.getReferenceAt(i);
            int copiedObjectPointer = copy(reference.getValue());
            object = reference.withValue(copiedObjectPointer);
        }
        return object;
    }

    private void swapSpaces() {
        int temporary = sourceSpaceStart;
        sourceSpaceStart = destinationSpaceStart;
        destinationSpaceStart = temporary;
    }

    private int copy(int pointer) {
        if (pointer == 0) return 0;
        if (heap[pointer] == FORWARD_POINTER) return heap[pointer + 1];

        Allocable object = get(pointer);
        int destinationPointer = freeMemoryStart;
        put(destinationPointer, object);
        putForwardPointer(pointer, destinationPointer);

        advanceFreeMemoryPointer(object);
        return destinationPointer;
    }

    private void advanceFreeMemoryPointer(Allocable object) {
        freeMemoryStart += object.size();
    }

    private void putForwardPointer(int oldLocation, int newLocation) {
        heap[oldLocation] = FORWARD_POINTER;
        heap[oldLocation + 1] = newLocation;
    }

    public AnalysisResult analyze() {
        AnalysisResult analysisResult = new AnalysisResult();
        int currentObjectPointer = sourceSpaceStart;
        while (currentObjectPointer < freeMemoryStart) {
            Allocable object = get(currentObjectPointer);
            object.appendToResult(analysisResult);
            currentObjectPointer += object.size();
        }
        return analysisResult;
    }
}
