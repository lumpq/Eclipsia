package io.lumpq126.eclipsia.scheduler;

import io.lumpq126.eclipsia.EclipsiaPlugin;
import io.lumpq126.eclipsia.utilities.manager.PlayerInfoManager;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;

public class AttributeScheduler {

    public static void setAttribute(Player player) {
        AttributeInstance attackAttr = player.getAttribute(Attribute.ATTACK_DAMAGE);
        if (attackAttr != null) {
            double str = PlayerInfoManager.getStat(player, "str");
            attackAttr.setBaseValue(str + 1);
        }
    }


    public static void start() {
        Bukkit.getScheduler().runTaskTimer(EclipsiaPlugin.getInstance(), () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                AttributeScheduler.setAttribute(player);
            }
        }, 0L, 1L);
    }
}
