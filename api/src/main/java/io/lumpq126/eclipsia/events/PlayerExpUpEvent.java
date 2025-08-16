package io.lumpq126.eclipsia.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * 플레이어 경험치가 증가할 때 호출되는 커스텀 이벤트.
 * <p>
 * Bukkit의 {@link Event}를 상속하며, {@link Cancellable}을 구현하여
 * 경험치 증가 동작을 취소할 수 있다.
 * <p>
 * 경험치 변경 전/후 값과 현재 레벨 정보를 제공한다.
 */
public class PlayerExpUpEvent extends Event implements Cancellable {

    /** Bukkit 이벤트 시스템에 필요한 HandlerList */
    private static final HandlerList HANDLERS = new HandlerList();

    /** 경험치가 변한 플레이어 */
    private final Player player;

    /** 경험치 변경 전 값 */
    private final double oldExp;

    /** 경험치 변경 후 값 */
    private final double newExp;

    /** 플레이어의 현재 레벨 */
    private final int level;

    /** 이벤트 취소 여부 */
    private boolean cancelled;

    /**
     * 새로운 경험치 증가 이벤트를 생성한다.
     *
     * @param player 경험치가 변한 플레이어
     * @param oldExp 변경 전 경험치 값
     * @param newExp 변경 후 경험치 값
     * @param level  현재 레벨
     */
    public PlayerExpUpEvent(@NotNull Player player, double oldExp, double newExp, int level) {
        this.player = player;
        this.oldExp = oldExp;
        this.newExp = newExp;
        this.level = level;
    }

    /**
     * 경험치가 변한 플레이어를 반환한다.
     *
     * @return {@link Player}
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * 플레이어의 현재 레벨을 반환한다.
     *
     * @return 현재 레벨
     */
    public int getLevel() {
        return level;
    }

    /**
     * 경험치 변경 전 값을 반환한다.
     *
     * @return 이전 경험치 값
     */
    public double getOldExp() {
        return oldExp;
    }

    /**
     * 경험치 변경 후 값을 반환한다.
     *
     * @return 새로운 경험치 값
     */
    public double getNewExp() {
        return newExp;
    }

    /**
     * 이벤트 취소 여부를 반환한다.
     *
     * @return 취소 상태
     */
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * 이벤트를 취소하거나 취소 해제한다.
     * <p>
     * 취소하면 경험치 증가 동작이 적용되지 않아야 한다.
     *
     * @param cancelled true로 설정 시 이벤트 취소
     */
    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
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
