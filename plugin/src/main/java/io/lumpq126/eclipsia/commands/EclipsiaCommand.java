package io.lumpq126.eclipsia.commands;

import io.lumpq126.eclipsia.EclipsiaPlugin;
import io.lumpq126.eclipsia.api.utilities.manager.*;
import io.lumpq126.eclipsia.items.FishItems;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class EclipsiaCommand implements CommandExecutor, TabCompleter {

    private List<Player> resolveTargets(CommandSender sender, String targetArg) {
        List<Player> players = new ArrayList<>();
        try {
            List<Entity> entities = Bukkit.selectEntities(sender, targetArg);
            for (Entity entity : entities) {
                if (entity instanceof Player p) players.add(p);
            }
        } catch (IllegalArgumentException e) {
            Player player = Bukkit.getPlayerExact(targetArg);
            if (player != null) players.add(player);
        }
        return players;
    }

    private void sendMessage(CommandSender sender, String message, NamedTextColor color) {
        sender.sendMessage(Component.text(message).color(color));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 0) {
            sendMessage(sender, "명령어를 입력하세요.", NamedTextColor.RED);
            return false;
        }

        String type = args[0].toLowerCase();

        switch (type) {
            case "fish" -> handleFish(sender, args);
            case "month" -> handleMonth(sender, args);
            case "level" -> handleLevel(sender, args);
            case "exp" -> handleExp(sender, args);
            case "stat" -> handleStat(sender, args);
            case "reload" -> reload();
            default -> sendMessage(sender, "알 수 없는 명령어입니다.", NamedTextColor.RED);
        }

        return true;
    }

    private void handleFish(CommandSender sender, String[] args) {
        if (args.length < 4 || !args[1].equalsIgnoreCase("give")) {
            sendMessage(sender, "/ec fish give <player|@selector> <id> [length] [weight] [count]", NamedTextColor.RED);
            return;
        }

        List<Player> targets = resolveTargets(sender, args[2]);
        if (targets.isEmpty()) {
            sendMessage(sender, "대상 플레이어를 찾을 수 없습니다.", NamedTextColor.RED);
            return;
        }

        String id = args[3];
        double length = -1;
        double weight = -1;
        int count = 1;

        try {
            if (args.length > 4) length = Double.parseDouble(args[4]);
            if (args.length > 5) weight = Double.parseDouble(args[5]);
            if (args.length > 6) count = Integer.parseInt(args[6]);
        } catch (NumberFormatException e) {
            sendMessage(sender, "길이, 무게, 개수는 숫자여야 합니다.", NamedTextColor.RED);
            return;
        }

        for (Player target : targets) {
            double finalLength = length;
            double finalWeight = weight;

            if (length < 0 || weight < 0) {
                ConfigurationSection section = EclipsiaPlugin.getFishConfig().getConfigurationSection(id);
                if (section != null) {
                    finalLength = section.getDouble("max-length", 1.0);
                    finalWeight = section.getDouble("max-weight", 1.0);
                } else {
                    sendMessage(sender, "해당 ID의 물고기를 찾을 수 없습니다: " + id, NamedTextColor.RED);
                    continue;
                }
            }

            ItemStack fish = FishItems.fish(target, id, finalLength, finalWeight);
            if (fish == null) {
                sendMessage(sender, "물고기 생성 실패: " + id, NamedTextColor.RED);
                continue;
            }

            for (int i = 0; i < count; i++) {
                target.getInventory().addItem(fish.clone());
            }

            sendMessage(sender, target.getName() + "에게 " + id + " 물고기 " + count + "개 지급 완료.", NamedTextColor.GREEN);
        }
    }

    private void handleMonth(CommandSender sender, String[] args) {
        if (args.length == 1) {
            int month = MonthManager.getCurrentMonth();
            sendMessage(sender, "현재 월: " + month + "월", NamedTextColor.YELLOW);
            return;
        }

        if (args.length == 3 && args[1].equalsIgnoreCase("set")) {
            try {
                int month = Integer.parseInt(args[2]);
                if (month < 1 || month > 12) throw new NumberFormatException();
                MonthManager.setMonth(month);
                sendMessage(sender, "월이 " + month + "월로 설정되었습니다.", NamedTextColor.GREEN);
            } catch (NumberFormatException e) {
                sendMessage(sender, "월은 1~12 사이여야 합니다.", NamedTextColor.RED);
            }
            return;
        }

        if (args.length == 2 && args[1].equalsIgnoreCase("reset")) {
            MonthManager.setMonth(1);
            sendMessage(sender, "월이 1월로 리셋되었습니다.", NamedTextColor.GREEN);
        }
    }

    private void handleLevel(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sendMessage(sender, "/ec level <get|set|add|reset> <player|@selector> [value]", NamedTextColor.RED);
            return;
        }

        String sub = args[1].toLowerCase();
        List<Player> targets = resolveTargets(sender, args[2]);
        if (targets.isEmpty()) {
            sendMessage(sender, "대상 플레이어 없음.", NamedTextColor.RED);
            return;
        }

        for (Player target : targets) {
            switch (sub) {
                case "get" -> sendMessage(sender, target.getName() + " 레벨: " + PlayerInfoManager.getLevel(target), NamedTextColor.YELLOW);
                case "reset" -> {
                    PlayerInfoManager.resetLevel(target);
                    sendMessage(sender, target.getName() + "의 레벨이 1로 초기화됨.", NamedTextColor.GREEN);
                }
                case "set", "add" -> {
                    if (args.length < 4) {
                        sendMessage(sender, "값을 입력하세요.", NamedTextColor.RED);
                        return;
                    }
                    try {
                        int value = Integer.parseInt(args[3]);
                        if (sub.equals("set")) PlayerInfoManager.setLevel(target, value);
                        else PlayerInfoManager.addLevel(target, value);
                        sendMessage(sender, target.getName() + "의 레벨이 적용됨.", NamedTextColor.GREEN);
                    } catch (NumberFormatException e) {
                        sendMessage(sender, "숫자 형식이 잘못되었습니다.", NamedTextColor.RED);
                    }
                }
                default -> sendMessage(sender, "잘못된 하위 명령어입니다.", NamedTextColor.RED);
            }
        }
    }

    private void handleExp(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sendMessage(sender, "/ec exp <get|set|add|reset> <player|@selector> [value]", NamedTextColor.RED);
            return;
        }

        String sub = args[1].toLowerCase();
        List<Player> targets = resolveTargets(sender, args[2]);
        if (targets.isEmpty()) {
            sendMessage(sender, "대상 플레이어 없음.", NamedTextColor.RED);
            return;
        }

        for (Player target : targets) {
            switch (sub) {
                case "get" -> sendMessage(sender, target.getName() + " 경험치: " + PlayerInfoManager.getExp(target), NamedTextColor.YELLOW);
                case "reset" -> {
                    PlayerInfoManager.setExp(target, 0);
                    sendMessage(sender, target.getName() + "의 경험치가 초기화됨.", NamedTextColor.GREEN);
                }
                case "set", "add" -> {
                    if (args.length < 4) {
                        sendMessage(sender, "값을 입력하세요.", NamedTextColor.RED);
                        return;
                    }
                    try {
                        int value = Integer.parseInt(args[3]);
                        if (sub.equals("set")) PlayerInfoManager.setExp(target, value);
                        else PlayerInfoManager.addExp(target, value);
                        sendMessage(sender, target.getName() + "의 경험치가 적용됨.", NamedTextColor.GREEN);
                    } catch (NumberFormatException e) {
                        sendMessage(sender, "숫자 형식이 잘못되었습니다.", NamedTextColor.RED);
                    }
                }
                default -> sendMessage(sender, "잘못된 하위 명령어입니다.", NamedTextColor.RED);
            }
        }
    }

    private void handleStat(CommandSender sender, String[] args) {
        if (args.length < 4) {
            sendMessage(sender, "/ec stat <get|set|add|reset> <player|@selector> <statType> [value] 또는 /ec stat statPoint add <player> <value>", NamedTextColor.RED);
            return;
        }

        if (args[1].equalsIgnoreCase("statpoint") && args[2].equalsIgnoreCase("add")) {
            List<Player> targets = resolveTargets(sender, args[3]);
            if (targets.isEmpty() || args.length < 5) {
                sendMessage(sender, "대상 또는 값이 누락되었습니다.", NamedTextColor.RED);
                return;
            }
            try {
                int value = Integer.parseInt(args[4]);
                for (Player p : targets) {
                    PlayerInfoManager.addStatPoint(p, value);
                    sendMessage(sender, p.getName() + "의 스탯 포인트 증가됨.", NamedTextColor.GREEN);
                }
            } catch (NumberFormatException e) {
                sendMessage(sender, "숫자를 입력해주세요.", NamedTextColor.RED);
            }
            return;
        }

        String sub = args[1].toLowerCase();
        List<Player> targets = resolveTargets(sender, args[2]);
        String stat = args[3].toLowerCase(); // 소문자로 비교

        if (targets.isEmpty()) {
            sendMessage(sender, "대상 플레이어 없음.", NamedTextColor.RED);
            return;
        }

        for (Player target : targets) {
            Set<String> validStats = PlayerInfoManager.getStatKeysExceptPoint(target);
            if (!validStats.contains(stat)) {
                sendMessage(sender, "알 수 없는 능력치입니다: " + stat, NamedTextColor.RED);
                continue;
            }

            switch (sub) {
                case "get" -> {
                    int statValue = PlayerInfoManager.getStat(target, stat);
                    sendMessage(sender, target.getName() + "의 " + stat + " 스탯: " + statValue, NamedTextColor.YELLOW);
                }
                case "reset" -> {
                    PlayerInfoManager.setStat(target, stat, 0);
                    sendMessage(sender, target.getName() + "의 " + stat + " 스탯이 리셋됨.", NamedTextColor.GREEN);
                }
                case "set", "add" -> {
                    if (args.length < 5) {
                        sendMessage(sender, "값을 입력하세요.", NamedTextColor.RED);
                        return;
                    }
                    try {
                        int value = Integer.parseInt(args[4]);
                        if (sub.equals("set")) PlayerInfoManager.setStat(target, stat, value);
                        else PlayerInfoManager.addStat(target, stat, value);
                        sendMessage(sender, target.getName() + "의 " + stat + " 스탯 적용됨.", NamedTextColor.GREEN);
                    } catch (NumberFormatException e) {
                        sendMessage(sender, "숫자를 입력해주세요.", NamedTextColor.RED);
                    }
                }
                default -> sendMessage(sender, "잘못된 stat 명령어입니다.", NamedTextColor.RED);
            }
        }
    }

    private void reload() {
        FishCatalogManager.reload();
        PlayerInfoManager.reload();
        PlayerPageManager.reload();
        StocksManager.reload();
        MonthManager.reload();
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String @NotNull [] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            List<String> main = List.of("fish", "month", "level", "exp", "stat", "reload");
            for (String s : main) {
                if (s.startsWith(args[0].toLowerCase())) {
                    completions.add(s);
                }
            }
        } else if (args.length == 2) {
            switch (args[0].toLowerCase()) {
                case "fish" -> completions.add("give");
                case "month" -> completions.addAll(List.of("set", "reset"));
                case "level", "exp" -> completions.addAll(List.of("get", "set", "add", "reset"));
                case "stat" -> completions.addAll(List.of("get", "set", "add", "reset", "statPoint"));
            }
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("fish") && args[1].equalsIgnoreCase("give")) {
                completions.addAll(getPlayersAndSelectors(args[2]));
            } else if (args[1].equalsIgnoreCase("statpoint")) {
                completions.add("add");
            } else {
                completions.addAll(getPlayersAndSelectors(args[2]));
            }
        } else if (args.length == 4) {
            if (args[0].equalsIgnoreCase("fish") && args[1].equalsIgnoreCase("give")) {
                ConfigurationSection section = EclipsiaPlugin.getFishConfig().getConfigurationSection("");
                if (section != null) {
                    for (String key : section.getKeys(false)) {
                        if (key.toLowerCase().startsWith(args[3].toLowerCase())) {
                            completions.add(key);
                        }
                    }
                }
            } else if (args[1].equalsIgnoreCase("statpoint")) {
                completions.addAll(getPlayersAndSelectors(args[3]));
            } else if (args[0].equalsIgnoreCase("stat") &&
                    List.of("get", "set", "add", "reset").contains(args[1].toLowerCase())) {

                List<Player> targets = resolveTargets(sender, args[2]);
                if (!targets.isEmpty()) {
                    Player target = targets.getFirst(); // 첫 번째 플레이어 기준
                    Set<String> statKeys = PlayerInfoManager.getStatKeysExceptPoint(target);
                    for (String key : statKeys) {
                        if (key.toLowerCase().startsWith(args[3].toLowerCase())) {
                            completions.add(key);
                        }
                    }
                }
            }
        }

        return completions;
    }

    private List<String> getPlayersAndSelectors(String arg) {
        List<String> list = new ArrayList<>(List.of("@a", "@p", "@r", "@s", "@e"));
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getName().toLowerCase().startsWith(arg.toLowerCase())) {
                list.add(player.getName());
            }
        }
        return list;
    }
}
