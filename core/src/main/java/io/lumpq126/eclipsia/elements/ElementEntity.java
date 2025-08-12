package io.lumpq126.eclipsia.elements;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public class ElementEntity {
    private static NamespacedKey elementKey;
    private static NamespacedKey enhanceElementKey;
    private static NamespacedKey enhanceAttackKey;
    private static NamespacedKey enhanceDefenseKey;

    private final Entity entity;
    private Element cachedElement;
    private int cachedEnhanceElement = -1;
    private int cachedEnhanceAttack = -1;
    private int cachedEnhanceDefense = -1;

    public static void init(JavaPlugin plugin) {
        elementKey = new NamespacedKey(plugin, "element");
        enhanceElementKey = new NamespacedKey(plugin, "enhance_element");
        enhanceAttackKey = new NamespacedKey(plugin, "enhance_attack");
        enhanceDefenseKey = new NamespacedKey(plugin, "enhance_defense");
    }

    public ElementEntity(Entity entity) {
        this.entity = entity;
    }

    // --- 속성 ---
    public Element getElement() {
        if (cachedElement != null) return cachedElement;
        PersistentDataContainer data = entity.getPersistentDataContainer();
        String value = data.get(elementKey, PersistentDataType.STRING);
        if (value == null) return cachedElement = Element.NORMAL;
        Element e = ElementEntityHelper.getElementByName(value);
        return cachedElement = (e != null) ? e : Element.NORMAL;
    }

    public void setElement(Element element) {
        cachedElement = element;
        entity.getPersistentDataContainer().set(elementKey, PersistentDataType.STRING, element.getName());
    }

    // --- 강화도: 속성 자체 ---
    public int getEnhanceElement() {
        if (cachedEnhanceElement != -1) return cachedEnhanceElement;
        Integer val = entity.getPersistentDataContainer().get(enhanceElementKey, PersistentDataType.INTEGER);
        return cachedEnhanceElement = (val != null) ? val : 0;
    }

    public void setEnhanceElement(int level) {
        cachedEnhanceElement = Math.max(0, level);
        entity.getPersistentDataContainer().set(enhanceElementKey, PersistentDataType.INTEGER, cachedEnhanceElement);
    }

    public void addEnhanceElement(int amount) {
        setEnhanceElement(getEnhanceElement() + amount);
    }

    public void removeEnhanceElement(int amount) {
        setEnhanceElement(Math.max(0, getEnhanceElement() - amount));
    }

    // --- 강화도: 공격력 ---
    public int getEnhanceAttack() {
        if (cachedEnhanceAttack != -1) return cachedEnhanceAttack;
        Integer val = entity.getPersistentDataContainer().get(enhanceAttackKey, PersistentDataType.INTEGER);
        return cachedEnhanceAttack = (val != null) ? val : 0;
    }

    public void setEnhanceAttack(int level) {
        cachedEnhanceAttack = Math.max(0, level);
        entity.getPersistentDataContainer().set(enhanceAttackKey, PersistentDataType.INTEGER, cachedEnhanceAttack);
    }

    public void addEnhanceAttack(int amount) {
        setEnhanceAttack(getEnhanceAttack() + amount);
    }

    public void removeEnhanceAttack(int amount) {
        setEnhanceAttack(Math.max(0, getEnhanceAttack() - amount));
    }

    // --- 강화도: 방어력 ---
    public int getEnhanceDefense() {
        if (cachedEnhanceDefense != -1) return cachedEnhanceDefense;
        Integer val = entity.getPersistentDataContainer().get(enhanceDefenseKey, PersistentDataType.INTEGER);
        return cachedEnhanceDefense = (val != null) ? val : 0;
    }

    public void setEnhanceDefense(int level) {
        cachedEnhanceDefense = Math.max(0, level);
        entity.getPersistentDataContainer().set(enhanceDefenseKey, PersistentDataType.INTEGER, cachedEnhanceDefense);
    }

    public void addEnhanceDefense(int amount) {
        setEnhanceDefense(getEnhanceDefense() + amount);
    }

    public void removeEnhanceDefense(int amount) {
        setEnhanceDefense(Math.max(0, getEnhanceDefense() - amount));
    }

    public Entity toEntity() {
        return entity;
    }

    public Player toPlayer() {
        return (entity instanceof Player) ? (Player) entity : null;
    }

    public static ElementEntity parseEntity(Entity e) {
        return new ElementEntity(e);
    }

    public static ElementEntity parsePlayer(Player p) {
        return new ElementEntity(p);
    }
}