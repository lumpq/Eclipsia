package kr.lumpq126.eclipsia.nms;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public interface NMSHandler {
    Inventory createInventory(Player owner, int size, String title);
    void sendActionBar(Player player, String message);
}
