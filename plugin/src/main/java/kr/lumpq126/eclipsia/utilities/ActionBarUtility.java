package kr.lumpq126.eclipsia.utilities;

import kr.lumpq126.eclipsia.nms.NMSHandler;
import kr.lumpq126.eclipsia.nms.NMSHandlerFactory;
import org.bukkit.entity.Player;

public class ActionBarUtility {
    private static final NMSHandler NMS = NMSHandlerFactory.loadNMS();

    public static void sendActionBar(Player player, String message) {
        NMS.sendActionBar(player, message);
    }
}
