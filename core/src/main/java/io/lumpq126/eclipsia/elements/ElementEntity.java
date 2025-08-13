package io.lumpq126.eclipsia.elements;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Bukkit의 Entity에 '속성(Element)'과 강화 수치(속성 강화, 공격력 강화, 방어력 강화)를 부여하고 관리하는 클래스.
 * <p>
 * 데이터는 Entity의 {@link PersistentDataContainer}에 저장되며,
 * 캐시를 통해 불필요한 접근을 줄인다.
 */
public class ElementEntity {

    /** PersistentDataContainer에서 속성을 식별하기 위한 키 */
    private static NamespacedKey elementKey;
    /** 속성 강화 레벨 키 */
    private static NamespacedKey enhanceElementKey;
    /** 공격력 강화 레벨 키 */
    private static NamespacedKey enhanceAttackKey;
    /** 방어력 강화 레벨 키 */
    private static NamespacedKey enhanceDefenseKey;

    /** 관리 대상 Bukkit 엔티티 */
    private final Entity entity;

    /** 캐싱된 속성 */
    private Element cachedElement;
    /** 캐싱된 속성 강화 레벨 (-1은 미로딩 상태) */
    private int cachedEnhanceElement = 0;
    /** 캐싱된 공격력 강화 레벨 (-1은 미로딩 상태) */
    private int cachedEnhanceAttack = 0;
    /** 캐싱된 방어력 강화 레벨 (-1은 미로딩 상태) */
    private int cachedEnhanceDefense = 0;

    /**
     * NamespacedKey를 초기화한다.
     * 플러그인 로드 시 1회 호출해야 한다.
     *
     * @param instance 플러그인 인스턴스
     */
    public static void init(JavaPlugin instance) {
        elementKey = new NamespacedKey(instance, "element");
        enhanceElementKey = new NamespacedKey(instance, "enhance_element");
        enhanceAttackKey = new NamespacedKey(instance, "enhance_attack");
        enhanceDefenseKey = new NamespacedKey(instance, "enhance_defense");
    }

    /**
     * 주어진 Bukkit 엔티티를 속성 관리 객체로 래핑한다.
     *
     * @param entity 대상 엔티티
     */
    public ElementEntity(Entity entity) {
        this.entity = entity;
    }

    // --- 속성 관리 ---

    /**
     * 엔티티의 속성을 가져온다.
     * 캐시가 없으면 PersistentDataContainer에서 불러오며,
     * 값이 없거나 알 수 없을 경우 {@link Element#NORMAL}을 반환한다.
     *
     * @return 엔티티의 속성
     */
    public Element getElement() {
        if (cachedElement != null) return cachedElement;
        PersistentDataContainer data = entity.getPersistentDataContainer();
        String value = data.get(elementKey, PersistentDataType.STRING);
        if (value == null) return cachedElement = Element.NORMAL;
        Element e = ElementEntityHelper.getElementByName(value);
        return cachedElement = (e != null) ? e : Element.NORMAL;
    }

    /**
     * 엔티티의 속성을 설정한다.
     * 캐시와 PersistentDataContainer 모두 갱신된다.
     *
     * @param element 설정할 속성
     */
    public void setElement(Element element) {
        cachedElement = element;
        entity.getPersistentDataContainer().set(elementKey, PersistentDataType.STRING, element.getName());
    }

    // --- 속성 강화도 관리 ---

    /**
     * 속성 강화 레벨을 가져온다.
     * 캐시가 없으면 PersistentDataContainer에서 불러온다.
     *
     * @return 속성 강화 레벨 (기본값 0)
     */
    public int getEnhanceElement() {
        if (cachedEnhanceElement != -1) return cachedEnhanceElement;
        Integer val = entity.getPersistentDataContainer().get(enhanceElementKey, PersistentDataType.INTEGER);
        return cachedEnhanceElement = (val != null) ? val : 0;
    }

    /**
     * 속성 강화 레벨을 설정한다.
     * 0 미만은 자동으로 0으로 조정된다.
     *
     * @param level 강화 레벨
     */
    public void setEnhanceElement(int level) {
        cachedEnhanceElement = Math.max(0, level);
        entity.getPersistentDataContainer().set(enhanceElementKey, PersistentDataType.INTEGER, cachedEnhanceElement);
    }

    /**
     * 속성 강화 레벨을 증가시킨다.
     *
     * @param amount 증가량
     */
    public void addEnhanceElement(int amount) {
        setEnhanceElement(getEnhanceElement() + amount);
    }

    /**
     * 속성 강화 레벨을 감소시킨다.
     * 최소 0까지만 감소한다.
     *
     * @param amount 감소량
     */
    public void removeEnhanceElement(int amount) {
        setEnhanceElement(Math.max(0, getEnhanceElement() - amount));
    }

    // --- 공격력 강화도 관리 ---

    /**
     * 공격력 강화 레벨을 가져온다.
     *
     * @return 공격력 강화 레벨 (기본값 0)
     */
    public int getEnhanceAttack() {
        if (cachedEnhanceAttack != -1) return cachedEnhanceAttack;
        Integer val = entity.getPersistentDataContainer().get(enhanceAttackKey, PersistentDataType.INTEGER);
        return cachedEnhanceAttack = (val != null) ? val : 0;
    }

    /**
     * 공격력 강화 레벨을 설정한다.
     * 0 미만은 자동으로 0으로 조정된다.
     *
     * @param level 강화 레벨
     */
    public void setEnhanceAttack(int level) {
        cachedEnhanceAttack = Math.max(0, level);
        entity.getPersistentDataContainer().set(enhanceAttackKey, PersistentDataType.INTEGER, cachedEnhanceAttack);
    }

    /**
     * 공격력 강화 레벨을 증가시킨다.
     *
     * @param amount 증가량
     */
    public void addEnhanceAttack(int amount) {
        setEnhanceAttack(getEnhanceAttack() + amount);
    }

    /**
     * 공격력 강화 레벨을 감소시킨다.
     * 최소 0까지만 감소한다.
     *
     * @param amount 감소량
     */
    public void removeEnhanceAttack(int amount) {
        setEnhanceAttack(Math.max(0, getEnhanceAttack() - amount));
    }

    // --- 방어력 강화도 관리 ---

    /**
     * 방어력 강화 레벨을 가져온다.
     *
     * @return 방어력 강화 레벨 (기본값 0)
     */
    public int getEnhanceDefense() {
        if (cachedEnhanceDefense != -1) return cachedEnhanceDefense;
        Integer val = entity.getPersistentDataContainer().get(enhanceDefenseKey, PersistentDataType.INTEGER);
        return cachedEnhanceDefense = (val != null) ? val : 0;
    }

    /**
     * 방어력 강화 레벨을 설정한다.
     * 0 미만은 자동으로 0으로 조정된다.
     *
     * @param level 강화 레벨
     */
    public void setEnhanceDefense(int level) {
        cachedEnhanceDefense = Math.max(0, level);
        entity.getPersistentDataContainer().set(enhanceDefenseKey, PersistentDataType.INTEGER, cachedEnhanceDefense);
    }

    /**
     * 방어력 강화 레벨을 증가시킨다.
     *
     * @param amount 증가량
     */
    public void addEnhanceDefense(int amount) {
        setEnhanceDefense(getEnhanceDefense() + amount);
    }

    /**
     * 방어력 강화 레벨을 감소시킨다.
     * 최소 0까지만 감소한다.
     *
     * @param amount 감소량
     */
    public void removeEnhanceDefense(int amount) {
        setEnhanceDefense(Math.max(0, getEnhanceDefense() - amount));
    }

    // --- 변환 메서드 ---

    /**
     * 관리 대상 Bukkit Entity를 반환한다.
     *
     * @return 원본 Entity 객체
     */
    public Entity toEntity() {
        return entity;
    }

    /**
     * 관리 대상이 Player일 경우 반환하고,
     * 그렇지 않으면 null을 반환한다.
     *
     * @return Player 객체 또는 null
     */
    public Player toPlayer() {
        return (entity instanceof Player) ? (Player) entity : null;
    }

    /**
     * 일반 Entity를 ElementEntity로 변환한다.
     *
     * @param e 대상 Entity
     * @return ElementEntity 객체
     */
    public static ElementEntity toElementEntity(Entity e) {
        return new ElementEntity(e);
    }

    /**
     * Player를 ElementEntity로 변환한다.
     *
     * @param p 대상 Player
     * @return ElementEntity 객체
     */
    public static ElementEntity toElementEntity(Player p) {
        return new ElementEntity(p);
    }
}
