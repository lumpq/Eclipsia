package io.lumpq126.eclipsia.elements;

import java.util.*;

/**
 * Element 클래스: 게임 내 속성 시스템을 관리
 * 관계 매트릭스를 사용하여 각 Element 간 상성 관계를 정의
 */
public class Element {

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
    // 모든 Element 정의
    // -----------------------------
    public static final Element NORMAL = new Element("NORMAL", 0);
    public static final Element FIRE = new Element("FIRE", 1);
    public static final Element WATER = new Element("WATER", 2);
    public static final Element EARTH = new Element("EARTH", 3);
    public static final Element WIND = new Element("WIND", 4);
    public static final Element POISON = new Element("POISON", 5);
    public static final Element LIGHT = new Element("LIGHT", 6);
    public static final Element DARKNESS = new Element("DARKNESS", 7);
    public static final Element ELECTRIC = new Element("ELECTRIC", 8);
    public static final Element ICE = new Element("ICE", 9);
    public static final Element METAL = new Element("METAL", 10);
    public static final Element PLANTS = new Element("PLANTS", 11);
    public static final Element ROT = new Element("ROT", 12);
    public static final Element SHADOW = new Element("SHADOW", 13);
    public static final Element ANGEL = new Element("ANGEL", 14);
    public static final Element DEVIL = new Element("DEVIL", 15);

    // Element 리스트 (모든 Element를 쉽게 반복 처리 가능)
    private static final List<Element> ALL_ELEMENTS = Arrays.asList(
            NORMAL, FIRE, WATER, EARTH, WIND, POISON, LIGHT, DARKNESS,
            ELECTRIC, ICE, METAL, PLANTS, ROT, SHADOW, ANGEL, DEVIL
    );

    // -----------------------------
    // 인스턴스 변수
    // -----------------------------
    private final int index;       // Element 고유 인덱스
    private final String name;     // Element 이름

    // -----------------------------
    // 관계 매트릭스
    // [this.index][target.index] = 관계 값
    // -----------------------------
    private static final int[][] relationMatrix = new int[ALL_ELEMENTS.size()][ALL_ELEMENTS.size()];

    // 생성자 (private으로 외부에서 직접 생성 금지)
    private Element(String name, int index) {
        this.name = name;
        this.index = index;
    }

    // -----------------------------
    // Getter
    // -----------------------------
    public String getName() { return name; }
    public int getIndex() { return index; }

    // -----------------------------
    // 관계 설정 및 조회
    // -----------------------------
    /**
     * 특정 Element와의 관계를 설정
     */
    public void setRelation(Element target, int relation) {
        relationMatrix[this.index][target.index] = relation;
    }

    /**
     * 특정 Element와의 관계를 조회
     */
    public int getRelation(Element target) {
        return relationMatrix[this.index][target.index];
    }

    // -----------------------------
    // 유틸리티
    // -----------------------------
    /**
     * 이름으로 Element 조회
     */
    public static Element getByName(String name) {
        for (Element e : ALL_ELEMENTS) {
            if (e.getName().equals(name)) return e;
        }
        return null;
    }

    @Override
    public String toString() { return name; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Element e)) return false;
        return name.equals(e.name);
    }

    @Override
    public int hashCode() { return name.hashCode(); }

    /**
     * 관계 초기화: 모든 관계를 NONE(0)으로 설정
     */
    public static void clearRelations() {
        for (int[] matrix : relationMatrix) {
            Arrays.fill(matrix, NONE);
        }
    }
}
