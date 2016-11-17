package interpreter;

import java.util.List;

public interface Variables {
    void put(String identifier, int pointer);
    int get(String identifier);
    List<Variable> getAll();
}

