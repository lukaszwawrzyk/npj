import ast.Program;
import interpreter.IntArrayHeap;
import interpreter.MapVariables;
import parsing.ParseResult;
import parsing.Parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.StringJoiner;

public class Interpreter {
    private final String programFile;
    private final int heapSize;

    public Interpreter(String programFile, int heapSize) {
        this.programFile = programFile;
        this.heapSize = heapSize;
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            handleError("Usage: Interpreter <program-file>");
        }

        String programFile = args[0];
        int heapSize = Integer.getInteger("npj.heap.size");

        Interpreter interpreter = new Interpreter(programFile, heapSize);
        interpreter.tryRunProgram();
    }

    private static void handleError(String x) {
        System.out.println(x);
        System.exit(1);
    }

    private void tryRunProgram() {
        try {
            runProgram();
        } catch (Throwable e) {
            handleError(errorMessageForException(e));
        }
    }

    private String errorMessageForException(Throwable e) {
        return "Error: " + e.getClass().getSimpleName() + " " + e.getMessage();
    }

    private void runProgram() throws IOException {
        String code = readProgramToString();
        Program program = parseProgram(code);
        interpreter.Interpreter interpreter = createInterpreter(program);
        interpreter.runProgram();
    }

    private String readProgramToString() throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(programFile));
        return joinLines(lines);
    }

    private Program parseProgram(String code) {
        Parser parser = new Parser();
        ParseResult parseResult = parser.parse(code);
        if (!parseResult.isSuccessful()) {
            handleError("Parsing failed " + parseResult.getErrorMessage());
        }

        return parseResult.getParsedResult();
    }

    private interpreter.Interpreter createInterpreter(Program program) {
        NPJOutput output = new NPJOutput();
        MapVariables variables = new MapVariables();
        IntArrayHeap intHeap = new IntArrayHeap(heapSize, variables);
        NPJHeap heap = new NPJHeap(intHeap);
        return new interpreter.Interpreter(program, output, variables, heap);
    }

    private String joinLines(List<String> lines) {
        StringJoiner joiner = new StringJoiner("\n");
        for (String line : lines) {
            joiner.add(line);
        }
        return joiner.toString();
    }

}
