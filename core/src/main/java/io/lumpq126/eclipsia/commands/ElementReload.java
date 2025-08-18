package io.lumpq126.eclipsia.commands;

import io.lumpq126.eclipsia.core.mechanics.elements.ElementStorage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class ElementReload implements CommandExecutor {
    private final JavaPlugin plugin;

    public ElementReload(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull [] args) {
        plugin.reloadConfig();
        ElementStorage.init(plugin);
        sender.sendMessage("§aElement config reloaded!");
        return true;
    }
}
