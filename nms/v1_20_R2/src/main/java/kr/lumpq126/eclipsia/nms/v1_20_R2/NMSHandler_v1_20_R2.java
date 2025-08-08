package kr.lumpq126.eclipsia.nms.v1_20_R2;

import kr.lumpq126.eclipsia.nms.NMSHandler;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class NMSHandler_v1_20_R2 implements NMSHandler {
    @Override
    public Inventory createInventory(Player owner, int size, String title) {
        return Bukkit.createInventory(owner, size, Component.text(title));
    }

    @Override
    public void sendActionBar(Player player, String message) {
        player.sendActionBar(Component.text(message));
    }
}