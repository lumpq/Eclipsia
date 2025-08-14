package io.lumpq126.eclipsia.utilities;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;

/**
 * 플레이어에게 액션바 메시지를 전송하는 유틸리티 클래스
 */
public class SendActionbar {

    /**
     * 플레이어에게 액션바 메시지를 전송합니다.
     *
     * @param player  대상 플레이어
     * @param message 전송할 MiniMessage 문자열
     */
    public static void send(Player player, String message) {
        if (player == null || message == null) return;

        Component component = MiniMessage.miniMessage().deserialize(message);
        player.sendActionBar(component);
    }
}
