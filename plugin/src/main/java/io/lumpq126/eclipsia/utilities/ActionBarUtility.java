package io.lumpq126.eclipsia.utilities;

import io.lumpq126.eclipsia.protocol.packets.SendActionBarToPacket;
import org.bukkit.entity.Player;

public class ActionBarUtility {

    public static void sendActionBar(Player player, String message) {
        SendActionBarToPacket.send(player, message);
    }
}
