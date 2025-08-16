package io.lumpq126.eclipsia.events;

import io.lumpq126.eclipsia.core.mechanics.elements.Element;
import io.lumpq126.eclipsia.core.mechanics.entities.EclipsiaEntity;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;

/**
 * {@code ElementDamageEvent} 클래스는 속성(Element) 기반
 * 커스텀 데미지 이벤트를 처리하기 위한 이벤트입니다.
 * <p>
 * Bukkit의 {@link EntityDamageByEntityEvent}를 래핑하며,
 * 공격자와 피해자의 {@link EclipsiaEntity} 객체를 제공합니다.
 * 또한 공격에 사용된 {@link Element} 정보를 포함합니다.
 */
public class ElementDamageEvent extends Event {

    /** 이벤트 핸들러 리스트 (Bukkit 이벤트 필수) */
    private static final HandlerList HANDLERS = new HandlerList();

    /** 원본 엔티티 데미지 이벤트 */
    private final EntityDamageByEntityEvent event;

    /** 공격자 EclipsiaEntity */
    private final EclipsiaEntity damager;

    /** 피해자 EclipsiaEntity */
    private final EclipsiaEntity victim;

    /** 공격에 사용된 속성(Element) */
    private final Element attackElement;

    /**
     * 새로운 {@code ElementDamageEvent}를 생성합니다.
     *
     * @param event 원본 {@link EntityDamageByEntityEvent} 객체
     * @param attackElement 공격에 사용된 {@link Element}
     */
    public ElementDamageEvent(EntityDamageByEntityEvent event, Element attackElement) {
        this.event = event;
        this.damager = new EclipsiaEntity(event.getDamager());
        this.victim = new EclipsiaEntity(event.getEntity());
        this.attackElement = attackElement;
    }

    /**
     * 원본 {@link EntityDamageByEntityEvent}를 반환합니다.
     *
     * @return 원본 엔티티 데미지 이벤트
     */
    public EntityDamageByEntityEvent getEvent() {
        return event;
    }

    /**
     * 공격자 {@link Entity}를 반환합니다.
     *
     * @return 공격자 엔티티
     */
    public Entity getDamager() {
        return damager.toEntity();
    }

    /**
     * 피해자 {@link Entity}를 반환합니다.
     *
     * @return 피해자 엔티티
     */
    public Entity getVictim() {
        return victim.toEntity();
    }

    /**
     * 공격자 {@link EclipsiaEntity}의 속성(Element)을 반환합니다.
     *
     * @return 공격자 속성
     */
    public Element getDamagerElement() {
        return damager.getElement();
    }

    /**
     * 피해자 {@link EclipsiaEntity}의 속성(Element)을 반환합니다.
     *
     * @return 피해자 속성
     */
    public Element getVictimElement() {
        return victim.getElement();
    }

    /**
     * 공격자 {@link EclipsiaEntity} 객체를 반환합니다.
     *
     * @return 공격자 EclipsiaEntity
     */
    public EclipsiaEntity getEclipsiaDamager() {
        return damager;
    }

    /**
     * 피해자 {@link EclipsiaEntity} 객체를 반환합니다.
     *
     * @return 피해자 EclipsiaEntity
     */
    public EclipsiaEntity getEclipsiaVictim() {
        return victim;
    }

    /**
     * 공격에 사용된 {@link Element}를 반환합니다.
     *
     * @return 공격 속성
     */
    public Element getAttackElement() {
        return attackElement;
    }

    /**
     * Bukkit 이벤트 핸들러를 반환합니다.
     *
     * @return 핸들러 리스트
     */
    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    /**
     * Bukkit 이벤트 등록을 위한 정적 핸들러 리스트를 반환합니다.
     *
     * @return 핸들러 리스트
     */
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
