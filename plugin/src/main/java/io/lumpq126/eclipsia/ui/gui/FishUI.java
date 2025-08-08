package io.lumpq126.eclipsia.ui.gui;

import io.lumpq126.eclipsia.EclipsiaPlugin;
import io.lumpq126.eclipsia.items.FishItems;
import io.lumpq126.eclipsia.api.utilities.manager.FishCatalogManager;
import io.lumpq126.eclipsia.api.utilities.manager.PlayerPageManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class FishUI {
    private static final int[] FISH_SLOTS = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34};

    public static void openBook(Player p, int page) {
        openFishInventory(p, page, false);
    }

    public static void openCatalog(Player p, int page) {
        openFishInventory(p, page, true);
    }

    private static void openFishInventory(Player p, int page, boolean isCatalog) {
        FileConfiguration config;
        if (isCatalog) {
            config = FishCatalogManager.getConfig();
        } else {
            config = EclipsiaPlugin.getFishConfig();
        }

        List<String> fishIds;
        if (isCatalog) {
            fishIds = new ArrayList<>(config.getStringList(p.getUniqueId() + ".unlocked"));
        } else {
            fishIds = new ArrayList<>(config.getKeys(false));
        }

        int totalPages = (int) Math.ceil(fishIds.size() / 21.0);
        page = Math.max(0, Math.min(page, totalPages - 1));

        PlayerPageManager.setBookPage(p.getUniqueId(), page);

        String title = isCatalog ? "§f\uEBBB緊" : "§f\uEBBB篇";
        Inventory inv = Bukkit.createInventory(p, 54, Component.text(title));

        int start = page * 21;
        int end = Math.min(start + 21, fishIds.size());

        for (int i = start; i < end; i++) {
            String id = fishIds.get(i);
            ItemStack item = id == null ? null : FishItems.fishBook(id);
            if (item != null) {
                inv.setItem(FISH_SLOTS[i - start], item);
            }
        }

        if (page > 0) {
            ItemStack prev = new ItemStack(Material.PAPER);
            ItemMeta meta = prev.getItemMeta();
            if (meta != null) {
                meta.setCustomModelData(2);
                meta.displayName(Component.text("§e◀ 이전 페이지"));
                prev.setItemMeta(meta);
                inv.setItem(46, prev);
            }
        }

        if (page < totalPages - 1) {
            ItemStack next = new ItemStack(Material.PAPER);
            ItemMeta meta = next.getItemMeta();
            if (meta != null) {
                meta.setCustomModelData(1);
                meta.displayName(Component.text("§e▶ 다음 페이지"));
                next.setItemMeta(meta);
                inv.setItem(52, next);
            }
        }

        p.openInventory(inv);
    }
}
