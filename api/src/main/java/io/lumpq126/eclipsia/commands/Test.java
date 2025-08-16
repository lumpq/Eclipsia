package io.lumpq126.eclipsia.commands;

import io.lumpq126.eclipsia.core.mechanics.enchants.Vampirism;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class Test implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        ItemStack i = Vampirism.get();
        if (i == null) return true;
        ((Player) sender).getInventory().addItem(i);
        return true;
    }
}
