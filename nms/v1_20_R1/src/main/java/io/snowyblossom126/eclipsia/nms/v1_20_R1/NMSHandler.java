package io.snowyblossom126.eclipsia.nms.v1_20_R1;

import io.snowyblossom126.eclipsia.utilities.Mm;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftInventoryCustom;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class NMSHandler implements io.snowyblossom126.eclipsia.nms.NMSHandler {

    @Override
    public Inventory createInventory(Player owner, int size, String title) {
        // PaperDevBundle 기반 CraftInventoryCustom 사용
        return new CraftInventoryCustom(owner, size, Mm.mm(title));
    }
}