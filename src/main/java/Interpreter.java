import ast.Program;
import interpreter.IntArrayHeap;
import interpreter.MapVariables;
import parsing.ParseResult;
import parsing.Parser;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.StringJoiner;

public class Interpreter {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Wrong number of arguments - pass program file name");
            System.exit(1);
        }
        String programFile = args[0];
        int heapSize = Integer.getInteger("npj.heap.size");

        Interpreter interpreter = new Interpreter(programFile, heapSize);

        interpreter.runProgram();
    }

    private String programFile;
    private int heapSize;

    public Interpreter(String programFile, int heapSize) {
        this.programFile = programFile;
        this.heapSize = heapSize;
    }

    private void runProgram() {
        try {
            List<String> lines = Files.readAllLines(Paths.get(programFile));
            StringJoiner joiner = new StringJoiner("\n");
            for (String line : lines) {
                joiner.add(line);
            }
            String code = joiner.toString();

            Parser parser = new Parser();
            ParseResult parseResult = parser.parse(code);
            if (!parseResult.isSuccessful()) {
                System.out.println("Parsing failed " + parseResult.getErrorMessage());
                System.exit(3);
            }

            Program program = parseResult.getParsedResult();

            NPJOutput output = new NPJOutput();
            MapVariables variables = new MapVariables();
            IntArrayHeap intHeap = new IntArrayHeap(heapSize, variables);
            NPJHeap heap = new NPJHeap(intHeap);
            interpreter.Interpreter interpreter = new interpreter.Interpreter(program, output, variables, heap);
            interpreter.runProgram();
        } catch (Exception e) {
            System.out.println("Error: " + e.getClass().getSimpleName() + " " + e.getMessage());
            System.exit(3);
        }
    }

}
