package interpreter;

import ast.Program;

public class Interpreter {
    private final Program program;
    private final Output output;
    private final Variables variables;
    private final Heap heap;

    public Interpreter(Program program, Output output, Variables variables, Heap heap) {
        this.program = program;
        this.output = output;
        this.variables = variables;
        this.heap = heap;
    }

    public void runProgram() {
        program.run(variables, heap, output);
    }
}
