package io.lumpq126.eclipsia.entities;

import io.lumpq126.eclipsia.elements.Element;
import io.lumpq126.eclipsia.stats.Stat;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

/**
 * Bukkit Entity 확장 클래스
 * PersistentDataContainer를 통해 Element, 강화 수치, 스탯/포인트 영구 저장
 */
public class EclipsiaEntity {

    private static Plugin pluginInstance; // 전역 플러그인 참조

    private final Entity entity;
    private final NamespacedKey elementKey;
    private final NamespacedKey enhanceElementKey;
    private final NamespacedKey enhanceAttackKey;
    private final NamespacedKey enhanceDefenseKey;
    private final NamespacedKey statPointsKey;

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
    }

    public Entity toEntity() { return entity; }
    public Player toPlayer() { return (entity instanceof Player p) ? p : null; }

    // -----------------------------
    // Element 관리 (PDC)
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
    // 속성 강화도 관리 (PDC)
    // -----------------------------
    public int getEnhanceElement() {
        return getIntData(enhanceElementKey, 0);
    }
    public void setEnhanceElement(int level) {
        setIntData(enhanceElementKey, Math.max(0, level));
    }
    public void addEnhanceElement(int amount) { setEnhanceElement(getEnhanceElement() + amount); }

    public int getEnhanceAttack() {
        return getIntData(enhanceAttackKey, 0);
    }
    public void setEnhanceAttack(int level) {
        setIntData(enhanceAttackKey, Math.max(0, level));
    }
    public void addEnhanceAttack(int amount) { setEnhanceAttack(getEnhanceAttack() + amount); }

    public int getEnhanceDefense() {
        return getIntData(enhanceDefenseKey, 0);
    }
    public void setEnhanceDefense(int level) {
        setIntData(enhanceDefenseKey, Math.max(0, level));
    }
    public void addEnhanceDefense(int amount) { setEnhanceDefense(getEnhanceDefense() + amount); }

    // -----------------------------
    // 스탯 관리 (PDC)
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
    // 스탯 포인트 관리 (PDC)
    // -----------------------------
    public int getStatPoints() {
        return getIntData(statPointsKey, Stat.INITIAL_POINT);
    }

    public void setStatPoints(int points) {
        setIntData(statPointsKey, Math.max(0, points));
    }

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

    private String getStringData(NamespacedKey key) {
        return entity.getPersistentDataContainer().get(key, PersistentDataType.STRING);
    }

    private void setStringData(NamespacedKey key, String value) {
        entity.getPersistentDataContainer().set(key, PersistentDataType.STRING, value);
    }

    private void removeData(NamespacedKey key) {
        entity.getPersistentDataContainer().remove(key);
    }

    private boolean hasData(NamespacedKey key, PersistentDataType<?, ?> type) {
        return entity.getPersistentDataContainer().has(key, type);
    }
}
