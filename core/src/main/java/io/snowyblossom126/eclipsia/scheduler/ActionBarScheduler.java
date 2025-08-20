package io.snowyblossom126.eclipsia.scheduler;

import io.snowyblossom126.eclipsia.EclipsiaPlugin;
import io.snowyblossom126.eclipsia.core.mechanics.entities.EclipsiaEntity;
import io.snowyblossom126.eclipsia.utilities.ActionBarUtility;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ActionBarScheduler {

    public static void showLevelAndExp(Player player) {
        EclipsiaEntity eEntity = new EclipsiaEntity(player);
        int level = eEntity.getLevel();
        double exp = eEntity.getExp();
        double nextLevelExp = eEntity.getRequiredExp(level);
        int sia = eEntity.getSia();

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
