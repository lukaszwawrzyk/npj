import interpreter.CopyingGC;

import java.util.Map;

public class NPJCollector implements Collector {
    private final CopyingGC collector;

    public NPJCollector(CopyingGC collector) {
        this.collector = collector;
    }

    @Override
    public void collect(int[] heap, Map<Object, Object> params) {
        collector.collect(heap, params);
    }
}
