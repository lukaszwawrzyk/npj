package ast;

import interpreter.Heap;
import interpreter.Output;
import interpreter.Variables;
import interpreter.structures.AllocableString;

public class PrintRef implements Statement {
    private final Identifier identifier;

    public PrintRef(Identifier identifier) {
        this.identifier = identifier;
    }

    @Override
    public void run(Variables variables, Heap heap, Output output) {
        AllocableString string = getStringFromHeap(variables, heap);
        String stringValueOrNull = getStringToPrint(string);
        output.printLine(stringValueOrNull);
    }

    private AllocableString getStringFromHeap(Variables variables, Heap heap) {
        String sourceVariableName = identifier.getValue();
        int stringPointer = variables.get(sourceVariableName);
        return (AllocableString) heap.get(stringPointer);
    }

    private String getStringToPrint(AllocableString string) {
        return string == null ? null : string.getValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PrintRef printRef = (PrintRef) o;

        return identifier.equals(printRef.identifier);
    }

    @Override
    public int hashCode() {
        return identifier.hashCode();
    }

    @Override
    public String toString() {
        return "PrintRef{" +
                "target=" + identifier +
                '}';
    }
}
