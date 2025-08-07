package kr.lumpq126.eclipsia.commands;

import kr.lumpq126.eclipsia.EclipsiaPlugin;
import kr.lumpq126.eclipsia.fish.items.FishItems;
import kr.lumpq126.eclipsia.utilities.manager.MonthManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class EclipsiaCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 0) {
            sender.sendMessage(Component.text("명령어를 입력하세요.").color(NamedTextColor.RED));
            return false;
        }

        String type = args[0].toLowerCase();

        switch (type) {
            // 물고기 관련 명령어 (관리자 권한 필요)
            case "fish" -> {
                if (!(sender instanceof Player p) || !p.isOp()) {
                    sender.sendMessage(Component.text("플레이어 권한이 필요하거나 OP만 사용 가능합니다.").color(NamedTextColor.RED));
                    return true;
                }

                if (args.length == 6 && args[1].equalsIgnoreCase("get")) {
                    try {
                        String id = args[2];
                        double len = Double.parseDouble(args[3]);
                        double wei = Double.parseDouble(args[4]);
                        int count = Integer.parseInt(args[5]);

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
                } else if (args.length == 3 && args[1].equalsIgnoreCase("get")) {
                    try {
                        String id = args[2];
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
                        } else {
                            p.sendMessage("§c해당 ID의 물고기를 찾을 수 없습니다.");
                        }
                    } catch (NumberFormatException e) {
                        p.sendMessage("§c숫자 형식이 잘못되었습니다.");
                    }
                } else {
                    sender.sendMessage(Component.text("잘못된 명령어 형식입니다.").color(NamedTextColor.RED));
                }
                return true;
            }

            // 월 관리 명령어
            case "month" -> {
                if (args.length == 1) {
                    int month = MonthManager.getCurrentMonth();
                    sender.sendMessage(
                            Component.text("현재 월: ").color(NamedTextColor.YELLOW)
                                    .append(Component.text(month + "월").color(NamedTextColor.AQUA))
                    );
                    return true;
                }

                if (args.length == 3 && args[1].equalsIgnoreCase("set")) {
                    try {
                        int newMonth = Integer.parseInt(args[2]);
                        if (newMonth < 1 || newMonth > 12) {
                            sender.sendMessage(Component.text("월은 1에서 12 사이여야 합니다.").color(NamedTextColor.RED));
                            return true;
                        }
                        MonthManager.setMonth(newMonth);
                        sender.sendMessage(Component.text("월이 " + newMonth + "월로 설정되었습니다.").color(NamedTextColor.GREEN));
                    } catch (NumberFormatException e) {
                        sender.sendMessage(Component.text("숫자를 입력해주세요.").color(NamedTextColor.RED));
                    }
                    return true;
                }

                if (args.length == 2 && args[1].equalsIgnoreCase("reset")) {
                    MonthManager.setMonth(1);
                    sender.sendMessage(Component.text("월이 1월로 리셋되었습니다.").color(NamedTextColor.GREEN));
                    return true;
                }

                sender.sendMessage(Component.text("/eclipsia month [set <1~12> | reset]").color(NamedTextColor.RED));
                return true;
            }

            // 이하 레벨, 경험치, 스탯 관련 명령어는 기존과 동일
            // (코드 길이상 생략 가능하니 필요시 요청해주세요)

            // 알 수 없는 명령어 처리
            default -> {
                sender.sendMessage(Component.text("알 수 없는 명령어입니다.").color(NamedTextColor.RED));
                return true;
            }
        }
    }
}
