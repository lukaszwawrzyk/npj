package interpreter.structures;

import interpreter.AnalysisResult;

public class AllocableString implements Allocable {
    private static final int HEADER = 0xdeadbeef;

    private final String value;

    public AllocableString(String value) {
        this.value = value;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AllocableString that = (AllocableString) o;

        return value != null ? value.equals(that.value) : that.value == null;

    }

    public String getValue() {
        return value;
    }

    @Override public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }

    @Override public String toString() {
        return "AllocableString{" +
                "value='" + value + '\'' +
                '}';
    }

    @Override public void putInto(int[] heap, int offset) {
        heap[offset] = HEADER;
        heap[offset + 1] = value.length();
        for (int i = 0; i < value.length(); i++) {
            heap[i + offset + 2] = value.charAt(i);
        }
    }

    @Override public int size() {
        return 2 + value.length();
    }

    @Override
    public void appendToResult(AnalysisResult analysisResult) {
        analysisResult.getStrings().add(value);
    }

    @Override
    public int getNumberOfReferences() {
        return 0;
    }

    @Override
    public Reference getReference(int index) {
        throw new IndexOutOfBoundsException();
    }

    public static AllocableString tryLoadFromHeap(int[] heap, int offset) {
        if (heap[offset] == HEADER) {
            int stringSize = heap[offset + 1];
            char[] characters = new char[stringSize];
            for (int i = 0; i < stringSize; i++) {
                characters[i] = (char) heap[i + offset + 2];
            }
            String string = new String(characters);
            return new AllocableString(string);
        } else {
            return null;
        }
    }
}
