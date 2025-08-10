package io.lumpq126.eclipsia.listeners;

import io.lumpq126.eclipsia.nms.utilities.manager.PlayerInfoManager;
import io.lumpq126.eclipsia.ui.gui.MainGUI;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
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
        String title = PlainTextComponentSerializer.plainText().serialize(event.getView().title());
        if (title.equals("main-home")) {
            event.setCancelled(true);
            if (event.getSlot() == 0) {
                MainGUI.openHomeGUI(player);
            }
            else if (event.getSlot() == 9) {
                MainGUI.openStatGUI(player);
            }
        }
        else if (title.equals("main-stat")) {
            event.setCancelled(true);
            if (event.getSlot() == )
        }
    }
}
