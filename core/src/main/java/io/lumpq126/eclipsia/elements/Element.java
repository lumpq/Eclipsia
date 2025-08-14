package io.lumpq126.eclipsia.elements;

import java.util.Arrays;
import java.util.Locale;

/**
 * Element 열거형: 게임 내 속성 시스템을 관리
 * 관계 매트릭스를 사용하여 각 Element 간 상성 관계를 정의
 */
public enum Element {

    NORMAL,
    FIRE,
    WATER,
    EARTH,
    WIND,
    POISON,
    LIGHT,
    DARKNESS,
    ELECTRIC,
    ICE,
    METAL,
    PLANTS,
    ROT,
    SHADOW,
    ANGEL,
    DEVIL;

    // -----------------------------
    // 관계 상수 정의
    // -----------------------------
    public static final int NONE = 0;               // 관계 없음
    public static final int GENERAL = 1;            // 일반적인 관계
    public static final int WEAKNESS = 2;           // 약점
    public static final int ULTIMATE_WEAKNESS = 3;  // 극약점
    public static final int STRENGTH = 4;           // 강점
    public static final int ULTIMATE_STRENGTH = 5;  // 극강점
    public static final int MUTUAL = 10;            // 상호관계 (특수관계)

    // -----------------------------
    // 관계 매트릭스
    // [from.ordinal()][to.ordinal()] = 관계 값
    // -----------------------------
    private static final int SIZE = values().length;
    private static final int[][] relationMatrix = new int[SIZE][SIZE];

    // -----------------------------
    // 관계 설정 및 조회
    // -----------------------------

    /**
     * 두 Element 간의 관계를 설정
     * MUTUAL일 경우 자동으로 양방향 동일하게 설정
     */
    public static void setRelation(Element from, Element to, int relation) {
        relationMatrix[from.ordinal()][to.ordinal()] = relation;
        if (relation == MUTUAL) {
            relationMatrix[to.ordinal()][from.ordinal()] = MUTUAL;
        }
    }

    /**
     * 두 Element 간의 관계 조회
     */
    public static int getRelation(Element from, Element to) {
        return relationMatrix[from.ordinal()][to.ordinal()];
    }

    // -----------------------------
    // 전투 유틸리티
    // -----------------------------

    /**
     * 두 속성 간의 상성을 기반으로 데미지 배율 반환
     * 필요에 따라 배율값은 게임 밸런스에 맞게 조정
     */
    public static double getDamageMultiplier(Element from, Element to) {
        int relation = getRelation(from, to);
        return switch (relation) {
            case WEAKNESS -> 1.5;
            case ULTIMATE_WEAKNESS, MUTUAL -> 2.0;
            case STRENGTH -> 0.75;
            case ULTIMATE_STRENGTH -> 0.5;
            default -> 1.0; // NONE, GENERAL 등
        };
    }

    // -----------------------------
    // 유틸리티
    // -----------------------------

    /**
     * 저장된 이름(예: "FIRE")으로 안전하게 Element 파싱. 없으면 null 반환
     */
    public static Element fromName(String name) {
        if (name == null) return null;
        try {
            return Element.valueOf(name.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    /**
     * 관계 초기화: 모든 관계를 NONE(0)으로 설정
     */
    public static void clearRelations() {
        for (int[] row : relationMatrix) {
            Arrays.fill(row, NONE);
        }
    }

    /**
     * 저장/전송 시 쓸 키 값 (enum 이름)
     */
    public static String getKey(Element element) {
        return element.name();
    }

    @Override
    public String toString() {
        return name();
    }
}
