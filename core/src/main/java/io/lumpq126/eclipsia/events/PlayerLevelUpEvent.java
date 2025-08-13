package io.lumpq126.eclipsia.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * 플레이어가 레벨업했을 때 호출되는 커스텀 이벤트.
 * <p>
 * 플레이어의 이전 레벨과 새 레벨 정보를 제공한다.
 * 이 이벤트는 취소할 수 없다.
 */
public class PlayerLevelUpEvent extends Event {

    /** Bukkit 이벤트 시스템에 필요한 HandlerList */
    private static final HandlerList HANDLERS = new HandlerList();

    /** 레벨업한 플레이어 */
    private final Player player;

    /** 레벨업 전 플레이어 레벨 */
    private final int oldLevel;

    /** 레벨업 후 플레이어 레벨 */
    private final int newLevel;

    /**
     * 새로운 플레이어 레벨업 이벤트를 생성한다.
     *
     * @param player   레벨업한 플레이어
     * @param oldLevel 레벨업 전 레벨
     * @param newLevel 레벨업 후 레벨
     */
    public PlayerLevelUpEvent(Player player, int oldLevel, int newLevel) {
        this.player = player;
        this.oldLevel = oldLevel;
        this.newLevel = newLevel;
    }

    /**
     * 레벨업한 플레이어를 반환한다.
     *
     * @return {@link Player}
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * 레벨업 전 플레이어의 레벨을 반환한다.
     *
     * @return 이전 레벨
     */
    public int getOldLevel() {
        return oldLevel;
    }

    /**
     * 레벨업 후 플레이어의 레벨을 반환한다.
     *
     * @return 새 레벨
     */
    public int getNewLevel() {
        return newLevel;
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
