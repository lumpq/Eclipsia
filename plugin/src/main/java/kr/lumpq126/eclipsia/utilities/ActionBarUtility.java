package kr.lumpq126.eclipsia.utilities;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class ActionBarUtility {
    public static void sendActionBar(Player player, String message) {
        player.sendActionBar(Component.text(message));
    }
}
