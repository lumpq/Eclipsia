package kr.lumpq126.eclipsia.command;

import kr.lumpq126.eclipsia.utility.manager.MonthManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class MonthCommand implements CommandExecutor {
    private final MonthManager monthManager;

    public MonthCommand(MonthManager monthManager) {
        this.monthManager = monthManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull [] args) {
        if (!sender.hasPermission("ecc.admin")) {
            sender.sendMessage(Component.text("이 명령어를 사용할 권한이 없습니다.").color(NamedTextColor.RED));
            return true;
        }

        if (args.length == 0) {
            int month = MonthManager.getCurrentMonth();
            sender.sendMessage(
                    Component.text("현재 월: ").color(NamedTextColor.YELLOW)
                            .append(Component.text(month + "월").color(NamedTextColor.AQUA))
            );
            return true;
        }

        if (args[0].equalsIgnoreCase("set")) {
            if (args.length != 2) {
                sender.sendMessage(Component.text("/month set <1~12>").color(NamedTextColor.RED));
                return true;
            }
            try {
                int newMonth = Integer.parseInt(args[1]);
                if (newMonth < 1 || newMonth > 12) {
                    sender.sendMessage(Component.text("월은 1에서 12 사이여야 합니다.").color(NamedTextColor.RED));
                    return true;
                }
                monthManager.setMonth(newMonth);
                sender.sendMessage(Component.text("월이 " + newMonth + "월로 설정되었습니다.").color(NamedTextColor.GREEN));
            } catch (NumberFormatException e) {
                sender.sendMessage(Component.text("숫자를 입력해주세요.").color(NamedTextColor.RED));
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("reset")) {
            monthManager.setMonth(1);
            sender.sendMessage(Component.text("월이 1월로 리셋되었습니다.").color(NamedTextColor.GREEN));
            return true;
        }

        sender.sendMessage(Component.text("/month [set <1~12> | reset]").color(NamedTextColor.RED));
        return true;
    }
}
