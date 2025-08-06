package kr.lumpq126.eclipsia.utilities;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class InventoryUtility {

    public static Inventory inventory(Player owner, int size, String name) {
        return Bukkit.createInventory(owner, size, MiniMessage.miniMessage().deserialize(name));
    }
}
