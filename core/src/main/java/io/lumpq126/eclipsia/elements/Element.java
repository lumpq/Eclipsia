package io.lumpq126.eclipsia.elements;

import java.util.Arrays;
import java.util.Locale;

/**
 * 게임 내 속성(Element) 시스템을 관리하는 열거형(enum) 클래스.
 * 각 Element 간 상성 관계를 관계 매트릭스를 사용해 정의하며,
 * 전투에서 데미지 계산 및 관계 조회 기능을 제공합니다.
 * 기본값은 항상 {@link #NORMAL}입니다.
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

    /** 일반적인 관계 */
    public static final int GENERAL = 1;

    /** 약점 관계 */
    public static final int WEAKNESS = 2;

    /** 극약점 관계 */
    public static final int ULTIMATE_WEAKNESS = 3;

    /** 강점 관계 */
    public static final int STRENGTH = 4;

    /** 극강점 관계 */
    public static final int ULTIMATE_STRENGTH = 5;

    /** 상호관계(특수관계) */
    public static final int MUTUAL = 10;

    // -----------------------------
    // 관계 매트릭스
    // -----------------------------

    /** Element 개수 */
    private static final int SIZE = values().length;

    /** 각 Element 간 관계를 저장하는 매트릭스 [from.ordinal()][to.ordinal()] */
    private static final int[][] relationMatrix = new int[SIZE][SIZE];

    // -----------------------------
    // 기본값 관련
    // -----------------------------

    /**
     * 기본 Element를 반환합니다.
     *
     * @return 기본 Element ({@link #NORMAL})
     */
    public static Element getDefault() {
        return NORMAL;
    }

    /**
     * 문자열 이름으로부터 안전하게 Element를 반환합니다.
     * 존재하지 않으면 기본값 {@link #NORMAL} 반환.
     *
     * @param name Element 이름 문자열 (예: "FIRE")
     * @return 해당 Element 또는 기본값 {@link #NORMAL}
     */
    public static Element fromNameOrDefault(String name) {
        Element e = fromName(name);
        return e != null ? e : getDefault();
    }

    // -----------------------------
    // 관계 설정 및 조회
    // -----------------------------

    /**
     * 두 Element 간의 관계를 설정합니다.
     * <p>
     * MUTUAL 관계인 경우 자동으로 양방향 동일하게 설정됩니다.
     *
     * @param from     관계를 시작하는 Element
     * @param to       관계를 받을 Element
     * @param relation 관계 값(GENERAL, WEAKNESS, STRENGTH 등)
     */
    public static void setRelation(Element from, Element to, int relation) {
        relationMatrix[from.ordinal()][to.ordinal()] = relation;
        if (relation == MUTUAL) {
            relationMatrix[to.ordinal()][from.ordinal()] = MUTUAL;
        }
    }

    /**
     * 두 Element 간의 관계를 조회합니다.
     * 관계가 설정되지 않았다면 기본값(NORMAL 기준)으로 처리됩니다.
     *
     * @param from 시작 Element
     * @param to   대상 Element
     * @return 관계 값(int)
     */
    public static int getRelation(Element from, Element to) {
        int relation = relationMatrix[from.ordinal()][to.ordinal()];
        // 기본값 처리: 초기화되지 않았다면 NORMAL 기준
        return relation != 0 ? relation : NORMAL.ordinal();
    }

    // -----------------------------
    // 전투 유틸리티
    // -----------------------------

    /**
     * 두 Element 간의 상성을 기반으로 데미지 배율을 계산합니다.
     * 배율은 게임 밸런스에 맞게 조정 가능합니다.
     *
     * @param from 공격 Element
     * @param to   방어 Element
     * @return 데미지 배율(double)
     */
    public static double getDamageMultiplier(Element from, Element to) {
        int relation = getRelation(from, to);
        return switch (relation) {
            case WEAKNESS -> 1.5;
            case ULTIMATE_WEAKNESS, MUTUAL -> 2.0;
            case STRENGTH -> 0.75;
            case ULTIMATE_STRENGTH -> 0.5;
            default -> 1.0; // NORMAL 기준
        };
    }

    // -----------------------------
    // 유틸리티
    // -----------------------------

    /**
     * 저장된 이름(예: "FIRE")으로 안전하게 Element 파싱. 없으면 null 반환
     *
     * @param name Element 이름
     * @return Element 또는 null
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
     * 관계 초기화: 모든 관계를 기본값(NORMAL.ordinal())으로 설정
     */
    public static void clearRelations() {
        for (int[] row : relationMatrix) {
            Arrays.fill(row, NORMAL.ordinal());
        }
    }

    /**
     * 저장 또는 전송 시 사용할 키 값을 반환합니다.
     *
     * @param element Element 객체
     * @return Element 이름(String)
     */
    public static String getKey(Element element) {
        return element.name();
    }

    /**
     * Element 이름을 문자열로 반환합니다.
     *
     * @return Element 이름(String)
     */
    @Override
    public String toString() {
        return name();
    }
}
