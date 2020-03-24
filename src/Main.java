import java.util.Random;
import java.util.Scanner;

public class Main {

    /* Вероятность брака */
    private static final double P = 0.1;
    /* Первый лимит брака */
    private static final int firstDefectLimit = 20;
    /* Второй лимит брака */
    private static final int secondDefectLimit = 10;
    /* Интенсивность поступления деталей */
    private static final double intensive = 2.0;
    /* Проверяем каждую третью деталь */
    private static int checkEvery = 3;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("Введите количество повторений для получения оценки:");
        int n = in.nextInt();
        double time = start(n);
        System.out.printf("Среднее время наработки станка до остановки - %.0f Минут, %.0f Секунд \n", Math.floor(time), time % 1 * 100);
        System.out.println("В среднем станок останавливается после обработки " + time * intensive + " деталей");
    }

    private static double start(int n) {
        Random r = new Random();
        /* Общее время работы станка */
        double time = 0.0;
        for (int i = 0; i < n; i++) {
            /* Счетчик обработанных деталей */
            int partsCounter = 0;
            /* Счетчик бракованных деталей */
            int defectCounter = 0;
            do {
                /* Цикл от 0 до количества деталей в потоке поступления подчиненому закону Пуассона */
                for (int poisson = 0; poisson < getPoissonRandom(); poisson++) {
                    /* Увеличиваем значение счетчика обработанных деталей */
                    partsCounter++;
                    /* Если количество брака достигло первого лимита, то начинаем проверять каждую деталь */
                    if (defectCounter == firstDefectLimit) {
                        checkEvery = 1;
                    }
                    /* Проверяем деталь на брак */
                    if (partsCounter % checkEvery == 0) {
                        /* Генерируем Xj для определения того, бракованная деталь или нет */
                        double chance = Math.floor(r.nextDouble() * 10) / 10;
                        if (chance == P) {
                            /* Если деталь бракованна, то увеличиваем счетчик брака */
                            defectCounter++;
                        }
                    }
                    /* Если количество брака достигло обоих лимитов, то приостанавливаем работу станка */
                    if (defectCounter == firstDefectLimit + secondDefectLimit) {
                        /* Считаем затраченное время (количество обработанных деталей / интенсивность их поступления) */
                        time += partsCounter / intensive;
                        break;
                    }
                }
            } while (defectCounter != firstDefectLimit + secondDefectLimit);
        }
        /* Возвращаем среднее время наработки станка до остановки */
        return time / n;
    }

    /* Пуассоновское распределение */
    private static int getPoissonRandom() {
        Random r = new Random();
        /* Натуральный логарифм по основанию e и интенсивности поступления деталей */
        double L = Math.exp(-Main.intensive);
        int k = 0;
        double p = 1.0;
        do {
            /* r.nextDouble() - генерируем равномерно распределенное случайное число */
            p = p * r.nextDouble();
            k++;
        } while (p > L);
        return k - 1;
    }
}