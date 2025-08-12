package io.lumpq126.eclipsia.nms.v1_21_R5;

import io.lumpq126.eclipsia.nms.NMSHandler;
import io.lumpq126.eclipsia.utilities.Mm;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class NMSHandlerImpl implements NMSHandler {
    @Override
    public Inventory createInventory(Player owner, int size, String title) {
        return Bukkit.createInventory(owner, size, Mm.mm(title));
    }

    @Override
    public void sendActionBar(Player player, String message) {
        player.sendActionBar(Mm.mm(message));
    }
}
