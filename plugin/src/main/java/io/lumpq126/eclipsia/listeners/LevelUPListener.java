package io.lumpq126.eclipsia.listeners;


import io.lumpq126.eclipsia.events.PlayerLevelUpEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class LevelUPListener implements Listener {

    @EventHandler
    public void onLevelUP(PlayerLevelUpEvent event) {
        event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 2.0f);
        event.getPlayer().sendMessage(Component.text("§l§a레벨 상승! " + event.getOldLevel() + " -> " + event.getNewLevel()));
    }
}
