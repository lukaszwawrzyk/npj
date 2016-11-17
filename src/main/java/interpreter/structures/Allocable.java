package interpreter.structures;

import interpreter.AnalysisResult;

public interface Allocable {
    void copyToHeap(int[] heap, int offset);

    int size();

    void appendToResult(AnalysisResult analysisResult);

    int getNumberOfReferences();

    Reference getReferenceAt(int index);
}
