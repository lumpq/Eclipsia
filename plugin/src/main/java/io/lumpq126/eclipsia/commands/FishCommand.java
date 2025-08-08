package io.lumpq126.eclipsia.commands;

import io.lumpq126.eclipsia.ui.gui.FishUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class FishCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player p) || !p.isOp()) return false;
        if (args.length == 1 && (args[0].equalsIgnoreCase("도감") || args[0].equalsIgnoreCase("catalog"))) {
            FishUI.openCatalog(p, 0);
            return true;
        } else if (args.length == 0) {
            FishUI.openBook(p, 0);
            return true;
        }

        return true;
    }
}
