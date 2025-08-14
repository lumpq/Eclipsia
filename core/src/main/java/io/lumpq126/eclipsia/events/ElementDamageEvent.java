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
 */
public class ElementDamageEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final EntityDamageByEntityEvent originalEvent;
    private final EclipsiaEntity damager;
    private final EclipsiaEntity victim;
    private final Element attackElement;

    /**
     * 새로운 속성 데미지 이벤트를 생성합니다.
     *
     * @param event         원본 {@link EntityDamageByEntityEvent}
     * @param attackElement 공격에 사용된 {@link Element}
     */
    public ElementDamageEvent(EntityDamageByEntityEvent event, Element attackElement) {
        this.originalEvent = event;
        this.damager = new EclipsiaEntity(event.getDamager());
        this.victim = new EclipsiaEntity(event.getEntity());
        this.attackElement = attackElement;
    }

    public EntityDamageByEntityEvent getOriginalEvent() {
        return originalEvent;
    }

    public Entity getDamager() {
        return damager.toEntity();
    }

    public Entity getVictim() {
        return victim.toEntity();
    }

    public Element getDamagerElement() {
        return damager.getElement();
    }

    public Element getVictimElement() {
        return victim.getElement();
    }

    public EclipsiaEntity getEclipsiaDamager() {
        return damager;
    }

    public EclipsiaEntity getEclipsiaVictim() {
        return victim;
    }

    public Element getAttackElement() {
        return attackElement;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
