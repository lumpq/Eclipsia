package io.snowyblossom126.eclipsia.utilities;

import io.snowyblossom126.eclipsia.EclipsiaPlugin;
import io.snowyblossom126.eclipsia.nms.NMSHandler;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class InventoryUtility {
    private static final NMSHandler NMS = EclipsiaPlugin.getNms();

    public static Inventory inventory(Player owner, int size, String name) {
        return NMS.createInventory(owner, size, name);
    }
}
