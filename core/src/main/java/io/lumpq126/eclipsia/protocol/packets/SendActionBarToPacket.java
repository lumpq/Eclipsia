package io.lumpq126.eclipsia.protocol.packets;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;

public class SendActionBarToPacket {

    public static void send(Player player, String message) {
        if (player == null || message == null) return;

        Component component = MiniMessage.miniMessage().deserialize(message);
        player.sendActionBar(component);
    }
}
