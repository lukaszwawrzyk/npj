package interpreter;

import java.util.Map;

public interface Variables {
    void put(String identifier, int pointer);
    int get(String identifier);
    Map<Object, Object> asMap();
}