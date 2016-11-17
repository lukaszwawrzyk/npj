package interpreter.structures;

public interface Reference {
    int getPointer();
    Allocable setPointer(int pointer);
}
