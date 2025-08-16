package io.lumpq126.eclipsia.nms;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public interface NMSHandler {

    Inventory createInventory(Player player, int size, String name);
}
