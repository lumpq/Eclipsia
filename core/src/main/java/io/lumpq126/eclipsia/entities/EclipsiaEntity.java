package io.lumpq126.eclipsia.entities;

import io.lumpq126.eclipsia.elements.Element;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.Optional;

/**
 * Bukkit Entity 확장 클래스
 * PersistentDataContainer를 통해 Element 정보와 강화 수치를 영구 저장 가능
 */
public class EclipsiaEntity {

    private final Entity entity;                  // 실제 Bukkit Entity
    private final NamespacedKey elementKey;       // Element 이름 저장 키
    private final NamespacedKey enhanceElementKey;// Element 강화 레벨 저장 키
    private final NamespacedKey enhanceAttackKey; // 공격 강화 레벨 저장 키
    private final NamespacedKey enhanceDefenseKey;// 방어 강화 레벨 저장 키

    // 생성자
    public EclipsiaEntity(Plugin plugin, Entity entity) {
        this.entity = entity;
        this.elementKey = new NamespacedKey(plugin, "element");
        this.enhanceElementKey = new NamespacedKey(plugin, "enhance_element");
        this.enhanceAttackKey = new NamespacedKey(plugin, "enhance_attack");
        this.enhanceDefenseKey = new NamespacedKey(plugin, "enhance_defense");
    }

    // -----------------------------
    // Element 관리
    // -----------------------------

    /** Entity에 저장된 Element 반환 (없으면 Optional.empty) */
    public Optional<Element> getElement() {
        PersistentDataContainer data = entity.getPersistentDataContainer();
        String name = data.get(elementKey, PersistentDataType.STRING);
        return Optional.ofNullable(Element.getByName(name));
    }

    /** Entity에 Element 저장 (null이면 삭제) */
    public void setElement(Element element) {
        PersistentDataContainer data = entity.getPersistentDataContainer();
        if (element != null) {
            data.set(elementKey, PersistentDataType.STRING, element.getName());
        } else {
            data.remove(elementKey);
        }
    }

    /** 원본 Bukkit Entity 반환 */
    public Entity toEntity() { return entity; }

    /** Player로 캐스팅(Optional) */
    public Optional<Player> toPlayer() {
        return (entity instanceof Player p) ? Optional.of(p) : Optional.empty();
    }

    /** Entity에 Element가 존재하는지 여부 확인 */
    public boolean hasElement() {
        return entity.getPersistentDataContainer().has(elementKey, PersistentDataType.STRING);
    }

    // -----------------------------
    // 속성 강화도 관리
    // -----------------------------
    // Element 강화 레벨
    public int getEnhanceElement() {
        PersistentDataContainer data = entity.getPersistentDataContainer();
        Integer val = data.get(enhanceElementKey, PersistentDataType.INTEGER);
        return (val != null) ? val : 0;
    }

    public void setEnhanceElement(int level) {
        PersistentDataContainer data = entity.getPersistentDataContainer();
        data.set(enhanceElementKey, PersistentDataType.INTEGER, Math.max(0, level));
    }

    public void addEnhanceElement(int amount) {
        setEnhanceElement(getEnhanceElement() + amount);
    }

    public void removeEnhanceElement(int amount) {
        setEnhanceElement(Math.max(0, getEnhanceElement() - amount));
    }

    // 공격력 강화 레벨
    public int getEnhanceAttack() {
        PersistentDataContainer data = entity.getPersistentDataContainer();
        Integer val = data.get(enhanceAttackKey, PersistentDataType.INTEGER);
        return (val != null) ? val : 0;
    }

    public void setEnhanceAttack(int level) {
        PersistentDataContainer data = entity.getPersistentDataContainer();
        data.set(enhanceAttackKey, PersistentDataType.INTEGER, Math.max(0, level));
    }

    public void addEnhanceAttack(int amount) {
        setEnhanceAttack(getEnhanceAttack() + amount);
    }

    public void removeEnhanceAttack(int amount) {
        setEnhanceAttack(Math.max(0, getEnhanceAttack() - amount));
    }

    // 방어력 강화 레벨
    public int getEnhanceDefense() {
        PersistentDataContainer data = entity.getPersistentDataContainer();
        Integer val = data.get(enhanceDefenseKey, PersistentDataType.INTEGER);
        return (val != null) ? val : 0;
    }

    public void setEnhanceDefense(int level) {
        PersistentDataContainer data = entity.getPersistentDataContainer();
        data.set(enhanceDefenseKey, PersistentDataType.INTEGER, Math.max(0, level));
    }

    public void addEnhanceDefense(int amount) {
        setEnhanceDefense(getEnhanceDefense() + amount);
    }

    public void removeEnhanceDefense(int amount) {
        setEnhanceDefense(Math.max(0, getEnhanceDefense() - amount));
    }
}
