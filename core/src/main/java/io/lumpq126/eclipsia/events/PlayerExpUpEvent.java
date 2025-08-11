package io.lumpq126.eclipsia.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerExpUpEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final double oldExp;
    private final double newExp;
    private final int level;
    private boolean cancelled;

    public PlayerExpUpEvent(@NotNull Player player, double oldExp, double newExp, int level) {
        this.player = player;
        this.oldExp = oldExp;
        this.newExp = newExp;
        this.level = level;
    }

    public Player getPlayer() {
        return player;
    }

    public int getLevel() {
        return level;
    }

    public double getOldExp() {
        return oldExp;
    }

    public double getNewExp() {
        return newExp;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
