package io.lumpq126.eclipsia.events;

import io.lumpq126.eclipsia.elements.Element;
import io.lumpq126.eclipsia.entities.EclipsiaEntity;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;

/**
 * 속성 시스템 기반 커스텀 데미지 이벤트 클래스.
 * <p>
 * 원본 {@link EntityDamageByEntityEvent}를 포함하며,
 * 공격자와 피격자의 {@link EclipsiaEntity} 정보를 제공하고,
 * 공격에 사용된 {@link Element} 속성을 전달합니다.
 */
public class ElementDamageEvent extends Event {

    /** Bukkit 이벤트 핸들러 리스트 */
    private static final HandlerList HANDLERS = new HandlerList();

    /** 원본 Bukkit 데미지 이벤트 */
    private final EntityDamageByEntityEvent originalEvent;

    /** 속성 정보를 가진 공격자 */
    private final EclipsiaEntity damager;

    /** 속성 정보를 가진 피격자 */
    private final EclipsiaEntity victim;

    /** 이번 공격에 사용된 속성 */
    private final Element attackElement;

    /**
     * 새로운 속성 데미지 이벤트를 생성합니다.
     *
     * @param event         원본 {@link EntityDamageByEntityEvent}
     * @param attackElement 공격에 사용된 {@link Element}
     */
    public ElementDamageEvent(EntityDamageByEntityEvent event, Element attackElement) {
        this.originalEvent = event;
        this.damager = new EclipsiaEntity(event.getDamager().getServer()
                .getPluginManager().getPlugin("Eclipsia"), event.getDamager());
        this.victim = new EclipsiaEntity(event.getEntity().getServer()
                .getPluginManager().getPlugin("Eclipsia"), event.getEntity());
        this.attackElement = attackElement;
    }

    /**
     * 원본 Bukkit 이벤트를 반환합니다.
     *
     * @return {@link EntityDamageByEntityEvent} 원본 이벤트
     */
    public EntityDamageByEntityEvent getOriginalEvent() {
        return originalEvent;
    }

    /**
     * 공격자 {@link Entity}를 반환합니다.
     *
     * @return 공격자 Bukkit 엔티티
     */
    public Entity getDamager() {
        return damager.toEntity();
    }

    /**
     * 피격자 {@link Entity}를 반환합니다.
     *
     * @return 피격자 Bukkit 엔티티
     */
    public Entity getVictim() {
        return victim.toEntity();
    }

    /**
     * 공격자의 속성 {@link Element}를 반환합니다.
     * <p>
     * 존재하지 않으면 기본 속성 {@link Element#NORMAL}을 반환합니다.
     *
     * @return 공격자 속성
     */
    public Element getDamagerElement() {
        return damager.getElement(); // 항상 Element 반환
    }

    /**
     * 피격자의 속성 {@link Element}를 반환합니다.
     * <p>
     * 존재하지 않으면 기본 속성 {@link Element#NORMAL}을 반환합니다.
     *
     * @return 피격자 속성
     */
    public Element getVictimElement() {
        return victim.getElement(); // 항상 Element 반환
    }

    /**
     * 속성 정보를 포함한 공격자 객체를 반환합니다.
     *
     * @return {@link EclipsiaEntity} 공격자
     */
    public EclipsiaEntity getEclipsiaDamager() {
        return damager;
    }

    /**
     * 속성 정보를 포함한 피격자 객체를 반환합니다.
     *
     * @return {@link EclipsiaEntity} 피격자
     */
    public EclipsiaEntity getEclipsiaVictim() {
        return victim;
    }

    /**
     * 공격에 실제 사용된 속성을 반환합니다.
     *
     * @return 공격 속성 {@link Element}
     */
    public Element getAttackElement() {
        return attackElement;
    }

    /**
     * Bukkit 이벤트 시스템에서 호출되는 핸들러 리스트를 반환합니다.
     *
     * @return {@link HandlerList}
     */
    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    /**
     * Bukkit 이벤트 시스템에서 요구하는 정적 메서드.
     *
     * @return {@link HandlerList}
     */
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
