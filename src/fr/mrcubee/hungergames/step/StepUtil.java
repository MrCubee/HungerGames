package fr.mrcubee.hungergames.step;

public class StepUtil {

    private static String longToString(long number) {
        if (number > -1 && number < 10)
            return "0" + number;
        return Long.toString(number);
    }

    public static String secondToString(long seconds) {
        long s = seconds;
        long m = 0;
        long h = 0;

        if (seconds <= 0)
            return "00:00:00";
        if (s >= 60) {
            m = s / 60;
            s = s - (m * 60);
        }
        if (m >= 60) {
            h = m / 60;
            m = m - (h * 60);
        }
        return (longToString(h) + ":" + longToString(m) + ":" + longToString(s));
    }

}
