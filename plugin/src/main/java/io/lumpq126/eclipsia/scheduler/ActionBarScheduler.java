package io.lumpq126.eclipsia.scheduler;

import io.lumpq126.eclipsia.EclipsiaPlugin;
import io.lumpq126.eclipsia.utilities.storage.PlayerInfoStorage;
import io.lumpq126.eclipsia.utilities.ActionBarUtility;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ActionBarScheduler {

    public static void showLevelAndExp(Player player) {
        int level = PlayerInfoStorage.getLevel(player);
        double exp = PlayerInfoStorage.getExp(player);
        double nextLevelExp = PlayerInfoStorage.getRequiredExp(level);
        int sia = PlayerInfoStorage.getSia(player);

        String message = "<green>[ Lv<white>. <green>" + level + " ] <dark_gray>| " +
                "<yellow>[ Exp<white>:<yellow> " + String.format("%.1f", exp) +
                " <white>/<yellow> " + nextLevelExp + " ] <dark_gray>| " +
                "<dark_green>[ Cash<white>: <dark_green>" + sia + " SIA <dark_green>]";
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
