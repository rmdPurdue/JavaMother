package ManualControl.Location;

public class SimpsonsRule {

    private static double f(double x, double aX, double bX, double cX,
                           double aY, double bY, double cY, double time) {
        return Math.sqrt(Math.pow((0.5 * aX * Math.pow(x,2) + (bX * x) + cX), 2) + Math.pow((0.5 * aY * Math.pow(x,2) + (bY * x) + cY), 2));
    }

    public static double integrate(double rangeLow, double rangeHigh, double aX, double bX, double cX,
                                   double aY, double bY, double cY, double time) {
        int N = 10000;  // precision parameter
        double h = (rangeHigh - rangeLow) / (N - 1);    // step size

        // 1/3 terms
        double sum = 1.0 / 3.0 * (f(rangeLow, aX, bX, cX, aY, bY, cY, time) + f(rangeHigh, aX, bX, cX, aY, bY, cY, time));

        // 4/3 terms
        for(int i = 1; i < N - 1; i += 2) {
            double x = rangeLow + h * i;
            sum += 4.0 / 3.0 * f(x, aX, bX, cX, aY, bY, cY, time);
        }

        // 2/3 terms
        for(int i = 2; i < N - 1; i += 2) {
            double x = rangeLow + h * i;
            sum += 2.0 / 3.0 * f(x, aX, bX, cX, aY, bY, cY, time);
        }

        return sum * h;
    }
}
