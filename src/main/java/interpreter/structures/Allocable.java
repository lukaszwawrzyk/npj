package interpreter.structures;

import interpreter.AnalysisResult;

public interface Allocable {
    void putInto(int[] heap, int offset);

    int size();

    void appendToResult(AnalysisResult analysisResult);

    int getNumberOfReferences();
    Reference getReference(int index);
}
