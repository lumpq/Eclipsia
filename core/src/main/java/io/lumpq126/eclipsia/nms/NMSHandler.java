package io.lumpq126.eclipsia.nms;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public interface NMSHandler {

    Inventory createInventory(Player player, int size, String name);
}
