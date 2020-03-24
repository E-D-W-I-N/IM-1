import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public class Main {

    /* Количество повторений */
    private static final int n = 2;
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
        System.out.println(start());
    }

    private static double start() {
        Random r = new Random();
        /* Общее время работы станка */
        double time = 0.0;
        for (int i = n; i > 0; i--) {
            /* Счетчик обработанных деталей */
            int partsCounter = 0;
            /* Счетчик бракованных деталей */
            int defectCounter = 0;
            while (true) {
                for (int poisson = getPoissonRandom(); poisson > 0; poisson--) {
                    /* Генерируем Xj для определения того, бракованная деталь или нет */
                    double chance = BigDecimal.valueOf(r.nextDouble()).setScale(1, RoundingMode.DOWN).doubleValue();
                    /* Увеличиваем значение счетчика обработанных деталей */
                    partsCounter++;
                    /* Если количество брака достигло первого лимита, то начинаем проверять каждую деталь */
                    if (defectCounter == firstDefectLimit) {
                        checkEvery = 1;
                    }
                    /* Проверяем деталь на брак */
                    if (partsCounter % checkEvery == 0) {
                        if (chance == P) {
                            /* Если деталь бракованна, то увеличиваем счетчик брака */
                            defectCounter++;
                        }
                    }
                }
                /* Если количество брака достигло обоих лимитов, то приостанавливаем работу станка */
                if (defectCounter == firstDefectLimit + secondDefectLimit) {
                    System.out.println(partsCounter);
                    /* Считаем затраченное время (количество обработанных деталей / интенсивность их поступления) */
                    time += partsCounter / 2.0;
                    System.out.println(time);
                    break;
                }
            }
        }
        /* Возвращаем среднее время работы станка до его отключения */
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