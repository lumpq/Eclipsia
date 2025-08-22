package io.snowyblossom126.eclipsia.nms.v1_20_R1.enchantments;

import io.snowyblossom126.eclipsia.nms.NMSBridger;
import io.snowyblossom126.enchantapi.api.enchantments.CustomEnchantment;
import org.bukkit.enchantments.Enchantment;

public class EnchantmentHandler implements NMSBridger.ParseCustomEnchantment {

    @Override
    public Enchantment getBukkitEnchantment(CustomEnchantment customEnchantment) {
        return Enchantment.getByKey(customEnchantment.getKey());
    }
}
