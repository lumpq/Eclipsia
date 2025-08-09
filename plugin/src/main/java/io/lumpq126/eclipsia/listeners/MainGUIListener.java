package io.lumpq126.eclipsia.listeners;

import io.lumpq126.eclipsia.ui.gui.MainGUI;
import io.lumpq126.eclipsia.api.utilities.manager.PlayerInfoManager;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class MainGUIListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        PlayerInfoManager.playerInitialSetting(p);
    }

    @EventHandler
    public void openHome(PlayerSwapHandItemsEvent event) {
        Player p = event.getPlayer();
        if (!p.isSneaking()) return;
        event.setCancelled(true);
        MainGUI.openHomeGUI(p);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (event.getView().title().contains(Component.text("main-"))) {
            if (event.getSlot() == 0) {
                MainGUI.openHomeGUI(player);
            }
            else if (event.getSlot() == 9) {
                MainGUI.openStatGUI(player);
            }
            else if (event.getSlot() == 18) {
                //TODO
            }
        }
    }
}
