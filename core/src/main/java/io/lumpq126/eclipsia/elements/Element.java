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
    ELECTRIC;

    private final Set<Element> strengths = EnumSet.noneOf(Element.class);
    private final Set<Element> ultimateStrengths = EnumSet.noneOf(Element.class);
    private final Set<Element> weaknesses = EnumSet.noneOf(Element.class);
    private final Set<Element> ultimateWeaknesses = EnumSet.noneOf(Element.class);
    private final Set<Element> generals = EnumSet.noneOf(Element.class);
    private final Set<Element> mutualStrengths = EnumSet.noneOf(Element.class);

    // 관계 초기화 (reload 시 필요)
    public void clearRelations() {
        strengths.clear();
        ultimateStrengths.clear();
        weaknesses.clear();
        ultimateWeaknesses.clear();
        generals.clear();
        mutualStrengths.clear();
    }

    // 관계 추가
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

    // 관계 조회
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
     */
    public String getRelation(Element other) {
        if (ultimateStrengths.contains(other)) return "최강점";
        if (strengths.contains(other)) return "강점";
        if (ultimateWeaknesses.contains(other)) return "최약점";
        if (weaknesses.contains(other)) return "약점";
        if (mutualStrengths.contains(other) && other.mutualStrengths.contains(this)) return "서로상성";
        if (generals.contains(other)) return "무상성";
        return "관계없음";
    }

    @Override
    public String toString() {
        return "Element." + name();
    }
}
