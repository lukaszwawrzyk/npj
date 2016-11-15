package interpreter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MapVariables implements Variables {
    private final Map<String, Integer> variables;

    public MapVariables() {
        this.variables = new HashMap<>();
    }

    @Override public void put(String identifier, int pointer) {
        variables.put(identifier, pointer);
    }

    @Override public int get(String identifier) {
        return variables.get(identifier);
    }

    @Override
    public Map<Object, Object> asMap() {
        return Collections.unmodifiableMap(variables);
    }
}
