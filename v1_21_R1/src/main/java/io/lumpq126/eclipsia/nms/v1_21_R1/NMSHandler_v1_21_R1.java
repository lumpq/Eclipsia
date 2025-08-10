package io.lumpq126.eclipsia.nms.v1_21_R1;

import io.lumpq126.eclipsia.nms.NMSHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class NMSHandler_v1_21_R1 implements NMSHandler {
    @Override
    public Inventory createInventory(Player owner, int size, String title) {
        return Bukkit.createInventory(owner, size, MiniMessage.miniMessage().deserialize(title));
    }

    @Override
    public void sendActionBar(Player player, String message) {
        player.sendActionBar(MiniMessage.miniMessage().deserialize(message));
    }
}
