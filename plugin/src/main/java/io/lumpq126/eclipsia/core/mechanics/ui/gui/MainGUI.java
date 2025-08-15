package io.lumpq126.eclipsia.core.mechanics.ui.gui;

import io.lumpq126.eclipsia.core.mechanics.entities.EclipsiaEntity;
import io.lumpq126.eclipsia.core.mechanics.stats.Stat;
import io.lumpq126.eclipsia.utilities.InventoryUtility;
import io.lumpq126.eclipsia.utilities.Mm;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
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
        Inventory inventory = InventoryUtility.inventory(player, 36, "<italic:false>main-home");
        ItemStack homePaper = ItemStack.of(Material.PAPER);
        ItemStack statPaper = ItemStack.of(Material.PAPER);
        ItemMeta homeMeta = homePaper.getItemMeta();
        ItemMeta statMeta = statPaper.getItemMeta();
        homeMeta.displayName(Mm.mm("<italic:false>main-home"));
        homePaper.setItemMeta(homeMeta);
        statMeta.displayName(Mm.mm("<italic:false>main-stat"));
        statPaper.setItemMeta(homeMeta);
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
        homeMeta.displayName(Mm.mm("<italic:false>main-home"));
        homePaper.setItemMeta(homeMeta);
        statMeta.displayName(Mm.mm("<italic:false>main-stat"));
        statPaper.setItemMeta(homeMeta);
        inventory.setItem(0, homePaper);
        inventory.setItem(9, statPaper);

        // 여기에 6개의 스탯 아이템을 순서대로 배치
        inventory.setItem(11, statItem(player, "STRENGTH")); // 근력
        inventory.setItem(12, statItem(player, "CONSTITUTION")); // 건강
        inventory.setItem(13, statItem(player, "AGILITY")); // 민첩
        inventory.setItem(20, statItem(player, "DEXTERITY")); // 재주
        inventory.setItem(21, statItem(player, "WISDOM")); // 지혜
        inventory.setItem(22, statItem(player, "INTELLIGENCE")); // 지능

        player.openInventory(inventory);
    }

    public static ItemStack statItem(Player player, String statName) {
        StatType type = StatType.fromName(statName);
        if (type == null) return ItemStack.of(Material.BARRIER); // fallback

        EclipsiaEntity eEntity = new EclipsiaEntity(player);

        int statValue = eEntity.getStat(Stat.fromName(statName));
        int statPoint = eEntity.getStatPoints();

        ItemStack item = new ItemStack(type.material);
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return item;

        // 철검과 네더라이트 갑옷일 경우 능력치 숨기기
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        String gradient = type.color.replace("<gradient:", "").replace(">", "");
        Component displayName = Mm.mm(
                String.format("<italic:false>%s:white><bold>%s</bold><gray> : </gray><yellow><bold>[ %d ]</bold></yellow>",
                        gradient, type.display, statValue));
        meta.displayName(displayName);

        List<Component> lore = new ArrayList<>();
        for (Component line : type.loreFunction.apply(statValue, statPoint)) {
            lore.add(removeItalic(line));
        }

        if (statValue >= 9999) {
            addEmptyLine(lore);
            lore.add(Mm.mm("<italic:false><gradient:#e5ccff:#7f00ff><bold>최대 레벨</bold></gradient>"));
        }

        addEmptyLine(lore);
        lore.add(Mm.mm("<italic:false><white>분배 가능한 능력치</white><gray> : </gray><gold><bold>" + statPoint + "</bold></gold>"));

        addEmptyLine(lore);
        lore.add(Mm.mm("<italic:false><gray>----------------------------------------</gray>"));
        addEmptyLine(lore);
        lore.add(Mm.mm("<italic:false><gray>쉬프트 + 우클릭으로 눌러 분배가능한 능력치 10개를 사용합니다."));

        meta.lore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private static void addEmptyLine(List<Component> lore) {
        lore.add(Component.empty());
    }

    private static Component removeItalic(Component component) {
        return component.decoration(TextDecoration.ITALIC, false);
    }
}
