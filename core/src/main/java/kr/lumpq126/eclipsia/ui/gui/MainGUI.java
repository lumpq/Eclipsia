package kr.lumpq126.eclipsia.ui.gui;

import kr.lumpq126.eclipsia.utilities.InventoryUtility;
import kr.lumpq126.eclipsia.utilities.manager.PlayerInfoManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
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
        Inventory inventory = InventoryUtility.inventory(player, 36, "main");
        player.openInventory(inventory);
    }

    public static void openStatGUI(Player player) {
        Inventory inventory = InventoryUtility.inventory(player, 36, "stat");

        // 여기에 6개의 스탯 아이템을 순서대로 배치
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

        // 철검과 네더라이트 갑옷일 경우 능력치 숨기기
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        String gradient = type.color.replace("<gradient:", "").replace(">", "");
        Component displayName = deserializeMM(
                String.format("%s:white><bold>%s</bold><gray> : </gray><yellow><bold>[ %d ]</bold></yellow>",
                        gradient, type.display, statValue));
        meta.displayName(displayName);

        List<Component> lore = new ArrayList<>();
        for (Component line : type.loreFunction.apply(statValue, statPoint)) {
            lore.add(removeItalic(line));
        }

        if (statValue >= 9999) {
            addEmptyLine(lore);
            lore.add(deserializeMM("<gradient:#e5ccff:#7f00ff><bold>최대 레벨</bold></gradient>"));
        }

        addEmptyLine(lore);
        lore.add(deserializeMM("<white>분배 가능한 능력치</white><gray> : </gray><gold><bold>" + statPoint + "</bold></gold>"));

        addEmptyLine(lore);
        lore.add(deserializeMM("<gray>----------------------------------------</gray>"));
        addEmptyLine(lore);
        lore.add(deserializeMM("<gray>쉬프트 + 우클릭으로 눌러 분배가능한 능력치 10개를 사용합니다."));

        meta.lore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private static Component deserializeMM(String miniMessage) {
        return MiniMessage.miniMessage().deserialize(miniMessage).decoration(TextDecoration.ITALIC, false);
    }

    private static void addEmptyLine(List<Component> lore) {
        lore.add(Component.empty());
    }

    private static Component removeItalic(Component component) {
        return component.decoration(TextDecoration.ITALIC, false);
    }
}
