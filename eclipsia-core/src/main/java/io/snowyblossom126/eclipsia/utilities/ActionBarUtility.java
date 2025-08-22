package io.snowyblossom126.eclipsia.utilities;

import org.bukkit.entity.Player;

public class ActionBarUtility {

    public static void sendActionBar(Player player, String message) {
        SendActionbar.send(player, message);
    }
}
