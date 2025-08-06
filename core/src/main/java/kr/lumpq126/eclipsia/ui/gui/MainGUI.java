package kr.lumpq126.eclipsia.ui.gui;

import kr.lumpq126.eclipsia.utilities.InventoryUtility;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class MainGUI {

    public static void openHomeGUI(Player p) {
        Inventory inv = InventoryUtility.inventory(p, 54, "main");
        inv.setItem(0, );
        p.openInventory(inv);
    }

    public static void openStatGUI(Player p) {

    }
}
