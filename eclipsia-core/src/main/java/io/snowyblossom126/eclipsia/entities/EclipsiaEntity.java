package io.snowyblossom126.eclipsia.entities;

import io.snowyblossom126.eclipsia.classes.ClassStorage;
import io.snowyblossom126.eclipsia.elements.Normal;
import io.snowyblossom126.eclipsia.stats.Stat;
import io.snowyblossom126.eclipsia.events.PlayerExpUpEvent;
import io.snowyblossom126.eclipsia.events.PlayerLevelUpEvent;
import io.snowyblossom126.elementapi.api.ElementAPI;
import io.snowyblossom126.elementapi.api.elements.Element;
import io.snowyblossom126.eclipsia.classes.Class;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

/**
 * {@code EclipsiaEntity}는 Bukkit {@link Entity}를 래핑하여 RPG 스타일의
 * 레벨, 경험치, 스탯, 속성(Element), 직업, SIA(통화/포인트) 등을 관리할 수 있는 클래스입니다.
 * <p>
 * 내부적으로 {@link org.bukkit.persistence.PersistentDataContainer}를 사용하여
 * 서버 재시작 후에도 데이터가 유지됩니다.
 * </p>
 *
 * @since 1.0
 * @see org.bukkit.entity.Entity
 * @see org.bukkit.persistence.PersistentDataContainer
 */
public class EclipsiaEntity {

    /** 플러그인 인스턴스 (PDC NamespacedKey 생성용) */
    private static Plugin pluginInstance;

    /** 관리 대상 Bukkit 엔티티 */
    private final Entity entity;

    // PDC 키
    private final NamespacedKey elementKey;
    private final NamespacedKey enhanceElementKey;
    private final NamespacedKey enhanceAttackKey;
    private final NamespacedKey enhanceDefenseKey;
    private final NamespacedKey statPointsKey;
    private final NamespacedKey levelKey;
    private final NamespacedKey expKey;
    private final NamespacedKey siaKey;
    private final NamespacedKey classKey;
    private final NamespacedKey classStageKey;
    private final NamespacedKey professionProficiencyKey;

    /** 최대 레벨 */
    private static final int MAX_LEVEL = 999;
    /** 초기 레벨 */
    private static final int INITIAL_LEVEL = 1;
    /** 레벨당 필요 경험치 배수 */
    private static final double EXP_PER_LEVEL_MULTIPLIER = 100.0;
    /** 초기 SIA 값 */
    private static final int INITIAL_SIA = 10000;

    // ====================================================
    // 플러그인 초기화
    // ====================================================

    /**
     * 플러그인 인스턴스를 등록합니다.
     * <p>
     * 반드시 {@code EclipsiaEntity} 인스턴스를 생성하기 전에 호출해야 합니다.
     * </p>
     *
     * @param plugin Bukkit 플러그인 인스턴스
     * @since 1.0
     */
    public static void init(Plugin plugin) {
        pluginInstance = plugin;
    }

    // ====================================================
    // 생성자
    // ====================================================

    /**
     * 지정된 {@link Entity}를 관리하는 {@code EclipsiaEntity} 인스턴스를 생성합니다.
     * <p>
     * 초기 PDC 값이 없으면 기본값으로 자동 설정됩니다.
     * </p>
     *
     * @param entity 관리할 Bukkit 엔티티
     * @throws IllegalStateException {@code init()} 호출 전이면 예외 발생
     * @since 1.0
     */
    public EclipsiaEntity(Entity entity) {
        if (pluginInstance == null)
            throw new IllegalStateException("EclipsiaEntity.init(plugin) must be called before creating instances.");
        this.entity = entity;

        // PDC 키 초기화
        this.elementKey = new NamespacedKey(pluginInstance, "element");
        this.enhanceElementKey = new NamespacedKey(pluginInstance, "enhance_element");
        this.enhanceAttackKey = new NamespacedKey(pluginInstance, "enhance_attack");
        this.enhanceDefenseKey = new NamespacedKey(pluginInstance, "enhance_defense");
        this.statPointsKey = new NamespacedKey(pluginInstance, "stat_points");
        this.levelKey = new NamespacedKey(pluginInstance, "level");
        this.expKey = new NamespacedKey(pluginInstance, "exp");
        this.siaKey = new NamespacedKey(pluginInstance, "sia");
        this.classKey = new NamespacedKey(pluginInstance, "player_class");
        this.classStageKey = new NamespacedKey(pluginInstance, "class_stage");
        this.professionProficiencyKey = new NamespacedKey(pluginInstance, "profession_proficiency");

        // 초기값 설정
        if (!hasData(professionProficiencyKey, PersistentDataType.INTEGER)) setProfessionProficiency(0);
        if (!hasData(classKey, PersistentDataType.STRING))
            setClass(Class.NOVICE, 0);
        if (!hasData(levelKey, PersistentDataType.INTEGER)) setLevel(INITIAL_LEVEL);
        if (!hasData(expKey, PersistentDataType.DOUBLE)) setExp(0);
        if (!hasData(siaKey, PersistentDataType.INTEGER)) setSia(INITIAL_SIA);
    }

    // ====================================================
    // 엔티티 반환
    // ====================================================

    /**
     * 관리 중인 원본 {@link Entity}를 반환합니다.
     *
     * @return 관리 대상 Bukkit 엔티티
     * @since 1.0
     */
    public Entity toEntity() {
        return entity;
    }

    /**
     * 엔티티가 {@link Player}인 경우 캐스팅하여 반환합니다.
     *
     * @return Player 또는 null
     * @since 1.0
     */
    public Player toPlayer() {
        return (entity instanceof Player p) ? p : null;
    }

    // ====================================================
    // SIA 관리
    // ====================================================

    /**
     * 현재 SIA 값을 반환합니다.
     *
     * @return SIA 값
     * @since 1.0
     */
    public int getSia() {
        return getIntData(siaKey, INITIAL_SIA);
    }

    /**
     * SIA 값을 설정합니다. 0 미만은 0으로 처리됩니다.
     *
     * @param amount 설정할 SIA 값
     * @since 1.0
     */
    public void setSia(int amount) {
        setIntData(siaKey, Math.max(0, amount));
    }

    /**
     * SIA를 증가시킵니다.
     *
     * @param amount 증가할 양
     * @since 1.0
     */
    public void addSia(int amount) {
        setSia(getSia() + amount);
    }

    /**
     * SIA를 감소시킵니다. 0 미만으로 내려가지 않습니다.
     *
     * @param amount 감소할 양
     * @since 1.0
     */
    public void removeSia(int amount) {
        setSia(Math.max(0, getSia() - amount));
    }

    // ====================================================
    // 레벨 / 경험치 관리
    // ====================================================

    /**
     * 현재 레벨을 반환합니다.
     *
     * @return 현재 레벨
     * @since 1.0
     */
    public int getLevel() {
        return getIntData(levelKey, INITIAL_LEVEL);
    }

    /**
     * 레벨을 설정합니다. 범위는 1~MAX_LEVEL 입니다.
     * 레벨 상승 시 {@link PlayerLevelUpEvent}가 호출됩니다.
     *
     * @param level 새 레벨
     * @since 1.0
     * @see PlayerLevelUpEvent
     */
    public void setLevel(int level) {
        int clamped = Math.min(MAX_LEVEL, Math.max(1, level));
        int oldLevel = getLevel();
        setIntData(levelKey, clamped);

        if (clamped > oldLevel && toPlayer() != null)
            Bukkit.getPluginManager().callEvent(new PlayerLevelUpEvent(toPlayer(), oldLevel, clamped));
    }

    /**
     * 현재 레벨을 지정한 양만큼 증가시킵니다.
     *
     * @param amount 증가할 레벨 수
     * @since 1.0
     */
    public void addLevel(int amount) {
        setLevel(getLevel() + amount);
    }

    /**
     * 레벨, 경험치, 스탯, 스탯 포인트를 초기값으로 리셋합니다.
     *
     * @since 1.0
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
     * 현재 경험치를 반환합니다.
     *
     * @return 경험치
     * @since 1.0
     */
    public double getExp() {
        return getDoubleData(expKey);
    }

    /**
     * 경험치를 설정하고 필요한 경우 자동으로 레벨업을 처리합니다.
     * 초과 경험치는 다음 레벨로 이월됩니다.
     *
     * @param exp 설정할 경험치 (음수면 0)
     * @since 1.0
     * @see PlayerExpUpEvent
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

        if (newLevel > currentLevel) setLevel(newLevel);

        double oldExp = getExp();
        setDoubleData(expKey, currentExp);

        if (toPlayer() != null)
            Bukkit.getPluginManager().callEvent(new PlayerExpUpEvent(toPlayer(), oldExp, currentExp, newLevel));
    }

    /**
     * 경험치를 지정량만큼 증가시킵니다.
     *
     * @param amount 추가할 경험치
     * @since 1.0
     */
    public void addExp(double amount) {
        setExp(getExp() + amount);
    }

    /**
     * 특정 레벨에서 다음 레벨로 가기 위해 필요한 경험치를 계산합니다.
     *
     * @param level 기준 레벨
     * @return 필요 경험치
     * @since 1.0
     */
    public double getRequiredExp(int level) {
        return level >= MAX_LEVEL ? 0.0 : level * EXP_PER_LEVEL_MULTIPLIER;
    }

    // ====================================================
    // Element / 강화 관리
    // ====================================================

    /**
     * 현재 속성을 반환합니다. 없으면 {@link Normal} 반환.
     *
     * @return Element
     * @since 1.0
     */
    public Element getElement() {
        String name = getStringData(elementKey);
        return ElementAPI.getInstance()
                .getElement(name)
                .orElse(Normal.INSTANCE());
    }

    /**
     * 속성을 설정합니다. null 이면 제거됩니다.
     *
     * @param element 설정할 Element
     * @since 1.0
     */
    public void setElement(Element element) {
        if (element != null) setStringData(elementKey, element.getId());
        else removeData(elementKey);
    }

    /**
     * 속성 존재 여부
     *
     * @return true/false
     * @since 1.0
     */
    public boolean hasElement() {
        return hasData(elementKey, PersistentDataType.STRING);
    }

    // 속성 강화
    public int getEnhanceElement() { return getIntData(enhanceElementKey, 0); }
    public void setEnhanceElement(int level) { setIntData(enhanceElementKey, Math.max(0, level)); }
    public void addEnhanceElement(int amount) { setEnhanceElement(getEnhanceElement() + amount); }

    public int getEnhanceAttack() { return getIntData(enhanceAttackKey, 0); }
    public void setEnhanceAttack(int level) { setIntData(enhanceAttackKey, Math.max(0, level)); }
    public void addEnhanceAttack(int amount) { setEnhanceAttack(getEnhanceAttack() + amount); }

    public int getEnhanceDefense() { return getIntData(enhanceDefenseKey, 0); }
    public void setEnhanceDefense(int level) { setIntData(enhanceDefenseKey, Math.max(0, level)); }
    public void addEnhanceDefense(int amount) { setEnhanceDefense(getEnhanceDefense() + amount); }

    // ====================================================
    // 스탯 / 스탯 포인트 관리
    // ====================================================

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

    public int getStatPoints() { return getIntData(statPointsKey, Stat.INITIAL_POINT); }
    public void setStatPoints(int points) { setIntData(statPointsKey, Math.max(0, points)); }
    public void addStatPoints(int amount) { setStatPoints(getStatPoints() + amount); }
    public void removeStatPoints(int amount) { setStatPoints(Math.max(0, getStatPoints() - amount)); }

    // ====================================================
    // 직업 / 숙련도 관리
    // ====================================================

    public ClassStorage.ClassInfo getClassInfo() {
        String className = getStringData(classKey);
        int stage = getIntData(classStageKey, 0);
        Class clazz = Class.fromNameOrDefault(className);
        return new ClassStorage.ClassInfo(clazz, stage);
    }

    public void setClass(Class clazz, int stage) {
        setStringData(classKey, clazz.name());
        setIntData(classStageKey, Math.max(0, stage));
    }

    public record ClassInfo(Class clazz, int stage) { }

    public int getProfessionProficiency() { return getIntData(professionProficiencyKey, 100); }
    public void setProfessionProficiency(int value) { setIntData(professionProficiencyKey, Math.max(0, value)); }
    public void addProfessionProficiency(int amount) { setProfessionProficiency(getProfessionProficiency() + amount); }
    public void removeProfessionProficiency(int amount) { setProfessionProficiency(getProfessionProficiency() - amount); }

    // ====================================================
    // 내부 PDC 유틸
    // ====================================================

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
