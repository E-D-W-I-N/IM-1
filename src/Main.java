import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public class Main {

    private static final int n = 2;
    private static final double P = 0.1;
    private static final int firstDefectLimit = 20;
    private static final int secondDefectLimit = 10;
    private static final double intensive = 2.0;
    private static int checkEvery = 3;


    public static void main(String[] args) {
        System.out.println(start() / n);
    }

    private static double start() {
        Random r = new Random();
        double time = 0.0;
        for (int i = n; i > 0; i--) {
            int defectCounter = 0;
            int partsCounter = 0;
            while (true) {
                for (int poisson = getPoissonRandom(); poisson > 0; poisson--) {
                    double chance = BigDecimal.valueOf(r.nextDouble()).setScale(1, RoundingMode.DOWN).doubleValue();
                    partsCounter++;
                    if (defectCounter == firstDefectLimit) {
                        checkEvery = 1;
                    }
                    if (partsCounter % checkEvery == 0) {
                        if (chance == P) {
                            defectCounter++;
                        }
                    }
                }
                if (defectCounter == firstDefectLimit + secondDefectLimit) {
                    System.out.println(partsCounter);
                    time += partsCounter / 2.0;
                    System.out.println(time);
                    break;
                }
            }
        }
        return time;
    }

    private static int getPoissonRandom() {
        Random r = new Random();
        double L = Math.exp(-Main.intensive);
        int k = 0;
        double p = 1.0;
        do {
            p = p * r.nextDouble();
            k++;
        } while (p > L);
        return k - 1;
    }
}