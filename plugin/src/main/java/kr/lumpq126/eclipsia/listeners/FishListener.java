package kr.lumpq126.eclipsia.listeners;

import kr.lumpq126.eclipsia.EclipsiaPlugin;
import kr.lumpq126.eclipsia.fish.ui.FishUI;
import kr.lumpq126.eclipsia.fish.items.FishItems;
import kr.lumpq126.eclipsia.api.utilities.manager.MonthManager;
import kr.lumpq126.eclipsia.api.utilities.manager.PlayerPageManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FishListener implements Listener {

    @EventHandler
    public void onBookClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player p)) return;

        String title = PlainTextComponentSerializer.plainText().serialize(e.getView().title());
        if (!title.equals("§f\uEBBB篇")) return;

        e.setCancelled(true);
        ItemStack item = e.getCurrentItem();
        if (item == null || !item.hasItemMeta()) return;

        int slot = e.getSlot();
        int currentPage = PlayerPageManager.getBookPage(p.getUniqueId());

        if (item.getType() == Material.PAPER) {
            ItemMeta meta = item.getItemMeta();
            if (meta == null || !meta.hasDisplayName()) return;

            Component display = meta.displayName();
            if (display == null) return;

            String name = (display instanceof TextComponent tc) ? tc.content() : PlainTextComponentSerializer.plainText().serialize(display);

            if (slot == 46 && name.contains("이전")) {
                FishUI.openBook(p, currentPage - 1);
                PlayerPageManager.setBookPage(p.getUniqueId(), currentPage - 1);
                PlayerPageManager.save();
                p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
            } else if (slot == 52 && name.contains("다음")) {
                FishUI.openBook(p, currentPage + 1);
                PlayerPageManager.setBookPage(p.getUniqueId(), currentPage + 1);
                PlayerPageManager.save();
                p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
            }
        }

        else if (item.getType() == Material.COD) {
            ItemMeta meta = item.getItemMeta();
            if (meta == null || !meta.hasLore()) return;

            List<Component> lore = meta.lore();
            if (lore == null || lore.isEmpty()) return;

            Component last = lore.getLast();
            String id = PlainTextComponentSerializer.plainText().serialize(last).trim();
            if (id.isEmpty()) return;

            FileConfiguration fishConfig = EclipsiaPlugin.getFishConfig();
            ConfigurationSection section = fishConfig.getConfigurationSection(id);
            if (section == null) return;

            double maxLength = section.getDouble("max-length", 1000);
            double maxWeight = section.getDouble("max-weight", 1000);

            ItemStack fish = FishItems.fish(p, id, maxLength, maxWeight);
            if (fish != null) {
                p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 2.0f);
                if (e.getClick().isLeftClick()) {
                    p.getInventory().addItem(fish);
                } else if (e.getClick().isRightClick()) {
                    for (int i = 0; i < 64; i++) {
                        p.getInventory().addItem(fish);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onCatalogClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player p)) return;

        String title = PlainTextComponentSerializer.plainText().serialize(e.getView().title());
        if (!title.equals("§f\uEBBB緊")) return;

        e.setCancelled(true);
        ItemStack item = e.getCurrentItem();
        if (item == null || !item.hasItemMeta()) return;

        int slot = e.getSlot();
        int currentPage = PlayerPageManager.getBookPage(p.getUniqueId());

        if (item.getType() == Material.PAPER) {
            ItemMeta meta = item.getItemMeta();
            if (meta == null || !meta.hasDisplayName()) return;

            Component display = meta.displayName();
            if (display == null) return;

            String name = (display instanceof TextComponent tc) ? tc.content() : PlainTextComponentSerializer.plainText().serialize(display);
            if (slot == 46 && name.contains("이전")) {
                FishUI.openCatalog(p, currentPage - 1);
                PlayerPageManager.setBookPage(p.getUniqueId(), currentPage - 1);
                PlayerPageManager.save();
                p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
            } else if (slot == 52 && name.contains("다음")) {
                FishUI.openCatalog(p, currentPage + 1);
                PlayerPageManager.setBookPage(p.getUniqueId(), currentPage + 1);
                PlayerPageManager.save();
                p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
            }
        }
    }

    @EventHandler
    public void onBookClose(InventoryCloseEvent e) {
        if (!(e.getPlayer() instanceof Player player)) return;
        if (!e.getView().title().equals(Component.text("§f\uEBBB篇"))) return;
        PlayerPageManager.setBookPage(player.getUniqueId(), 0);
    }

    @EventHandler
    public void onCatalogClose(InventoryCloseEvent e) {
        if (!(e.getPlayer() instanceof Player player)) return;
        if (!e.getView().title().equals(Component.text("§f\uEBBB緊"))) return;
        PlayerPageManager.setBookPage(player.getUniqueId(), 0);
    }

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        if (event.getState() != PlayerFishEvent.State.CAUGHT_FISH) return;
        if (!(event.getCaught() instanceof Item item)) return;

        Player player = event.getPlayer();
        FileConfiguration fishConfig = EclipsiaPlugin.getFishConfig();
        int currentMonth = MonthManager.getCurrentMonth();

        List<String> candidates = getAvailableFishIds(fishConfig, currentMonth);
        if (candidates.isEmpty()) return;

        String selectedId = candidates.get(new Random().nextInt(candidates.size()));
        ConfigurationSection selected = fishConfig.getConfigurationSection(selectedId);
        if (selected == null) return;

        double minLength = selected.getDouble("min-length", 0);
        double maxLength = selected.getDouble("max-length", 100);
        double maxWeight = selected.getDouble("max-weight", 100);

        double length = getLengthByCondition(selected.getConfigurationSection("capture-condition"), currentMonth, minLength, maxLength);

        double lengthRatio = (maxLength > 0) ? (length / maxLength) : 0;
        double rawWeight = maxWeight * lengthRatio;
        double weight = getRandomDouble(rawWeight * 0.85, rawWeight * 1.15);

        ItemStack fishItem = FishItems.fish(player, selectedId, length, weight);
        if (fishItem == null) return;

        item.setItemStack(fishItem);
    }

    private List<String> getAvailableFishIds(FileConfiguration config, int currentMonth) {
        List<String> result = new ArrayList<>();

        for (String id : config.getKeys(false)) {
            ConfigurationSection section = config.getConfigurationSection(id);
            if (section == null || !section.getBoolean("enable")) continue;

            ConfigurationSection condition = section.getConfigurationSection("capture-condition");
            if (condition == null) continue;

            List<Integer> canCapture = condition.getIntegerList("can-capture-month");
            boolean monthOk = canCapture.isEmpty() ? condition.getBoolean("always-can-capture") : canCapture.contains(currentMonth);
            if (!monthOk) continue;

            result.add(id);
        }

        return result;
    }

    private double getLengthByCondition(ConfigurationSection condition, int month, double min, double max) {
        if (condition == null) return getRandomDouble(min, max);

        ConfigurationSection large = condition.getConfigurationSection("large-object-capture");
        if (isEnabledForMonth(large, month)) {
            double minLarge = large.getDouble("min-length", min);
            return getRandomDouble(minLarge, max);
        }

        ConfigurationSection small = condition.getConfigurationSection("small-object-capture");
        if (isEnabledForMonth(small, month)) {
            double maxSmall = small.getDouble("max-length", max);
            return getRandomDouble(min, maxSmall);
        }

        return getRandomDouble(min, max);
    }

    private boolean isEnabledForMonth(ConfigurationSection section, int month) {
        return section != null && section.getBoolean("enable") && section.getIntegerList("month").contains(month);
    }

    private double getRandomDouble(double min, double max) {
        return min + (max - min) * new Random().nextDouble();
    }
}
