import interpreter.IntArrayHeap;

import java.util.Map;

public class NPJCollectorAdapter implements Collector {
    private final IntArrayHeap intArrayHeap;

    public NPJCollectorAdapter(IntArrayHeap intArrayHeap) {
        this.intArrayHeap = intArrayHeap;
    }

    @Override
    public void collect(int[] heap, Map<Object, Object> params) {
        intArrayHeap.collect();
    }
}
