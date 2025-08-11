package io.lumpq126.eclipsia.ui.gui;

import io.lumpq126.eclipsia.utilities.Mm;
import io.lumpq126.eclipsia.utilities.manager.PlayerInfoManager;
import io.lumpq126.eclipsia.utilities.InventoryUtility;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class MainGUI {

    public static void openHomeGUI(Player player) {
        Inventory inventory = InventoryUtility.inventory(player, 36, "main-home");
        ItemStack homePaper = ItemStack.of(Material.PAPER);
        ItemStack statPaper = ItemStack.of(Material.PAPER);
        ItemMeta homeMeta = homePaper.getItemMeta();
        ItemMeta statMeta = statPaper.getItemMeta();

        // bold, italic 모두 false
        homeMeta.displayName(Mm.mm("main-home", false, false));
        homePaper.setItemMeta(homeMeta);

        statMeta.displayName(Mm.mm("main-stat", false, false));
        statPaper.setItemMeta(statMeta);

        inventory.setItem(0, homePaper);
        inventory.setItem(9, statPaper);
        player.openInventory(inventory);
    }

    public static void openStatGUI(Player player) {
        Inventory inventory = InventoryUtility.inventory(player, 36, "main-stat");

        ItemStack homePaper = ItemStack.of(Material.PAPER);
        ItemStack statPaper = ItemStack.of(Material.PAPER);
        ItemMeta homeMeta = homePaper.getItemMeta();
        ItemMeta statMeta = statPaper.getItemMeta();

        homeMeta.displayName(Mm.mm("main-home", false, false));
        homePaper.setItemMeta(homeMeta);

        statMeta.displayName(Mm.mm("main-stat", false, false));
        statPaper.setItemMeta(statMeta);

        inventory.setItem(0, homePaper);
        inventory.setItem(9, statPaper);

        inventory.setItem(11, statItem(player, "str")); // 근력
        inventory.setItem(12, statItem(player, "con")); // 건강
        inventory.setItem(13, statItem(player, "agi")); // 민첩
        inventory.setItem(20, statItem(player, "dex")); // 재주
        inventory.setItem(21, statItem(player, "wis")); // 지혜
        inventory.setItem(22, statItem(player, "int")); // 지능

        player.openInventory(inventory);
    }

    public static ItemStack statItem(Player player, String statName) {
        StatType type = StatType.fromName(statName);
        if (type == null) return new ItemStack(Material.BARRIER); // fallback

        int statValue = PlayerInfoManager.getStat(player, statName);
        int statPoint = PlayerInfoManager.getStatPoint(player);

        ItemStack item = new ItemStack(type.material);
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return item;

        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        String gradient = type.color.replace("<gradient:", "").replace(">", "");
        Component displayName = Mm.mm(
                String.format("%s:white%s<gray> : </gray><yellow>[ %d ]</yellow>",
                        gradient, type.display, statValue), false, false);
        meta.displayName(displayName);

        // 그대로 사용, italic 제거 안함 (style 없으니 걱정없음)
        List<Component> lore = new ArrayList<>(type.loreFunction.apply(statValue, statPoint));

        if (statValue >= 9999) {
            addEmptyLine(lore);
            lore.add(Mm.mm("<gradient:#e5ccff:#7f00ff>최대 레벨</gradient>", false, false));
        }

        addEmptyLine(lore);
        lore.add(Mm.mm("<white>분배 가능한 능력치</white><gray> : </gray><gold>" + statPoint + "</gold>", false, false));

        addEmptyLine(lore);
        lore.add(Mm.mm("<gray>----------------------------------------</gray>", false, false));
        addEmptyLine(lore);
        lore.add(Mm.mm("<gray>쉬프트 + 우클릭으로 눌러 분배가능한 능력치 10개를 사용합니다.", false, false));

        meta.lore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private static void addEmptyLine(List<Component> lore) {
        lore.add(Component.empty());
    }
}
