package io.lumpq126.eclipsia.scheduler;

import io.lumpq126.eclipsia.EclipsiaPlugin;
import io.lumpq126.eclipsia.nms.utilities.manager.PlayerInfoManager;
import io.lumpq126.eclipsia.utilities.ActionBarUtility;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ActionBarScheduler {

    public static void showLevelAndExp(Player player) {
        int level = PlayerInfoManager.getLevel(player);
        double exp = PlayerInfoManager.getExp(player);
        double nextLevelExp = PlayerInfoManager.getRequiredExp(level);
        int sia = PlayerInfoManager.getSia(player);

        String message =
                "§a[ Lv§f. §a" + level + " ] §8| " +
                "§e[ Exp§f:§e " + String.format("%.1f", exp) +
                " §f/§e " + nextLevelExp + " ] §8| §2[ Cash§f: §2" + sia + " SIA §2]";
        ActionBarUtility.sendActionBar(player, message);
    }

    public static void start() {
        Bukkit.getScheduler().runTaskTimer(EclipsiaPlugin.getInstance(), () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                ActionBarScheduler.showLevelAndExp(player);
            }
        }, 0L, 1L);
    }
}
