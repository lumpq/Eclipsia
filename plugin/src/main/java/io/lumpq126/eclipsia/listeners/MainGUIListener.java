package io.lumpq126.eclipsia.listeners;

import io.lumpq126.eclipsia.utilities.Mm;
import io.lumpq126.eclipsia.utilities.manager.PlayerInfoManager;
import io.lumpq126.eclipsia.ui.gui.MainGUI;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Sound;
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
            if (event.getSlot() == 11) {
                if (PlayerInfoManager.getStatPoint(player) < 1) {
                    player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 1.0f, 1.5f);
                    player.sendMessage(Mm.mm("<red>분배가능한 능력치가 부족합니다!"));
                    return;
                }
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 2.0f);
                player.sendMessage(Mm.mm("<green>능력치가 상승했습니다!"));
                PlayerInfoManager.addStat(player, "str", 1);
                PlayerInfoManager.addStatPoint(player, -1);
                event.getInventory().setItem(11, MainGUI.statItem(player, "str"));
            }
            else if (event.getSlot() == 12) {
                if (PlayerInfoManager.getStatPoint(player) < 1) {
                    player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 1.0f, 1.5f);
                    player.sendMessage(Mm.mm("<red>분배가능한 능력치가 부족합니다!"));
                    return;
                }
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 2.0f);
                player.sendMessage(Mm.mm("<green>능력치가 상승했습니다!"));
                PlayerInfoManager.addStat(player, "con", 1);
                PlayerInfoManager.addStatPoint(player, -1);
                event.getInventory().setItem(12, MainGUI.statItem(player, "con"));
            }
            else if (event.getSlot() == 13) {
                if (PlayerInfoManager.getStatPoint(player) < 1) {
                    player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 1.0f, 1.5f);
                    player.sendMessage(Mm.mm("<red>분배가능한 능력치가 부족합니다!"));
                    return;
                }
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 2.0f);
                player.sendMessage(Mm.mm("<green>능력치가 상승했습니다!"));
                PlayerInfoManager.addStat(player, "agi", 1);
                PlayerInfoManager.addStatPoint(player, -1);
                event.getInventory().setItem(13, MainGUI.statItem(player, "agi"));
            }
            else if (event.getSlot() == 20) {
                if (PlayerInfoManager.getStatPoint(player) < 1) {
                    player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 1.0f, 1.5f);
                    player.sendMessage(Mm.mm("<red>분배가능한 능력치가 부족합니다!"));
                    return;
                }
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 2.0f);
                player.sendMessage(Mm.mm("<green>능력치가 상승했습니다!"));
                PlayerInfoManager.addStat(player, "dex", 1);
                PlayerInfoManager.addStatPoint(player, -1);
                event.getInventory().setItem(20, MainGUI.statItem(player, "dex"));
            }
            else if (event.getSlot() == 21) {
                if (PlayerInfoManager.getStatPoint(player) < 1) {
                    player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 1.0f, 1.5f);
                    player.sendMessage(Mm.mm("<red>분배가능한 능력치가 부족합니다!"));
                    return;
                }
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 2.0f);
                player.sendMessage(Mm.mm("<green>능력치가 상승했습니다!"));
                PlayerInfoManager.addStat(player, "wis", 1);
                PlayerInfoManager.addStatPoint(player, -1);
                event.getInventory().setItem(21, MainGUI.statItem(player, "wis"));
            }
            else if (event.getSlot() == 22) {
                if (PlayerInfoManager.getStatPoint(player) < 1) {
                    player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 1.0f, 1.5f);
                    player.sendMessage(Mm.mm("<red>분배가능한 능력치가 부족합니다!"));
                    return;
                }
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 2.0f);
                player.sendMessage(Mm.mm("<green>능력치가 상승했습니다!"));
                PlayerInfoManager.addStat(player, "int", 1);
                PlayerInfoManager.addStatPoint(player, -1);
                event.getInventory().setItem(22, MainGUI.statItem(player, "int"));
            }
        }
    }
}
