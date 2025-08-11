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

    public ElementDamageEvent(EntityDamageByEntityEvent event) {
        this.originalEvent = event;
        this.damager = new ElementEntity(event.getDamager());
        this.victim = new ElementEntity(event.getEntity());
    }

    public EntityDamageByEntityEvent getOriginalEvent() {
        return originalEvent;
    }

    public Entity getDamager() {
        return originalEvent.getDamager();
    }

    public Entity getVictim() {
        return originalEvent.getEntity();
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

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
