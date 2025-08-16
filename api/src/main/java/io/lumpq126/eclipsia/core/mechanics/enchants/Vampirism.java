package io.lumpq126.eclipsia.core.mechanics.enchants;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class Vampirism {
    private static final NamespacedKey key = NamespacedKey.minecraft("vampirism");
    private static final Enchantment enchantment = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT).get(key);

    public static ItemStack get() {
        ItemStack i = ItemStack.of(Material.IRON_SWORD);
        if (enchantment == null) return null;
        i.getItemMeta().addEnchant(enchantment, 3, false);
        return i;
    }
}
