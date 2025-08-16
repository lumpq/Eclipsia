package io.lumpq126.eclipsia.commands;

import io.lumpq126.eclipsia.core.mechanics.enchants.enchantment.Vampirism;
import io.lumpq126.enchantAPI.v1_21_R3.enchantment.manager.EnchantmentManager_v1_21_R3;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class Test implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        ItemStack i = ((Player) sender).getInventory().getItemInMainHand();
        Vampirism vampirism = new Vampirism();
        EnchantmentManager_v1_21_R3.addEnchant(i, vampirism.getKey(), 1);
        return true;
    }
}
