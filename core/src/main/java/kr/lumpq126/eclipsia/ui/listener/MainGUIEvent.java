package kr.lumpq126.eclipsia.ui.listener;

import kr.lumpq126.eclipsia.ui.gui.MainGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class MainGUIEvent implements Listener {

    @EventHandler
    public void open(PlayerSwapHandItemsEvent event) {
        Player p = event.getPlayer();
        if (!p.isSneaking()) return;
        event.setCancelled(true);
        MainGUI.openStatGUI(p);
    }
}
