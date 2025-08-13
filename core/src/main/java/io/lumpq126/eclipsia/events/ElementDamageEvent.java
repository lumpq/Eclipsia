package io.lumpq126.eclipsia.events;

import io.lumpq126.eclipsia.elements.Element;
import io.lumpq126.eclipsia.elements.ElementEntity;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;

/**
 * 속성 시스템을 기반으로 한 커스텀 데미지 이벤트.
 * <p>
 * 원본 {@link EntityDamageByEntityEvent}를 포함하며,
 * 공격자/피격자의 {@link ElementEntity} 정보와
 * 공격에 사용된 {@link Element} 속성을 제공한다.
 */
public class ElementDamageEvent extends Event {

    /** Bukkit 이벤트 시스템에 필요한 HandlerList */
    private static final HandlerList HANDLERS = new HandlerList();

    /** 원본 Bukkit 데미지 이벤트 */
    private final EntityDamageByEntityEvent originalEvent;

    /** 속성 정보가 포함된 공격자 */
    private final ElementEntity damager;
    /** 속성 정보가 포함된 피격자 */
    private final ElementEntity victim;

    /** 이번 공격에 사용된 속성 (무기나 스킬에 따라 달라질 수 있음) */
    private final Element attackElement;

    /**
     * 새로운 속성 데미지 이벤트를 생성한다.
     * <p>
     * 공격 속성은 반드시 명시해야 하며,
     * 공격자와 피격자는 {@link ElementEntity}로 래핑된다.
     *
     * @param event         원본 {@link EntityDamageByEntityEvent}
     * @param attackElement 공격에 사용된 속성
     */
    public ElementDamageEvent(EntityDamageByEntityEvent event, Element attackElement) {
        this.originalEvent = event;
        this.damager = ElementEntity.toElementEntity(event.getDamager());
        this.victim = ElementEntity.toElementEntity(event.getEntity());
        this.attackElement = attackElement;
    }

    /**
     * 원본 Bukkit 이벤트를 반환한다.
     *
     * @return {@link EntityDamageByEntityEvent}
     */
    public EntityDamageByEntityEvent getOriginalEvent() {
        return originalEvent;
    }

    /**
     * 공격자 엔티티를 반환한다.
     *
     * @return 공격자 {@link Entity}
     */
    public Entity getDamager() {
        return damager.toEntity();
    }

    /**
     * 피격자 엔티티를 반환한다.
     *
     * @return 피격자 {@link Entity}
     */
    public Entity getVictim() {
        return victim.toEntity();
    }

    /**
     * 공격자의 속성을 반환한다.
     *
     * @return {@link Element}
     */
    public Element getDamagerElement() {
        return damager.getElement();
    }

    /**
     * 피격자의 속성을 반환한다.
     *
     * @return {@link Element}
     */
    public Element getVictimElement() {
        return victim.getElement();
    }

    /**
     * 속성 정보를 포함한 공격자 객체를 반환한다.
     *
     * @return {@link ElementEntity}
     */
    public ElementEntity getElementDamager() {
        return damager;
    }

    /**
     * 속성 정보를 포함한 피격자 객체를 반환한다.
     *
     * @return {@link ElementEntity}
     */
    public ElementEntity getElementVictim() {
        return victim;
    }

    /**
     * 공격에 실제 사용된 속성을 반환한다.
     * <p>
     * 이는 공격자의 기본 속성과 다를 수 있다.
     *
     * @return {@link Element}
     */
    public Element getAttackElement() {
        return attackElement;
    }

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
