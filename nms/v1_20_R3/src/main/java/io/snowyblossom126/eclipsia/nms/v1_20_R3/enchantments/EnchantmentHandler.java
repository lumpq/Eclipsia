package io.snowyblossom126.eclipsia.nms.v1_20_R3.enchantments;

import io.snowyblossom126.eclipsia.nms.NMSBridger;
import io.snowyblossom126.enchantapi.api.enchantments.CustomEnchantment;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;

public class EnchantmentHandler implements NMSBridger.ParseCustomEnchantment {

    @Override
    public Enchantment getBukkitEnchantment(CustomEnchantment customEnchantment) {
        return Registry.ENCHANTMENT.get(customEnchantment.getKey());
    }
}
