package io.lumpq126.eclipsia.scheduler;

import io.lumpq126.eclipsia.utilities.ActionBarUtility;
import io.lumpq126.eclipsia.api.utilities.manager.PlayerInfoManager;
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
}
