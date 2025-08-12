package io.lumpq126.eclipsia.elements;

import java.util.EnumSet;
import java.util.Set;

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

    private final Set<Element> strengths = EnumSet.noneOf(Element.class);
    private final Set<Element> ultimateStrengths = EnumSet.noneOf(Element.class);
    private final Set<Element> weaknesses = EnumSet.noneOf(Element.class);
    private final Set<Element> ultimateWeaknesses = EnumSet.noneOf(Element.class);
    private final Set<Element> generals = EnumSet.noneOf(Element.class);
    private final Set<Element> mutualStrengths = EnumSet.noneOf(Element.class);

    public void clearRelations() {
        strengths.clear();
        ultimateStrengths.clear();
        weaknesses.clear();
        ultimateWeaknesses.clear();
        generals.clear();
        mutualStrengths.clear();
    }

    public void addStrength(Element e) {
        strengths.add(e);
    }

    public void addUltimateStrength(Element e) {
        ultimateStrengths.add(e);
    }

    public void addWeakness(Element e) {
        weaknesses.add(e);
    }

    public void addUltimateWeakness(Element e) {
        ultimateWeaknesses.add(e);
    }

    public void addGeneral(Element e) {
        generals.add(e);
    }

    public void addMutualStrength(Element e) {
        mutualStrengths.add(e);
    }

    public Set<Element> getStrengths() {
        return EnumSet.copyOf(strengths);
    }

    public Set<Element> getUltimateStrengths() {
        return EnumSet.copyOf(ultimateStrengths);
    }

    public Set<Element> getWeaknesses() {
        return EnumSet.copyOf(weaknesses);
    }

    public Set<Element> getUltimateWeaknesses() {
        return EnumSet.copyOf(ultimateWeaknesses);
    }

    public Set<Element> getGenerals() {
        return EnumSet.copyOf(generals);
    }

    public Set<Element> getMutualStrengths() {
        return EnumSet.copyOf(mutualStrengths);
    }

    /**
     * 두 속성 간 관계 판별
     * 우선순위: mutualStrengths(양방향) > ultimateStrength > strength > ultimateWeakness > weakness > general
     */
    public int getRelation(Element other) {
        // 상호 강점은 별도 플래그로 사용, 점수 계산에는 영향 없음
        if (mutualStrengths.contains(other) && other.mutualStrengths.contains(this)) {
            return 10; // 특수 플래그
        }
        if (ultimateStrengths.contains(other)) return 5;
        if (strengths.contains(other)) return 4;
        if (ultimateWeaknesses.contains(other)) return 3;
        if (weaknesses.contains(other)) return 2;
        if (generals.contains(other)) return 1;
        return 0;
    }

    @Override
    public String toString() {
        return "Element." + name();
    }
}
