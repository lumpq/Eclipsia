package io.lumpq126.eclipsia.scheduler;

import io.lumpq126.eclipsia.EclipsiaPlugin;
import io.lumpq126.eclipsia.entities.EclipsiaEntity;
import io.lumpq126.eclipsia.stats.Stat;
import io.lumpq126.eclipsia.utilities.storage.PlayerInfoStorage;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;

public class AttributeScheduler {

    public static void setAttribute(Player player) {
        AttributeInstance attackAttr = player.getAttribute(Attribute.ATTACK_DAMAGE);
        AttributeInstance healthAttr = player.getAttribute(Attribute.MAX_HEALTH);
        AttributeInstance speedAttr = player.getAttribute(Attribute.MOVEMENT_SPEED);
        AttributeInstance attackSpeedAttr = player.getAttribute(Attribute.ATTACK_SPEED);
        EclipsiaEntity eEntity = new EclipsiaEntity(player);
        if (attackAttr != null) {
            double str = eEntity.getStat(Stat.fromName("STRENGTH"));
            attackAttr.setBaseValue(str + 1);
        }
        if (healthAttr != null) {
            double con = eEntity.getStat(Stat.fromName("CONSTITUTION"));
            healthAttr.setBaseValue(con + 100);
        }
        if (speedAttr != null) {
            double agi = eEntity.getStat(Stat.fromName("agi"));
            double moveSpeed = Math.min(agi, 2000) * 0.2;
            speedAttr.setBaseValue(0.1 + moveSpeed * 0.01 * 0.1);
        }
        if (attackSpeedAttr != null) {
            double agi = eEntity.getStat(Stat.fromName("agi"));
            if (agi >= 2000) {
                double atkSpeed = (agi - 1999) * 0.04;
                attackSpeedAttr.setBaseValue(4 + 4 * atkSpeed * 0.01);
            }
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
