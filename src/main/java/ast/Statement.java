package ast;

import interpreter.Heap;
import interpreter.Output;
import interpreter.Variables;

public interface Statement {
    void run(Variables variables, Heap heap, Output output);
}
