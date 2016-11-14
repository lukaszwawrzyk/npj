package interpreter;

public interface Variables {
    void put(String identifier, int pointer);
    int get(String identifier);
}