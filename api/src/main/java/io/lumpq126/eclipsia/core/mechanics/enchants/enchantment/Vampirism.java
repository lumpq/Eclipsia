package io.lumpq126.eclipsia.core.mechanics.enchants.enchantment;

import io.lumpq126.enchantAPI.api.enchantment.CustomEnchantment;
import io.lumpq126.enchantAPI.api.enchantment.properties.Rarity;
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
    public void addEnchant(ItemStack itemStack, int i) {

    }

    @Override
    public void removeEnchant(ItemStack itemStack) {

    }

    @Override
    public boolean canEnchant(ItemStack itemStack) {
        return false;
    }

    private Vampirism(Builder builder) {
        super(builder);
    }
}
