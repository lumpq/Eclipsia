package kr.lumpq126.eclipsia.utilities;

import kr.lumpq126.eclipsia.EclipsiaPlugin;
import kr.lumpq126.eclipsia.nms.NMSHandler;
import org.bukkit.entity.Player;

public class ActionBarUtility {
    private static final NMSHandler NMS = EclipsiaPlugin.getNms();

    public static void sendActionBar(Player player, String message) {
        NMS.sendActionBar(player, message);
    }
}
