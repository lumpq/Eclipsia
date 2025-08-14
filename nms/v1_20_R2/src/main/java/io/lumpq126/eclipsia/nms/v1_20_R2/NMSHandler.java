package io.lumpq126.eclipsia.nms.v1_20_R2;

import io.lumpq126.eclipsia.utilities.Mm;
import org.bukkit.craftbukkit.v1_20_R2.inventory.CraftInventoryCustom;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class NMSHandler implements io.lumpq126.eclipsia.nms.NMSHandler {

    @Override
    public Inventory createInventory(Player owner, int size, String title) {
        // PaperDevBundle 기반 CraftInventoryCustom 사용
        return new CraftInventoryCustom(owner, size, Mm.mm(title));
    }
}