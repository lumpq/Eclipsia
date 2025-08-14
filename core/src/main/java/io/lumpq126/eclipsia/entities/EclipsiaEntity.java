package io.lumpq126.eclipsia.entities;

import io.lumpq126.eclipsia.elements.Element;
import io.lumpq126.eclipsia.events.PlayerExpUpEvent;
import io.lumpq126.eclipsia.events.PlayerLevelUpEvent;
import io.lumpq126.eclipsia.stats.Stat;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

/**
 * {@code EclipsiaEntity} 클래스는 Bukkit 엔티티(Player, Mob 등)에
 * RPG 스타일의 레벨, 경험치, 스탯, SIA, 속성(Element) 등을
 * 관리할 수 있는 래퍼 클래스입니다.
 */
public class EclipsiaEntity {

    /** 플러그인 인스턴스 (초기화 필요) */
    private static Plugin pluginInstance;

    /** 관리 대상 Bukkit 엔티티 */
    private final Entity entity;

    /** 엔티티 PDC(Persistent Data Container) 키 */
    private final NamespacedKey elementKey;
    private final NamespacedKey enhanceElementKey;
    private final NamespacedKey enhanceAttackKey;
    private final NamespacedKey enhanceDefenseKey;
    private final NamespacedKey statPointsKey;
    private final NamespacedKey levelKey;
    private final NamespacedKey expKey;
    private final NamespacedKey siaKey;

    /** 상수 정의 */
    private static final int MAX_LEVEL = 999;
    private static final int INITIAL_LEVEL = 1;
    private static final double EXP_PER_LEVEL_MULTIPLIER = 100.0;
    private static final int INITIAL_SIA = 10000;

    /**
     * EclipsiaEntity 초기화를 위해 플러그인 인스턴스를 설정합니다.
     *
     * @param plugin 플러그인 인스턴스
     */
    public static void init(Plugin plugin) {
        pluginInstance = plugin;
    }

    /**
     * 주어진 Bukkit 엔티티를 감싸는 {@code EclipsiaEntity} 객체를 생성합니다.
     * PDC에 초기 레벨, 경험치, SIA가 존재하지 않으면 초기값으로 설정됩니다.
     *
     * @param entity Bukkit 엔티티
     * @throws IllegalStateException {@code init(plugin)}가 호출되지 않은 경우
     */
    public EclipsiaEntity(Entity entity) {
        if (pluginInstance == null) {
            throw new IllegalStateException("EclipsiaEntity.init(plugin) must be called before creating instances.");
        }
        this.entity = entity;
        this.elementKey = new NamespacedKey(pluginInstance, "element");
        this.enhanceElementKey = new NamespacedKey(pluginInstance, "enhance_element");
        this.enhanceAttackKey = new NamespacedKey(pluginInstance, "enhance_attack");
        this.enhanceDefenseKey = new NamespacedKey(pluginInstance, "enhance_defense");
        this.statPointsKey = new NamespacedKey(pluginInstance, "stat_points");
        this.levelKey = new NamespacedKey(pluginInstance, "level");
        this.expKey = new NamespacedKey(pluginInstance, "exp");
        this.siaKey = new NamespacedKey(pluginInstance, "sia");

        if (!hasData(levelKey, PersistentDataType.INTEGER)) setLevel(INITIAL_LEVEL);
        if (!hasData(expKey, PersistentDataType.DOUBLE)) setExp(0);
        if (!hasData(siaKey, PersistentDataType.INTEGER)) setSia(INITIAL_SIA);
    }

    /**
     * 감싸고 있는 엔티티를 반환합니다.
     *
     * @return Bukkit 엔티티
     */
    public Entity toEntity() { return entity; }

    /**
     * 엔티티가 Player이면 Player 객체를 반환하고, 아니면 null을 반환합니다.
     *
     * @return Player 객체 또는 null
     */
    public Player toPlayer() { return (entity instanceof Player p) ? p : null; }

    // -----------------------------
    // SIA 관리
    // -----------------------------

    /**
     * 엔티티의 SIA를 가져옵니다.
     *
     * @return SIA 값
     */
    public int getSia() {
        return getIntData(siaKey, INITIAL_SIA);
    }

    /**
     * 엔티티의 SIA를 설정합니다. 0 미만은 허용되지 않습니다.
     *
     * @param amount SIA 값
     */
    public void setSia(int amount) {
        setIntData(siaKey, Math.max(0, amount));
    }

    /**
     * 엔티티의 SIA를 추가합니다.
     *
     * @param amount 추가할 SIA
     */
    public void addSia(int amount) {
        setSia(getSia() + amount);
    }

    /**
     * 엔티티의 SIA를 감소시킵니다. 0 미만으로 내려가지 않습니다.
     *
     * @param amount 감소할 SIA
     */
    public void removeSia(int amount) {
        setSia(Math.max(0, getSia() - amount));
    }

    // -----------------------------
    // 레벨 / 경험치 관리
    // -----------------------------

    /**
     * 엔티티의 레벨을 가져옵니다.
     *
     * @return 현재 레벨
     */
    public int getLevel() {
        return getIntData(levelKey, INITIAL_LEVEL);
    }

    /**
     * 엔티티의 레벨을 설정합니다. 최대 레벨을 초과할 수 없으며,
     * 레벨업 시 PlayerLevelUpEvent를 호출합니다.
     *
     * @param level 설정할 레벨
     */
    public void setLevel(int level) {
        int clamped = Math.min(MAX_LEVEL, Math.max(1, level));
        int oldLevel = getLevel();
        setIntData(levelKey, clamped);

        if (clamped > oldLevel && toPlayer() != null) {
            Bukkit.getPluginManager().callEvent(new PlayerLevelUpEvent(toPlayer(), oldLevel, clamped));
        }
    }

    /**
     * 엔티티의 레벨을 증가시킵니다.
     *
     * @param amount 증가할 레벨
     */
    public void addLevel(int amount) {
        setLevel(getLevel() + amount);
    }

    /**
     * 엔티티의 레벨, 경험치, 스탯, 스탯 포인트를 초기화합니다.
     */
    public void resetLevel() {
        setLevel(INITIAL_LEVEL);
        setDoubleData(expKey, 0.0);
        setStatPoints(Stat.INITIAL_POINT);
        for (Stat stat : Stat.values()) {
            setStat(stat, Stat.INITIAL_STAT);
        }
    }

    /**
     * 엔티티의 경험치를 가져옵니다.
     *
     * @return 현재 경험치
     */
    public double getExp() {
        return getDoubleData(expKey);
    }

    /**
     * 엔티티의 경험치를 설정하고 레벨업을 처리합니다.
     * Player인 경우 PlayerExpUpEvent를 호출합니다.
     *
     * @param exp 설정할 경험치
     */
    public void setExp(double exp) {
        int currentLevel = getLevel();
        double currentExp = Math.max(0.0, exp);

        if (currentLevel >= MAX_LEVEL) {
            setDoubleData(expKey, 0.0);
            return;
        }

        int newLevel = currentLevel;
        while (currentExp >= getRequiredExp(newLevel) && newLevel < MAX_LEVEL) {
            currentExp -= getRequiredExp(newLevel);
            newLevel++;
        }

        if (newLevel > currentLevel) {
            setLevel(newLevel);
        }

        double oldExp = getExp();
        setDoubleData(expKey, currentExp);

        if (toPlayer() != null) {
            Bukkit.getPluginManager().callEvent(new PlayerExpUpEvent(toPlayer(), oldExp, currentExp, newLevel));
        }
    }

    /**
     * 엔티티의 경험치를 추가합니다.
     *
     * @param amount 추가할 경험치
     */
    public void addExp(double amount) {
        setExp(getExp() + amount);
    }

    /**
     * 지정 레벨에 도달하기 위해 필요한 경험치를 계산합니다.
     *
     * @param level 계산할 레벨
     * @return 필요 경험치
     */
    public double getRequiredExp(int level) {
        if (level >= MAX_LEVEL) return 0.0;
        return level * EXP_PER_LEVEL_MULTIPLIER;
    }

    // -----------------------------
    // Element 관리
    // -----------------------------

    /**
     * 엔티티의 속성(Element)을 가져옵니다.
     *
     * @return 현재 Element
     */
    public Element getElement() {
        String name = getStringData(elementKey);
        Element parsed = Element.fromName(name);
        return (parsed != null) ? parsed : Element.NORMAL;
    }

    /**
     * 엔티티의 속성(Element)을 설정합니다.
     *
     * @param element 설정할 Element
     */
    public void setElement(Element element) {
        if (element != null) setStringData(elementKey, Element.getKey(element));
        else removeData(elementKey);
    }

    /**
     * 엔티티가 속성을 가지고 있는지 확인합니다.
     *
     * @return 속성이 존재하면 true, 아니면 false
     */
    public boolean hasElement() {
        return hasData(elementKey, PersistentDataType.STRING);
    }

    // -----------------------------
    // 속성 강화도 관리
    // -----------------------------

    /** 각각의 속성 강화도를 가져오고 설정/증가시키는 메서드 */
    public int getEnhanceElement() { return getIntData(enhanceElementKey, 0); }
    public void setEnhanceElement(int level) { setIntData(enhanceElementKey, Math.max(0, level)); }
    public void addEnhanceElement(int amount) { setEnhanceElement(getEnhanceElement() + amount); }

    public int getEnhanceAttack() { return getIntData(enhanceAttackKey, 0); }
    public void setEnhanceAttack(int level) { setIntData(enhanceAttackKey, Math.max(0, level)); }
    public void addEnhanceAttack(int amount) { setEnhanceAttack(getEnhanceAttack() + amount); }

    public int getEnhanceDefense() { return getIntData(enhanceDefenseKey, 0); }
    public void setEnhanceDefense(int level) { setIntData(enhanceDefenseKey, Math.max(0, level)); }
    public void addEnhanceDefense(int amount) { setEnhanceDefense(getEnhanceDefense() + amount); }

    // -----------------------------
    // 스탯 관리
    // -----------------------------

    /**
     * 엔티티의 특정 스탯 값을 가져옵니다.
     *
     * @param stat 스탯 종류
     * @return 스탯 값
     */
    public int getStat(Stat stat) {
        if (stat == null) return 0;
        NamespacedKey key = new NamespacedKey(pluginInstance, "stat_" + stat.name().toLowerCase());
        return getIntData(key, Stat.INITIAL_STAT);
    }

    /**
     * 엔티티의 특정 스탯 값을 설정합니다.
     *
     * @param stat 스탯 종류
     * @param value 설정할 값
     */
    public void setStat(Stat stat, int value) {
        if (stat == null) return;
        NamespacedKey key = new NamespacedKey(pluginInstance, "stat_" + stat.name().toLowerCase());
        setIntData(key, Math.max(0, value));
    }

    public void addStat(Stat stat, int amount) {
        if (stat == null) return;
        setStat(stat, getStat(stat) + amount);
    }

    public void removeStat(Stat stat, int amount) {
        if (stat == null) return;
        setStat(stat, Math.max(0, getStat(stat) - amount));
    }

    // -----------------------------
    // 스탯 포인트 관리
    // -----------------------------

    public int getStatPoints() { return getIntData(statPointsKey, Stat.INITIAL_POINT); }
    public void setStatPoints(int points) { setIntData(statPointsKey, Math.max(0, points)); }
    public void addStatPoints(int amount) { setStatPoints(getStatPoints() + amount); }
    public void removeStatPoints(int amount) { setStatPoints(Math.max(0, getStatPoints() - amount)); }

    // -----------------------------
    // 내부 PDC 유틸
    // -----------------------------

    private int getIntData(NamespacedKey key, int defaultValue) {
        Integer val = entity.getPersistentDataContainer().get(key, PersistentDataType.INTEGER);
        return (val != null) ? val : defaultValue;
    }

    private void setIntData(NamespacedKey key, int value) {
        entity.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, value);
    }

    private double getDoubleData(NamespacedKey key) {
        Double val = entity.getPersistentDataContainer().get(key, PersistentDataType.DOUBLE);
        return (val != null) ? val : 0.0;
    }

    private void setDoubleData(NamespacedKey key, double value) {
        entity.getPersistentDataContainer().set(key, PersistentDataType.DOUBLE, value);
    }

    private String getStringData(NamespacedKey key) {
        return entity.getPersistentDataContainer().get(key, PersistentDataType.STRING);
    }

    private void setStringData(NamespacedKey key, String value) {
        entity.getPersistentDataContainer().set(key, PersistentDataType.STRING, value);
    }

    private void removeData(NamespacedKey key) {
        entity.getPersistentDataContainer().remove(key);
    }

    private <T, Z> boolean hasData(NamespacedKey key, PersistentDataType<T, Z> type) {
        return entity.getPersistentDataContainer().has(key, type);
    }
}
