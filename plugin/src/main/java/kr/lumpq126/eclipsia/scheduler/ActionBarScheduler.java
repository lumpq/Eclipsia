package kr.lumpq126.eclipsia.scheduler;

import kr.lumpq126.eclipsia.utilities.ActionBarUtility;
import kr.lumpq126.eclipsia.api.utilities.manager.PlayerInfoManager;
import org.bukkit.entity.Player;

public class ActionBarScheduler {
    public static void showLevelAndExp(Player player) {
        int level = PlayerInfoManager.getLevel(player);
        double exp = PlayerInfoManager.getExp(player);
        double nextLevelExp = PlayerInfoManager.getRequiredExp(level);

        String message = "§a[ Lv. " + level + " ] §8| §e[ EXP§f:§e " + String.format("%.1f", exp) + " §f/§e " + nextLevelExp + " ]";
        ActionBarUtility.sendActionBar(player, message);
    }
}
