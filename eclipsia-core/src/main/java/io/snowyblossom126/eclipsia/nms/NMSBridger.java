package io.snowyblossom126.eclipsia.nms;

import io.snowyblossom126.enchantapi.api.enchantments.CustomEnchantment;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public interface NMSBridger {

    interface NMSInventoryHandler {
        Inventory createInventory(Player player, int size, String name);
    }

    interface ParseCustomEnchantment {
        Enchantment getBukkitEnchantment(CustomEnchantment customEnchantment);
    }

    NMSInventoryHandler getInventoryHandler();

    ParseCustomEnchantment getEnchantmentParser();
}
