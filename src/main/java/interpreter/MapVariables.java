package interpreter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapVariables implements Variables {
    private final Map<String, Integer> variables;

    public MapVariables() {
        this.variables = new HashMap<String, Integer>();
    }

    @Override public void put(String identifier, int pointer) {
        variables.put(identifier, pointer);
    }

    @Override public int get(String identifier) {
        return variables.get(identifier);
    }

    @Override
    public List<Variable> getAll() {
        List<Variable> list = new ArrayList<Variable>();
        for (Map.Entry<String, Integer> pair : variables.entrySet()) {
            Variable variable = new Variable(pair.getKey(), pair.getValue());
            list.add(variable);
        }
        return list;
    }
}
