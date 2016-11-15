import interpreter.AnalysisResult;
import interpreter.Heap;
import interpreter.IntArrayHeap;
import interpreter.Variables;
import interpreter.structures.Allocable;

public class NPJHeap implements Heap {
    private final IntArrayHeap heap;
    private final Collector collector;

    public NPJHeap(IntArrayHeap heap, Collector collector) {
        this.heap = heap;
        this.collector = collector;
    }

    @Override
    public Allocable get(int pointer) {
        return heap.get(pointer);
    }

    @Override
    public int add(Allocable object) {
        return heap.add(object);
    }

    @Override
    public void put(int pointer, Allocable object) {
        heap.put(pointer, object);
    }

    @Override
    public void analyze() {
        AnalysisResult result = heap.analyze();
        NPJ.heapAnalyze(result.getTreeData(), result.getStrings());
    }

    @Override
    public void collect(Variables variables) {
        NPJ.collect(heap.getRawHeap(), collector, variables.asMap());
    }
}
