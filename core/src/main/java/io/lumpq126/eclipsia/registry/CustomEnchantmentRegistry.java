package io.lumpq126.eclipsia.registry;

import io.lumpq126.eclipsia.EclipsiaPlugin;
import io.lumpq126.eclipsia.core.mechanics.enchants.enchantment.Vampirism;
import io.lumpq126.enchantAPI.api.EnchantAPI;

public class CustomEnchantmentRegistry {

    public static void registry(EclipsiaPlugin plugin) {
        EnchantAPI api = EnchantAPI.getInstance();

        if (api == null) {
            plugin.getLogger().severe("EnchantAPI 플러그인을 찾을 수 없습니다.");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
            return;
        }

        api.registerEnchantment(new Vampirism());
    }
}
