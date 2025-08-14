package io.lumpq126.eclipsia.elements;

import java.util.HashSet;
import java.util.Set;

/**
 * 게임 내 '속성(Element)'을 나타내는 클래스.
 * 각 속성은 이름(name)과 다른 속성과의 관계(강점, 약점 등)를 가진다.
 * 관계는 단방향이거나 양방향일 수 있다.
 */
public class Element {

    /** 일반 속성 */
    public static final Element NORMAL = new Element("NORMAL");
    /** 불 속성 */
    public static final Element FIRE = new Element("FIRE");
    /** 물 속성 */
    public static final Element WATER = new Element("WATER");
    /** 땅 속성 */
    public static final Element EARTH = new Element("EARTH");
    /** 바람 속성 */
    public static final Element WIND = new Element("WIND");
    /** 독 속성 */
    public static final Element POISON = new Element("POISON");
    /** 빛 속성 */
    public static final Element LIGHT = new Element("LIGHT");
    /** 어둠 속성 */
    public static final Element DARKNESS = new Element("DARKNESS");
    /** 전기 속성 */
    public static final Element ELECTRIC = new Element("ELECTRIC");
    /** 얼음 속성 */
    public static final Element ICE = new Element("ICE");
    /** 금속 속성 */
    public static final Element METAL = new Element("METAL");
    /** 식물 속성 */
    public static final Element PLANTS = new Element("PLANTS");
    /** 부패 속성 */
    public static final Element ROT = new Element("ROT");
    /** 그림자 속성 */
    public static final Element SHADOW = new Element("SHADOW");
    /** 천사 속성 */
    public static final Element ANGEL = new Element("ANGEL");
    /** 악마 속성 */
    public static final Element DEVIL = new Element("DEVIL");

    /** 속성 이름 */
    private final String name;

    /** 일반 강점 속성 목록 */
    private final Set<Element> strengths = new HashSet<>();
    /** 궁극 강점 속성 목록 */
    private final Set<Element> ultimateStrengths = new HashSet<>();
    /** 일반 약점 속성 목록 */
    private final Set<Element> weaknesses = new HashSet<>();
    /** 궁극 약점 속성 목록 */
    private final Set<Element> ultimateWeaknesses = new HashSet<>();
    /** 비슷한 수준(중립) 관계 속성 목록 */
    private final Set<Element> generals = new HashSet<>();
    /** 양방향 강점(특수 상성) 속성 목록 */
    private final Set<Element> mutualStrengths = new HashSet<>();

    /**
     * 새로운 속성을 생성한다.
     *
     * @param name 속성 이름
     */
    private Element(String name) {
        this.name = name;
    }

    /**
     * 속성 이름을 반환한다.
     *
     * @return 속성 이름
     */
    public String getName() {
        return name;
    }

    /**
     * 이 속성이 가진 모든 관계(강점, 약점, 중립 관계)를 초기화한다.
     * 모든 관계 Set이 비워진다.
     */
    public void clearRelations() {
        strengths.clear();
        ultimateStrengths.clear();
        weaknesses.clear();
        ultimateWeaknesses.clear();
        generals.clear();
        mutualStrengths.clear();
    }

    /**
     * 일반 강점 속성을 추가한다.
     *
     * @param e 강점으로 추가할 속성
     */
    public void addStrength(Element e) {
        strengths.add(e);
    }

    /**
     * 궁극 강점 속성을 추가한다.
     *
     * @param e 궁극 강점으로 추가할 속성
     */
    public void addUltimateStrength(Element e) {
        ultimateStrengths.add(e);
    }

    /**
     * 일반 약점 속성을 추가한다.
     *
     * @param e 약점으로 추가할 속성
     */
    public void addWeakness(Element e) {
        weaknesses.add(e);
    }

    /**
     * 궁극 약점 속성을 추가한다.
     *
     * @param e 궁극 약점으로 추가할 속성
     */
    public void addUltimateWeakness(Element e) {
        ultimateWeaknesses.add(e);
    }

    /**
     * 중립 관계 속성을 추가한다.
     *
     * @param e 중립 관계로 추가할 속성
     */
    public void addGeneral(Element e) {
        generals.add(e);
    }

    /**
     * 양방향 강점 관계 속성을 추가한다.
     * 이 경우 서로가 서로를 특수한 강점으로 간주한다.
     *
     * @param e 양방향 강점 관계로 추가할 속성
     */
    public void addMutualStrength(Element e) {
        mutualStrengths.add(e);
    }

    /**
     * 일반 강점 속성 목록을 반환한다.
     *
     * @return 강점 속성 Set의 복사본
     */
    public Set<Element> getStrengths() {
        return new HashSet<>(strengths);
    }

    /**
     * 궁극 강점 속성 목록을 반환한다.
     *
     * @return 궁극 강점 속성 Set의 복사본
     */
    public Set<Element> getUltimateStrengths() {
        return new HashSet<>(ultimateStrengths);
    }

    /**
     * 일반 약점 속성 목록을 반환한다.
     *
     * @return 약점 속성 Set의 복사본
     */
    public Set<Element> getWeaknesses() {
        return new HashSet<>(weaknesses);
    }

    /**
     * 궁극 약점 속성 목록을 반환한다.
     *
     * @return 궁극 약점 속성 Set의 복사본
     */
    public Set<Element> getUltimateWeaknesses() {
        return new HashSet<>(ultimateWeaknesses);
    }

    /**
     * 중립 관계 속성 목록을 반환한다.
     *
     * @return 중립 관계 속성 Set의 복사본
     */
    public Set<Element> getGenerals() {
        return new HashSet<>(generals);
    }

    /**
     * 양방향 강점 속성 목록을 반환한다.
     *
     * @return 양방향 강점 속성 Set의 복사본
     */
    public Set<Element> getMutualStrengths() {
        return new HashSet<>(mutualStrengths);
    }

    /**
     * 두 속성 간 관계를 판별한다.
     * 판별 우선순위:
     * <ol>
     *   <li>mutualStrengths (양방향 강점) → 10</li>
     *   <li>ultimateStrength → 5</li>
     *   <li>strength → 4</li>
     *   <li>ultimateWeakness → 3</li>
     *   <li>weakness → 2</li>
     *   <li>general → 1</li>
     *   <li>그 외 → 0</li>
     * </ol>
     *
     * @param other 비교 대상 속성
     * @return 관계 우선순위를 나타내는 정수 값
     */
    public int getRelation(Element element, Element other) {
        if (element.getMutualStrengths().contains(other)) return 10; // 특수 플래그
        if (element.getUltimateStrengths().contains(other)) return 5;
        if (element.getStrengths().contains(other)) return 4;
        if (element.getUltimateWeaknesses().contains(other)) return 3;
        if (element.getWeaknesses().contains(other)) return 2;
        if (element.getGenerals().contains(other)) return 1;
        return 0;
    }

    /**
     * 디버깅 및 로깅을 위한 문자열 표현을 반환한다.
     *
     * @return "Element.속성이름" 형식의 문자열
     */
    @Override
    public String toString() {
        return name;
    }

    /**
     * 두 Element 객체의 동등성을 비교한다.
     * 이름이 동일하면 같은 속성으로 간주한다.
     *
     * @param obj 비교 대상 객체
     * @return 동일하면 true, 아니면 false
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Element other)) return false;
        return name.equals(other.name);
    }

    /**
     * 해시 코드 생성.
     * 속성 이름 기반으로 해시 코드를 만든다.
     *
     * @return 해시 코드 값
     */
    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
