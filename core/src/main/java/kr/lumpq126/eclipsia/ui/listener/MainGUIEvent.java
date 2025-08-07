package kr.lumpq126.eclipsia.ui.listener;

import kr.lumpq126.eclipsia.ui.gui.MainGUI;
import kr.lumpq126.eclipsia.utilities.manager.PlayerInfoManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class MainGUIEvent implements Listener {

    @EventHandler
    public void open(PlayerSwapHandItemsEvent event) {
        Player p = event.getPlayer();
        if (!p.isSneaking()) return;
        event.setCancelled(true);
        MainGUI.openStatGUI(p);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        PlayerInfoManager.playerInitialSetting(p);
    }
}
