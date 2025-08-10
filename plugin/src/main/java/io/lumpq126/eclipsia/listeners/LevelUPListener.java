package io.lumpq126.eclipsia.listeners;


import io.lumpq126.eclipsia.nms.events.PlayerLevelUpEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class LevelUPListener implements Listener {

    @EventHandler
    public void onLevelUP(PlayerLevelUpEvent event) {
        event.getPlayer().sendMessage(Component.text("§l§a레벨 상승! " + event.getOldLevel() + " -> " + event.getNewLevel()));
    }
}
