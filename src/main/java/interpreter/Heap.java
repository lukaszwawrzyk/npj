package interpreter;

import interpreter.structures.Allocable;

public interface Heap {
    int add(Allocable object);
    Allocable get(int pointer);
}
