package io.lumpq126.eclipsia.events;

import io.lumpq126.eclipsia.elements.Element;
import io.lumpq126.eclipsia.elements.ElementEntity;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;

public class ElementDamageEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    private final EntityDamageByEntityEvent originalEvent;
    private final ElementEntity damager;
    private final ElementEntity victim;
    private final Element attackElement; // 공격에 사용된 속성

    // 새 버전: 반드시 공격 속성을 명시
    public ElementDamageEvent(EntityDamageByEntityEvent event, Element attackElement) {
        this.originalEvent = event;
        this.damager = ElementEntity.parseEntity(event.getDamager());
        this.victim = ElementEntity.parseEntity(event.getEntity());
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

    public ElementEntity getElementDamager() {
        return damager;
    }

    public ElementEntity getElementVictim() {
        return victim;
    }

    // 공격에 사용된 속성 반환
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