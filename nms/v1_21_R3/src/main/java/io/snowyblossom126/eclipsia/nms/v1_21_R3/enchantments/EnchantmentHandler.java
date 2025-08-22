package io.snowyblossom126.eclipsia.nms.v1_21_R3.enchantments;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import io.snowyblossom126.eclipsia.nms.NMSBridger;
import io.snowyblossom126.enchantapi.api.enchantments.CustomEnchantment;
import org.bukkit.enchantments.Enchantment;

public class EnchantmentHandler implements NMSBridger.ParseCustomEnchantment {

    @Override
    public Enchantment getBukkitEnchantment(CustomEnchantment customEnchantment) {
        var enchantmentRegistry = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT);
        return enchantmentRegistry.get(customEnchantment.getKey());
    }
}
