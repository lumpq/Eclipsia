package io.lumpq126.eclipsia.listeners;

import io.lumpq126.eclipsia.utilities.Mm;
import io.lumpq126.eclipsia.utilities.manager.PlayerInfoManager;
import io.lumpq126.eclipsia.ui.gui.MainGUI;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import java.util.Map;

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
            if (event.getSlot() == 9) {
                MainGUI.openStatGUI(player);
            }
        }
        else if (title.equals("main-stat")) {
            event.setCancelled(true);

            if (event.getSlot() == 0) {
                MainGUI.openHomeGUI(player);
                return;
            }

            Map<Integer, String> slotStatMap = Map.of(
                    11, "str",
                    12, "con",
                    13, "agi",
                    20, "dex",
                    21, "wis",
                    22, "int"
            );

            if (!slotStatMap.containsKey(event.getSlot())) return;

            String stat = slotStatMap.get(event.getSlot());
            handleStatPointAllocation(player, event, stat);
        }
    }

    private void handleStatPointAllocation(Player player, InventoryClickEvent event, String stat) {
        int availablePoints = PlayerInfoManager.getStatPoint(player);
        if (availablePoints < 1) {
            player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 1.0f, 1.5f);
            player.sendMessage(Mm.mm("<red>분배가능한 능력치가 부족합니다!"));
            return;
        }

        int pointsToUse;

        if (event.getClick() == ClickType.LEFT) {
            pointsToUse = 1;
        } else if (event.getClick() == ClickType.RIGHT) {
            pointsToUse = availablePoints;
        } else {
            return;
        }

        PlayerInfoManager.addStat(player, stat, pointsToUse);
        PlayerInfoManager.addStatPoint(player, -pointsToUse);

        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 2.0f);
        player.sendMessage(Mm.mm("<green>능력치가 " + pointsToUse + "만큼 상승했습니다!"));
        event.getInventory().setItem(event.getSlot(), MainGUI.statItem(player, stat));
    }
}
