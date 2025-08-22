package io.snowyblossom126.eclipsia.utilities.registry;

import io.snowyblossom126.eclipsia.EclipsiaPlugin;
import io.snowyblossom126.eclipsia.enchantments.Vampirism;
import io.snowyblossom126.enchantapi.api.EnchantAPI;

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
