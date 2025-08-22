package io.snowyblossom126.eclipsia.nms.v1_20_R4.utilities;

import io.snowyblossom126.eclipsia.nms.NMSBridger;
import io.snowyblossom126.eclipsia.utilities.Mm;
import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftInventoryCustom;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class InventoryHandler implements NMSBridger.NMSInventoryHandler {

    @Override
    public Inventory createInventory(Player owner, int size, String title) {
        // PaperDevBundle 기반 CraftInventoryCustom 사용
        return new CraftInventoryCustom(owner, size, Mm.mm(title));
    }
}