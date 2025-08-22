package io.snowyblossom126.eclipsia.enchantments;

import io.snowyblossom126.eclipsia.EclipsiaPlugin;
import io.snowyblossom126.enchantapi.api.enchantments.CustomEnchantment;
import io.snowyblossom126.enchantapi.api.enchantments.properties.Rarity;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class Vampirism extends CustomEnchantment {

    public Vampirism() {
        super(new Builder("vampirism", "Vampirism", 3) {
            @Override
            public CustomEnchantment build() {
                return new Vampirism(this);
            }
        }
        .anvilCost(15)
                .applicableSlots(EquipmentSlot.HAND)
                .weight(5)
                .rarity(Rarity.COMMON)
                .enchantmentTarget(EnchantmentTarget.WEAPON)
                .treasure(false)
                .cursed(false)
                .canTrade(true)
                .discoverable(true)
                .minCost(1, 11)
                .maxCost(20, 11));
    }

    @Override
    public boolean canEnchant(ItemStack itemStack) {
        return false;
    }

    @Override
    public Enchantment parseToBukkitEnchantment() {
        return EclipsiaPlugin.getNms().getEnchantmentParser().getBukkitEnchantment(this);
    }

    private Vampirism(Builder builder) {
        super(builder);
    }
}
