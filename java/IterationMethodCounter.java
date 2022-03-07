import java.util.ArrayList;
import java.util.List;

public class IterationMethodCounter implements IterationMethod {

    private float[][] system;
    private List<Float> results;

    public IterationMethodCounter(float[][] system) {
        this.system = system;
        results = new ArrayList<>();
    }

    private boolean checkMass(){
        return system.length + 1 == system[0].length;
    }

    @Override
    public boolean diagonalTransformationCondition() {
        if(checkMass()){
            for (int i = 0; i < system.length; i++){
                float sum = 0;
                for (int j = 0; j < system[0].length -1; j++){
                    if(i == j){
                        continue;
                    }
                    sum = module(system[i][j]) + sum;
                }
                if (module(system[i][i]) < sum ){
                    return false;
                }
            }
            return true;
        }
        else {
            System.out.println("Проверьте введенные данные!");
            return false;
        }
    }

    private float module(float x){
        if(x >= 0){
            return x;
        }
        else {
            return -x;
        }
    }

    public void diagonalTransformation(){
        int maxElementLine = 0;
        for (int j = 0; j < system[0].length - 1; j++) {
            for (int i = 0; i < system.length; i++) {
                if(system[maxElementLine][j] > system[i][j]){
                    System.out.println("MaxEl = " + maxElementLine + " i = " + i);
                    diagonalTransformationWithLines(maxElementLine, i);
                    maxElementLine = i;
                }
            }
        }
        for (int i = 0; i < system.length; i++) {
            for (int j = 0; j < system[0].length -1; j++) {
                System.out.print(system[i][j] + " ");
            }
            System.out.println();
        }
    }

    @Override
    public void diagonalTransformationWithLines(int line1, int line2) {
        for (int j = 0; j < system[0].length; j++) {
            float t = system[line1][j];
            system[line1][j] = system[line2][j];
            system[line2][j] = t;
        }
    }

    @Override
    public void systemTransformation() {
        for (int i = 0; i < system.length; i++) {
            float cur = system[i][i];
            float last = system[i][system[0].length - 1];
            for (int j = 0; j < system[0].length; j++) {
                    system[i][j] = -system[i][j] / cur;
            }
            system[i][i] = 0;
            system[i][system[0].length - 1] = -system[i][system[0].length - 1];
        }
    }

    @Override
    public boolean matrixNormCondition() {
        float max = -1;
        for (int i = 0; i < system.length; i++) {
            float sum = 0;
            for (int j = 0; j < system[0].length - 1; j++) {
                sum = sum + module(system[i][j]);
            }
            if (sum > max){
                max = sum;
            }
        }
        return max < 1;
    }

    @Override
    public void iterationProcess(float e) {
        float maxDeviation = -1;

        float result[] = new float[system.length];
        float d[] = new float[system.length];

        for (int r = 0; r < system.length; r ++){
            results.add(system[r][system[0].length - 1]);
            result[r] = system[r][system[0].length - 1];
            d[r] = result[r];
        }

        results.add(maxDeviation);

        int iteration = 1;
        float resultNext[] = new float[system.length];
        boolean a = true;
        while (a){
                for (int i = 0; i < system.length; i++) {
                    for (int j = 0; j < system[0].length - 1; j++) {
                        resultNext[i] = system[i][j]*result[j] + resultNext[i];
                    }
                    resultNext[i] = resultNext[i] + d[i];
                    results.add(resultNext[i]);
                }

            maxDeviation = -1;
            for (int i = 0; i < system.length; i++){
                if(module(result[i] - resultNext[i]) > maxDeviation){
                        maxDeviation = module(result[i] - resultNext[i]);
                }
            }
            results.add(maxDeviation);

            for (int i = 0; i < system.length; i++){
                result[i] = resultNext[i];
                resultNext[i] = 0;
            }
                if (maxDeviation < e){
                    a = false;
                }
                iteration++;
        }
    }

    public float[][] getSystem() {
        return system;
    }

    public List<Float> getResults() {
        return results;
    }
}
