package io.lumpq126.eclipsia.entities;

import io.lumpq126.eclipsia.elements.Element;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

/**
 * Bukkit Entity 확장 클래스
 * PersistentDataContainer를 통해 Element 정보와 강화 수치를 영구 저장 가능
 */
public class EclipsiaEntity {

    private static final int DEFAULT_LEVEL = 0;
    private static Plugin pluginInstance; // 전역 플러그인 참조

    private final Entity entity;                  // 실제 Bukkit Entity
    private final NamespacedKey elementKey;       // Element 이름 저장 키
    private final NamespacedKey enhanceElementKey;// Element 강화 레벨 저장 키
    private final NamespacedKey enhanceAttackKey; // 공격 강화 레벨 저장 키
    private final NamespacedKey enhanceDefenseKey;// 방어 강화 레벨 저장 키

    /**
     * 플러그인 전역 인스턴스 초기화
     */
    public static void init(Plugin plugin) {
        pluginInstance = plugin;
    }

    // 생성자
    public EclipsiaEntity(Entity entity) {
        if (pluginInstance == null) {
            throw new IllegalStateException("EclipsiaEntity.init(plugin) must be called before creating instances.");
        }
        this.entity = entity;
        this.elementKey = new NamespacedKey(pluginInstance, "element");
        this.enhanceElementKey = new NamespacedKey(pluginInstance, "enhance_element");
        this.enhanceAttackKey = new NamespacedKey(pluginInstance, "enhance_attack");
        this.enhanceDefenseKey = new NamespacedKey(pluginInstance, "enhance_defense");
    }

    // -----------------------------
    // Element 관리
    // -----------------------------

    /** Entity에 저장된 Element 반환 (없으면 Element.NORMAL) */
    public Element getElement() {
        PersistentDataContainer data = entity.getPersistentDataContainer();
        String name = data.get(elementKey, PersistentDataType.STRING);
        Element parsed = Element.fromName(name);
        return (parsed != null) ? parsed : Element.NORMAL;
    }

    /** Entity에 Element 저장 (null이면 삭제) */
    public void setElement(Element element) {
        PersistentDataContainer data = entity.getPersistentDataContainer();
        if (element != null) {
            data.set(elementKey, PersistentDataType.STRING, element.getKey());
        } else {
            data.remove(elementKey);
        }
    }

    /** Entity에 Element가 존재하는지 여부 확인 */
    public boolean hasElement() {
        return entity.getPersistentDataContainer().has(elementKey, PersistentDataType.STRING);
    }

    /** 원본 Bukkit Entity 반환 */
    public Entity toEntity() {
        return entity;
    }

    /** Player로 캐스팅(Optional) */
    public Player toPlayer() {
        return (entity instanceof Player p) ? p : null;
    }

    // -----------------------------
    // 속성 강화도 관리
    // -----------------------------

    public int getEnhanceElement() {
        Integer val = entity.getPersistentDataContainer().get(enhanceElementKey, PersistentDataType.INTEGER);
        return (val != null) ? val : DEFAULT_LEVEL;
    }

    public void setEnhanceElement(int level) {
        entity.getPersistentDataContainer().set(enhanceElementKey, PersistentDataType.INTEGER, Math.max(0, level));
    }

    public void addEnhanceElement(int amount) {
        setEnhanceElement(getEnhanceElement() + amount);
    }

    public void removeEnhanceElement(int amount) {
        setEnhanceElement(Math.max(0, getEnhanceElement() - amount));
    }

    public int getEnhanceAttack() {
        Integer val = entity.getPersistentDataContainer().get(enhanceAttackKey, PersistentDataType.INTEGER);
        return (val != null) ? val : DEFAULT_LEVEL;
    }

    public void setEnhanceAttack(int level) {
        entity.getPersistentDataContainer().set(enhanceAttackKey, PersistentDataType.INTEGER, Math.max(0, level));
    }

    public void addEnhanceAttack(int amount) {
        setEnhanceAttack(getEnhanceAttack() + amount);
    }

    public void removeEnhanceAttack(int amount) {
        setEnhanceAttack(Math.max(0, getEnhanceAttack() - amount));
    }

    public int getEnhanceDefense() {
        Integer val = entity.getPersistentDataContainer().get(enhanceDefenseKey, PersistentDataType.INTEGER);
        return (val != null) ? val : DEFAULT_LEVEL;
    }

    public void setEnhanceDefense(int level) {
        entity.getPersistentDataContainer().set(enhanceDefenseKey, PersistentDataType.INTEGER, Math.max(0, level));
    }

    public void addEnhanceDefense(int amount) {
        setEnhanceDefense(getEnhanceDefense() + amount);
    }

    public void removeEnhanceDefense(int amount) {
        setEnhanceDefense(Math.max(0, getEnhanceDefense() - amount));
    }
}
