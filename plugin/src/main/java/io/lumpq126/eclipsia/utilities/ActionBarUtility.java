package io.lumpq126.eclipsia.utilities;

import io.lumpq126.eclipsia.EclipsiaPlugin;
import io.lumpq126.eclipsia.nms.NMSHandler;
import org.bukkit.entity.Player;

public class ActionBarUtility {
    private static final NMSHandler NMS = EclipsiaPlugin.getNms();

    public static void sendActionBar(Player player, String message) {
        NMS.sendActionBar(player, message);
    }
}
