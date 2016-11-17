import interpreter.AnalysisResult;
import interpreter.Heap;
import interpreter.IntArrayHeap;
import interpreter.structures.Allocable;

public class NPJHeap implements Heap {
    private final IntArrayHeap heap;

    public NPJHeap(IntArrayHeap heap) {
        this.heap = heap;
    }

    @Override
    public Allocable get(int pointer) {
        return heap.get(pointer);
    }

    @Override
    public int add(Allocable object) {
        return heap.allocate(object);
    }

    @Override
    public void put(int pointer, Allocable object) {
        heap.update(pointer, object);
    }

    @Override
    public void analyze() {
        AnalysisResult result = heap.analyze();
        NPJ.heapAnalyze(result.getTreeData(), result.getStrings());
    }

    @Override
    public void collect() {
        NPJ.collect(null, new NPJCollectorAdapter(heap), null);
    }
}
