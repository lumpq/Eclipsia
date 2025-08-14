package io.lumpq126.eclipsia.utilities;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;

public class SendActionbar {

    public static void send(Player player, String message) {
        if (player == null || message == null) return;

        Component component = MiniMessage.miniMessage().deserialize(message);
        player.sendActionBar(component);
    }
}
