package kr.lumpq126.eclipsia.utilities.calc;

public class FishGradeCalculator {

    private static final String[] GRADES = {
            "F", "E-", "E", "E+",
            "D-", "D", "D+",
            "C-", "C", "C+",
            "B-", "B", "B+",
            "A-", "A", "A+",
            "S", "S+", "SS+", "SSS"
    };

    private static final String[] COLORS = {
            "<gray>", "<gray>", "<gray>", "<gray>",
            "<white>", "<white>", "<white>",
            "<yellow>", "<yellow>", "<yellow>",
            "<green>", "<green>", "<green>",
            "<red>", "<red>", "<red>",
            "<dark_red>", "<dark_red>", "<gold>", "<gold>"
    };

    private static final double[] THRESHOLDS = {
            0.01, 0.03, 0.07, 0.10,
            0.14, 0.20, 0.26,
            0.33, 0.41, 0.49,
            0.57, 0.65, 0.73,
            0.80, 0.87, 0.92,
            0.95, 0.97, 0.985, 1.0
    };

    public static String getGrade(double length, double minLength, double maxLength,
                                  double weight, double minWeight, double maxWeight) {
        double lengthRatio = clamp((length - minLength) / (maxLength - minLength));
        double weightRatio = clamp((weight - minWeight) / (maxWeight - minWeight));
        double averageRatio = (lengthRatio + weightRatio) / 2.0;

        for (int i = 0; i < THRESHOLDS.length; i++) {
            if (averageRatio <= THRESHOLDS[i]) {
                return COLORS[i] + GRADES[i];
            }
        }

        return COLORS[COLORS.length - 1] + GRADES[GRADES.length - 1];
    }

    private static double clamp(double value) {
        if (value < 0.0) return 0.0;
        return Math.min(value, 1.0);
    }
}

