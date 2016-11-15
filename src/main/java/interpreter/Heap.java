package interpreter;

import interpreter.structures.Allocable;

public interface Heap {
    Allocable get(int pointer);
    int add(Allocable object);
    void put(int pointer, Allocable object);
}
