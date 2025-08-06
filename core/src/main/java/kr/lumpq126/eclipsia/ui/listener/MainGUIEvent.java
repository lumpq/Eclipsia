package kr.lumpq126.eclipsia.ui.listener;

import kr.lumpq126.eclipsia.ui.gui.MainGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class MainGUIEvent implements Listener {

    @EventHandler
    public void open(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        if (event.getAction() != Action.RIGHT_CLICK_AIR) return;
        if (!p.isSneaking()) return;
        MainGUI.openHomeGUI(p);
    }
}
