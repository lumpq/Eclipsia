package kr.lumpq126.eclipsia.commands;

import kr.lumpq126.eclipsia.EclipsiaPlugin;
import kr.lumpq126.eclipsia.fish.items.FishItems;
import kr.lumpq126.eclipsia.fish.ui.FishUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class FishCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player p) || !p.isOp()) return false;

        if (args.length == 5 && args[0].equalsIgnoreCase("get")) {
            try {
                String id = args[1];
                double len = Double.parseDouble(args[2]);
                double wei = Double.parseDouble(args[3]);
                int count = Integer.parseInt(args[4]);

                ItemStack fish = FishItems.fish(p, id, len, wei);
                if (fish == null) {
                    p.sendMessage("§c해당 ID의 물고기를 찾을 수 없습니다.");
                    return true;
                }

                for (int i = 0; i < count; i++) {
                    p.getInventory().addItem(fish);
                }

                p.sendMessage("§a" + id + " " + count + "개를 지급했습니다!");
            } catch (NumberFormatException e) {
                p.sendMessage("§c숫자 형식이 잘못되었습니다.");
            }
        } else if (args.length == 2 && args[0].equalsIgnoreCase("get")) {
            try {
                String id = args[1];
                ConfigurationSection fishSection = EclipsiaPlugin.getFishConfig().getConfigurationSection(id);
                if (fishSection != null) {
                    double len = fishSection.getDouble("max-length", -1);
                    double wei = fishSection.getDouble("max-weight", -1);
                    ItemStack fish = FishItems.fish(p, id, len, wei);
                    if (fish == null) {
                        p.sendMessage("§c해당 ID의 물고기를 찾을 수 없습니다.");
                        return true;
                    }

                    p.getInventory().addItem(fish);
                    p.sendMessage("§a" + id + " 1개를 지급했습니다!");
                }
            } catch (NumberFormatException e) {
                p.sendMessage("§c숫자 형식이 잘못되었습니다.");
            }
        } else if (args.length == 1 && (args[0].equalsIgnoreCase("도감") || args[0].equalsIgnoreCase("catalog"))) {
            FishUI.openCatalog(p, 0);
            return true;
        } else if (args.length == 0) {
            FishUI.openBook(p, 0);
            return true;
        }

        return true;
    }
}
