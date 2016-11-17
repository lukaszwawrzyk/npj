package interpreter.structures;

public interface Reference {
    int getValue();
    Allocable withValue(int pointer);
}
