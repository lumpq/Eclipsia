package io.lumpq126.eclipsia.core.mechanics.enchants.enchantment;

import io.lumpq126.enchantAPI.v1_21_R3.enchantment.CustomEnchantment_v1_21_R3;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class Vampirism extends CustomEnchantment_v1_21_R3 {


    public Vampirism() {
        super(
                "vampirism",                      // id
                "Vampirism",                      // 표시 이름
                3,                                // maxLevel
                15,                               // anvilCost
                5,                                // weight (COMMON 정도)
                EnchantmentTarget.WEAPON,         // 인챈트 대상
                new EquipmentSlot[]{EquipmentSlot.HAND}, // 적용 슬롯
                false,                            // isTreasure (X, 일반적으로 나옴)
                false,                            // isCursed (X)
                true,                             // canTrade (주민 거래 가능)
                true,                             // isDiscoverable (인챈트 테이블 등장 가능)
                1,                                // minCostBase
                11,                               // minCostPerLevel
                20,                               // maxCostBase
                11                                // maxCostPerLevel
        );
    }

    @Override
    public void addEnchant(ItemStack item, int level) {
        if (item == null || !item.hasItemMeta()) return;

        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(getKey(), PersistentDataType.INTEGER, level);
        item.setItemMeta(meta);
    }

    @Override
    public void removeEnchant(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return;

        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().remove(getKey());
        item.setItemMeta(meta);
    }

    @Override
    public boolean canEnchant(ItemStack item) {
        // 검과 도끼에만 인챈트를 부여하도록 조건을 수정합니다.
        return item != null && (item.getType().toString().endsWith("_SWORD") || item.getType().toString().endsWith("_AXE"));
    }
}
