package io.snowyblossom126.eclipsia.utilities.calc;

/**
 * 물고기의 길이와 무게를 기반으로 등급을 계산하는 유틸리티 클래스입니다.
 * <p>
 * 길이와 무게를 기준으로 비율을 계산하고, 사전에 정의된 임계값(THRESHOLDS)을 통해
 * 등급(GRADE)과 색상(COLOR)을 결정합니다.
 */
public class FishGradeCalculator {

    /** 등급 이름 배열 */
    private static final String[] GRADES = {
            "F", "E-", "E", "E+",
            "D-", "D", "D+",
            "C-", "C", "C+",
            "B-", "B", "B+",
            "A-", "A", "A+",
            "S", "S+", "SS+", "SSS"
    };

    /** 등급 색상 배열 */
    private static final String[] COLORS = {
            "<gray>", "<gray>", "<gray>", "<gray>",
            "<white>", "<white>", "<white>",
            "<yellow>", "<yellow>", "<yellow>",
            "<green>", "<green>", "<green>",
            "<red>", "<red>", "<red>",
            "<dark_red>", "<dark_red>", "<gold>", "<gold>"
    };

    /** 등급 임계값 배열 */
    private static final double[] THRESHOLDS = {
            0.01, 0.03, 0.07, 0.10,
            0.14, 0.20, 0.26,
            0.33, 0.41, 0.49,
            0.57, 0.65, 0.73,
            0.80, 0.87, 0.92,
            0.95, 0.97, 0.985, 1.0
    };

    /**
     * 길이와 무게를 기반으로 물고기의 등급을 계산합니다.
     *
     * @param length    실제 길이
     * @param minLength 최소 길이
     * @param maxLength 최대 길이
     * @param weight    실제 무게
     * @param minWeight 최소 무게
     * @param maxWeight 최대 무게
     * @return 등급 문자열(색상 포함)
     */
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

    /**
     * 값이 0.0~1.0 범위를 벗어나지 않도록 제한합니다.
     *
     * @param value 입력 값
     * @return 0.0~1.0 범위로 클램프된 값
     */
    private static double clamp(double value) {
        if (value < 0.0) return 0.0;
        return Math.min(value, 1.0);
    }
}
