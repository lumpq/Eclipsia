package io.lumpq126.eclipsia.core.mechanics.enchants.listener;

import io.lumpq126.eclipsia.core.mechanics.enchants.enchantment.Vampirism;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public record VampirismListener(Vampirism vampirismEnchantment) implements Listener {

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        // 공격자가 플레이어가 아니라면 즉시 종료
        if (!(event.getDamager() instanceof Player player)) {
            return;
        }

        // 손에 들고 있는 아이템이 유효한지 확인
        ItemStack mainHandItem = player.getInventory().getItemInMainHand();
        ItemMeta meta = mainHandItem.getItemMeta();
        if (meta == null) {
            return;
        }

        // PersistentDataContainer에 뱀피리즘 인챈트가 있는지 확인
        Integer enchantLevel = meta.getPersistentDataContainer().get(vampirismEnchantment.getKey(), PersistentDataType.INTEGER);
        if (enchantLevel == null) {
            return;
        }

        // 인챈트 효과 적용
        double healthToHeal = event.getDamage() * (0.1 * enchantLevel);

        // 최대 체력 속성을 안전하게 가져와서 사용
        AttributeInstance maxHealthAttribute = player.getAttribute(Attribute.MAX_HEALTH);
        double maxHealth = (maxHealthAttribute != null) ? maxHealthAttribute.getValue() : 20.0; // 기본 체력 20.0

        // 플레이어의 체력 회복, 최대 체력을 넘지 않도록
        player.setHealth(Math.min(player.getHealth() + healthToHeal, maxHealth));
    }
}
