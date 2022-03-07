public interface IterationMethod {

    boolean diagonalTransformationCondition();
    void diagonalTransformation();
    void diagonalTransformationWithLines(int line1, int line2);
    void systemTransformation();
    boolean matrixNormCondition();
    void iterationProcess(float e);
}
