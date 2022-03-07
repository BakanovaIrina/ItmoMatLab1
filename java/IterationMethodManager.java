import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.Scanner;

public class IterationMethodManager implements MethodManager{

    @Override
    public void manage() {
        Scanner in = new Scanner(System.in);
        System.out.println("Выберете, каким способом ввести данные: " +
                "\nСчитать из файла (1)" +
                "\nСчитать из терминала (2)" +
                "\nВыход (3)");
        String variation = in.nextLine();

        float[][] system;

        if(variation.equals("1")){
            system = readSystemFromFile(in);
        }
        else if (variation.equals("2")){
            system = readSystem(in);
        }
        else {
            return;
        }

        System.out.println("Введенная система: ");

        printSystem(system);

        IterationMethodCounter iterationMethodCounter = new IterationMethodCounter(system);

        System.out.println("Этап 1: Проверка преобладания диагональных элементов");

        if(iterationMethodCounter.diagonalTransformationCondition()){
            System.out.println("Условие соблюдено. Переставление элементов не нужно");
        }
        else {
            System.out.println("Условие не соблюдено. Происходит переставление элементов...");
            iterationMethodCounter.diagonalTransformation();
            System.out.println("Измененная система:");
            printSystem(iterationMethodCounter.getSystem());
        }

        System.out.println("Этап 2: Выражение элементов в каждой строке:");
        iterationMethodCounter.systemTransformation();
        System.out.println("Полученная система:");
        printChangedSystem(iterationMethodCounter.getSystem());

        System.out.println("Этап 3: Проверка нормы матрицы:");
        if(iterationMethodCounter.matrixNormCondition()){
            System.out.println("Норма матрицы удовлетворяет условиям");
        }
        else {
            System.out.println("Норма матрицы не удовлетворяет условиям. Система не может быть решена");
            return;
        }

        System.out.println("Этап 4: Выполнение расчетов");
        iterationMethodCounter.iterationProcess((float) 0.01);

        List result = iterationMethodCounter.getResults();
        printResult(result, system.length);

    }

    private float[][] readSystem(Scanner in){
        int count = 21;
        while (count > 20 || count < 2){
            System.out.println("Введите количество уравнений системы (не более 20):");
            count = Integer.parseInt(in.nextLine());
        }

        float[][] system = new float[count][count +1];

        System.out.println("Введите коэффициенты в уравнении по каждому запросу");

        for(int i = 1; i < count + 1; i++){
            for (int j = 1; j < count + 2; j++){
                System.out.println("Введите " + "a[" + i + "]" + "[" + j + "]");
                system[i - 1][j - 1] = Float.parseFloat(in.nextLine());
            }
        }

        return system;
    }

    private float[][] readSystemFromFile(Scanner in){
        float[][] system = null;
        System.out.println("Введите название файла:");
        String fileName = in.nextLine();
        try {
            int count = -1;
            Scanner fileScanner = new Scanner(new FileReader(new File(fileName)));
            if (fileScanner.hasNext()){
                count = Integer.parseInt(fileScanner.nextLine());
            }

            system = new float[count][count+1];

            for(int i = 0; i < count; i++){
                String[] parts = fileScanner.nextLine().split(" ");
                for (int j = 0; j < count + 1; j++){
                    system[i][j] = Float.parseFloat(parts[j]);
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("Проблемы с файлом");
        }
        return system;
    }

    private void printSystem(float[][] system){
        for (int i = 0; i < system.length; i++) {
            for (int j = 0; j < system[0].length; j++){
                if(j == system[0].length -1){
                    System.out.print(system[i][j]);
                }
                else {
                    System.out.print(system[i][j] + "x" + (j + 1));
                }
                if(j < system[0].length -2){
                    if (system[i][j + 1] >= 0){
                        System.out.print( " + ");
                    }
                    else {
                        System.out.print(" ");
                    }
                }
                else {
                    if(j == system[0].length -2)
                    System.out.print(" = ");
                }
            }
            System.out.println();
        }
    }

    private void printChangedSystem(float[][] system){
        for (int i = 0; i < system.length; i++) {
            System.out.print("x" + i + " = ");
            for (int j = 0; j < system[0].length; j++) {
                if (i == j || system[i][j] == 0) {
                    continue;
                }
                else {
                    if (system[i][j] > 0){
                        System.out.print(" + ");
                    }
                    if (j == system[0].length -1){
                        System.out.println(system[i][j]);
                    }
                    else {
                        System.out.print(system[i][j] + "x" + j);
                    }
                }
            }
        }
    }

    private void printResult(List result, int lengthSystem){
        for(int i = 0; i < result.size()/(lengthSystem + 1); i++){
            System.out.println("Итерация " + i + ":");
            for(int j = 0; j < lengthSystem; j ++){
                System.out.println("x" + j + " = " + result.get(i*(lengthSystem + 1) + j));
            }
            if(i > 0){
                System.out.println("Максимальное отклонение: " + result.get(i*(lengthSystem + 1) + 3));
            }
        }
    }
}
