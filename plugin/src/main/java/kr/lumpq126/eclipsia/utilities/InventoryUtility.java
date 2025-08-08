package kr.lumpq126.eclipsia.utilities;

import kr.lumpq126.eclipsia.nms.NMSHandler;
import kr.lumpq126.eclipsia.nms.NMSHandlerFactory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class InventoryUtility {
    private static final NMSHandler NMS = NMSHandlerFactory.loadNMS();

    public static Inventory inventory(Player owner, int size, String name) {
        return NMS.createInventory(owner, size, name);
    }
}
