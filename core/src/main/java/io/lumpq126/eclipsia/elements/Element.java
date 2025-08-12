package io.lumpq126.eclipsia.elements;

import java.util.HashSet;
import java.util.Set;

public class Element {
    public static final Element NORMAL = new Element("NORMAL");
    public static final Element FIRE = new Element("FIRE");
    public static final Element WATER = new Element("WATER");
    public static final Element EARTH = new Element("EARTH");
    public static final Element WIND = new Element("WIND");
    public static final Element POISON = new Element("POISON");
    public static final Element LIGHT = new Element("LIGHT");
    public static final Element DARKNESS = new Element("DARKNESS");
    public static final Element ELECTRIC = new Element("ELECTRIC");
    public static final Element ICE = new Element("ICE");
    public static final Element METAL = new Element("METAL");
    public static final Element PLANTS = new Element("PLANTS");
    public static final Element ROT = new Element("ROT");
    public static final Element SHADOW = new Element("SHADOW");
    public static final Element ANGEL = new Element("ANGEL");
    public static final Element DEVIL = new Element("DEVIL");

    private final String name;

    private final Set<Element> strengths = new HashSet<>();
    private final Set<Element> ultimateStrengths = new HashSet<>();
    private final Set<Element> weaknesses = new HashSet<>();
    private final Set<Element> ultimateWeaknesses = new HashSet<>();
    private final Set<Element> generals = new HashSet<>();
    private final Set<Element> mutualStrengths = new HashSet<>();

    private Element(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

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
        return new HashSet<>(strengths);
    }

    public Set<Element> getUltimateStrengths() {
        return new HashSet<>(ultimateStrengths);
    }

    public Set<Element> getWeaknesses() {
        return new HashSet<>(weaknesses);
    }

    public Set<Element> getUltimateWeaknesses() {
        return new HashSet<>(ultimateWeaknesses);
    }

    public Set<Element> getGenerals() {
        return new HashSet<>(generals);
    }

    public Set<Element> getMutualStrengths() {
        return new HashSet<>(mutualStrengths);
    }

    /**
     * 두 속성 간 관계 판별
     * 우선순위: mutualStrengths(양방향) > ultimateStrength > strength > ultimateWeakness > weakness > general
     */
    public int getRelation(Element other) {
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
        return "Element." + name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Element other)) return false;
        return name.equals(other.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
