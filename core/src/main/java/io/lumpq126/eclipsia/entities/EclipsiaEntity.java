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

public class EclipsiaEntity {

    private static Plugin pluginInstance;

    private final Entity entity;

    // 기존 Keys
    private final NamespacedKey elementKey;
    private final NamespacedKey enhanceElementKey;
    private final NamespacedKey enhanceAttackKey;
    private final NamespacedKey enhanceDefenseKey;
    private final NamespacedKey statPointsKey;

    // 새 Keys
    private final NamespacedKey levelKey;
    private final NamespacedKey expKey;
    private final NamespacedKey siaKey;

    // 상수
    private static final int MAX_LEVEL = 999;
    private static final int INITIAL_LEVEL = 1;
    private static final double EXP_PER_LEVEL_MULTIPLIER = 100.0;
    private static final int INITIAL_SIA = 10000;

    public static void init(Plugin plugin) {
        pluginInstance = plugin;
    }

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

    public Entity toEntity() { return entity; }
    public Player toPlayer() { return (entity instanceof Player p) ? p : null; }

    // -----------------------------
    // SIA 관리
    // -----------------------------
    public int getSia() {
        return getIntData(siaKey, INITIAL_SIA);
    }

    public void setSia(int amount) {
        setIntData(siaKey, Math.max(0, amount));
    }

    public void addSia(int amount) {
        setSia(getSia() + amount);
    }

    public void removeSia(int amount) {
        setSia(Math.max(0, getSia() - amount));
    }

    // -----------------------------
    // 레벨 / 경험치 관리
    // -----------------------------
    public int getLevel() {
        return getIntData(levelKey, INITIAL_LEVEL);
    }

    public void setLevel(int level) {
        int clamped = Math.min(MAX_LEVEL, Math.max(1, level));
        int oldLevel = getLevel();
        setIntData(levelKey, clamped);

        if (clamped > oldLevel && toPlayer() != null) {
            Bukkit.getPluginManager().callEvent(new PlayerLevelUpEvent(toPlayer(), oldLevel, clamped));
        }
    }

    public void addLevel(int amount) {
        setLevel(getLevel() + amount);
    }

    public void resetLevel() {
        setLevel(INITIAL_LEVEL); // 레벨 초기화
        setDoubleData(expKey, 0.0); // 경험치 초기화
        setStatPoints(Stat.INITIAL_POINT); // 스탯포인트 초기화
        for (Stat stat : Stat.values()) {
            setStat(stat, Stat.INITIAL_STAT);
        }
    }

    public double getExp() {
        return getDoubleData(expKey);
    }

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

    public void addExp(double amount) {
        setExp(getExp() + amount);
    }

    public double getRequiredExp(int level) {
        if (level >= MAX_LEVEL) return 0.0;
        return level * EXP_PER_LEVEL_MULTIPLIER;
    }

    // -----------------------------
    // Element 관리
    // -----------------------------
    public Element getElement() {
        String name = getStringData(elementKey);
        Element parsed = Element.fromName(name);
        return (parsed != null) ? parsed : Element.NORMAL;
    }

    public void setElement(Element element) {
        if (element != null) setStringData(elementKey, Element.getKey(element));
        else removeData(elementKey);
    }

    public boolean hasElement() {
        return hasData(elementKey, PersistentDataType.STRING);
    }

    // -----------------------------
    // 속성 강화도 관리
    // -----------------------------
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
    public int getStat(Stat stat) {
        if (stat == null) return 0;
        NamespacedKey key = new NamespacedKey(pluginInstance, "stat_" + stat.name().toLowerCase());
        return getIntData(key, Stat.INITIAL_STAT);
    }

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
