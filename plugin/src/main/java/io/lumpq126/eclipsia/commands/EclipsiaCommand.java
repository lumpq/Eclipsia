package io.lumpq126.eclipsia.commands;

import io.lumpq126.eclipsia.EclipsiaPlugin;
import io.lumpq126.eclipsia.items.FishItems;
import io.lumpq126.eclipsia.utilities.storage.FishCatalogStorage;
import io.lumpq126.eclipsia.utilities.storage.MonthStorage;
import io.lumpq126.eclipsia.utilities.storage.PlayerInfoStorage;
import io.lumpq126.eclipsia.utilities.storage.PlayerPageStorage;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class EclipsiaCommand implements CommandExecutor, TabCompleter {

    /**
     * 명령어의 대상이 될 플레이어들을 해석하는 메서드입니다.
     * @param sender 명령어 실행자
     * @param targetArg 대상 플레이어 이름 또는 선택자(@a, @p 등)
     * @return 대상 플레이어 목록
     */
    private List<Player> resolveTargets(CommandSender sender, String targetArg) {
        List<Player> players = new ArrayList<>();
        try {
            List<Entity> entities = Bukkit.selectEntities(sender, targetArg);
            for (Entity entity : entities) {
                if (entity instanceof Player p) players.add(p);
            }
        } catch (IllegalArgumentException e) {
            // 선택자가 아닐 경우, 정확한 플레이어 이름으로 시도
            Player player = Bukkit.getPlayerExact(targetArg);
            if (player != null) players.add(player);
        }
        return players;
    }

    /**
     * 명령어 실행자에게 색깔이 적용된 메시지를 보내는 유틸 메서드입니다.
     * @param sender 명령어 실행자
     * @param message 보낼 메시지
     * @param color 메시지 색상
     */
    private void sendMessage(CommandSender sender, String message, NamedTextColor color) {
        sender.sendMessage(Component.text(message).color(color));
    }

    /**
     * 명령어 실행 시 호출되는 메서드입니다.
     * 명령어 타입에 따라 각 하위 명령어 처리 메서드를 호출합니다.
     */
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
            case "reload" -> reload(sender);
            default -> sendMessage(sender, "알 수 없는 명령어입니다.", NamedTextColor.RED);
        }

        return true;
    }

    /**
     * /ec fish give 명령어 처리 메서드입니다.
     * 플레이어에게 지정된 ID의 물고기 아이템을 지급합니다.
     */
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

            // 길이나 무게가 음수일 경우 설정값에서 기본값 가져오기
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

    /**
     * /ec month 명령어 처리 메서드입니다.
     * 현재 월 조회, 월 설정 및 리셋 기능을 수행합니다.
     */
    private void handleMonth(CommandSender sender, String[] args) {
        if (args.length == 1) {
            int month = MonthStorage.getCurrentMonth();
            sendMessage(sender, "현재 월: " + month + "월", NamedTextColor.YELLOW);
            return;
        }

        if (args.length == 3 && args[1].equalsIgnoreCase("set")) {
            try {
                int month = Integer.parseInt(args[2]);
                if (month < 1 || month > 12) throw new NumberFormatException();
                MonthStorage.setMonth(month);
                sendMessage(sender, "월이 " + month + "월로 설정되었습니다.", NamedTextColor.GREEN);
            } catch (NumberFormatException e) {
                sendMessage(sender, "월은 1~12 사이여야 합니다.", NamedTextColor.RED);
            }
            return;
        }

        if (args.length == 2 && args[1].equalsIgnoreCase("reset")) {
            MonthStorage.setMonth(1);
            sendMessage(sender, "월이 1월로 리셋되었습니다.", NamedTextColor.GREEN);
        }
    }

    /**
     * /ec level 명령어 처리 메서드입니다.
     * 플레이어 레벨 조회, 설정, 추가, 초기화를 수행합니다.
     */
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
                case "get" -> sendMessage(sender, target.getName() + " 레벨: " + PlayerInfoStorage.getLevel(target), NamedTextColor.YELLOW);
                case "reset" -> {
                    PlayerInfoStorage.resetLevel(target);
                    sendMessage(sender, target.getName() + "의 레벨이 1로 초기화됨.", NamedTextColor.GREEN);
                }
                case "set", "add" -> {
                    if (args.length < 4) {
                        sendMessage(sender, "값을 입력하세요.", NamedTextColor.RED);
                        return;
                    }
                    try {
                        int value = Integer.parseInt(args[3]);
                        if (sub.equals("set")) PlayerInfoStorage.setLevel(target, value);
                        else PlayerInfoStorage.addLevel(target, value);
                        sendMessage(sender, target.getName() + "의 레벨이 적용됨.", NamedTextColor.GREEN);
                    } catch (NumberFormatException e) {
                        sendMessage(sender, "숫자 형식이 잘못되었습니다.", NamedTextColor.RED);
                    }
                }
                default -> sendMessage(sender, "잘못된 하위 명령어입니다.", NamedTextColor.RED);
            }
        }
    }

    /**
     * /ec exp 명령어 처리 메서드입니다.
     * 플레이어 경험치 조회, 설정, 추가, 초기화를 수행합니다.
     */
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
                case "get" -> sendMessage(sender, target.getName() + " 경험치: " + PlayerInfoStorage.getExp(target), NamedTextColor.YELLOW);
                case "reset" -> {
                    PlayerInfoStorage.setExp(target, 0);
                    sendMessage(sender, target.getName() + "의 경험치가 초기화됨.", NamedTextColor.GREEN);
                }
                case "set", "add" -> {
                    if (args.length < 4) {
                        sendMessage(sender, "값을 입력하세요.", NamedTextColor.RED);
                        return;
                    }
                    try {
                        int value = Integer.parseInt(args[3]);
                        if (sub.equals("set")) PlayerInfoStorage.setExp(target, value);
                        else PlayerInfoStorage.addExp(target, value);
                        sendMessage(sender, target.getName() + "의 경험치가 적용됨.", NamedTextColor.GREEN);
                    } catch (NumberFormatException e) {
                        sendMessage(sender, "숫자 형식이 잘못되었습니다.", NamedTextColor.RED);
                    }
                }
                default -> sendMessage(sender, "잘못된 하위 명령어입니다.", NamedTextColor.RED);
            }
        }
    }

    /**
     * /ec stat 명령어 처리 메서드입니다.
     * 플레이어 스탯 조회, 설정, 추가, 초기화 및 스탯 포인트 추가 기능을 수행합니다.
     */
    private void handleStat(CommandSender sender, String[] args) {
        if (args.length < 4) {
            sendMessage(sender, "/ec stat <get|set|add|reset> <player|@selector> <statType> [value] 또는 /ec stat statPoint add <player> <value>", NamedTextColor.RED);
            return;
        }

        // statPoint add 처리 분기
        if (args[1].equalsIgnoreCase("statpoint") && args[2].equalsIgnoreCase("add")) {
            List<Player> targets = resolveTargets(sender, args[3]);
            if (targets.isEmpty() || args.length < 5) {
                sendMessage(sender, "대상 또는 값이 누락되었습니다.", NamedTextColor.RED);
                return;
            }
            try {
                int value = Integer.parseInt(args[4]);
                for (Player p : targets) {
                    PlayerInfoStorage.addStatPoint(p, value);
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
            // 해당 플레이어가 가진 유효한 스탯 이름들 조회 (statPoint 제외)
            Set<String> validStats = PlayerInfoStorage.getStatKeysExceptPoint(target);
            if (!validStats.contains(stat)) {
                sendMessage(sender, "알 수 없는 능력치입니다: " + stat, NamedTextColor.RED);
                continue;
            }

            switch (sub) {
                case "get" -> {
                    int value = PlayerInfoStorage.getStat(target, stat);
                    sendMessage(sender, target.getName() + "의 " + stat + ": " + value, NamedTextColor.YELLOW);
                }
                case "reset" -> {
                    PlayerInfoStorage.setStat(target, stat, 0);
                    sendMessage(sender, target.getName() + "의 " + stat + "이 초기화되었습니다.", NamedTextColor.GREEN);
                }
                case "set", "add" -> {
                    if (args.length < 5) {
                        sendMessage(sender, "값을 입력하세요.", NamedTextColor.RED);
                        return;
                    }
                    try {
                        int value = Integer.parseInt(args[4]);
                        if (sub.equals("set")) PlayerInfoStorage.setStat(target, stat, value);
                        else PlayerInfoStorage.addStat(target, stat, value);
                        sendMessage(sender, target.getName() + "의 " + stat + "가 적용되었습니다.", NamedTextColor.GREEN);
                    } catch (NumberFormatException e) {
                        sendMessage(sender, "숫자 형식이 잘못되었습니다.", NamedTextColor.RED);
                    }
                }
                default -> sendMessage(sender, "잘못된 하위 명령어입니다.", NamedTextColor.RED);
            }
        }
    }

    /**
     * /ec reload 명령어 처리 메서드입니다.
     * 플러그인 설정 파일들을 다시 불러옵니다.
     */
    private void reload(CommandSender sender) {
        FishCatalogStorage.reload();
        PlayerInfoStorage.reload();
        PlayerPageStorage.reload();
        MonthStorage.reload();
        sendMessage(sender, "설정 파일이 재로드되었습니다.", NamedTextColor.GREEN);
    }

    /**
     * 명령어 탭 완성 기능을 제공합니다.
     * 각 단계별로 가능한 명령어 또는 인자를 반환합니다.
     */
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String @NotNull [] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            // 첫번째 인자 - 명령어 종류
            completions.addAll(List.of("fish", "month", "level", "exp", "stat", "reload"));
        } else if (args.length == 2) {
            String type = args[0].toLowerCase();
            switch (type) {
                case "fish" -> {
                    completions.add("give");
                }
                case "month" -> {
                    completions.addAll(List.of("set", "reset"));
                }
                case "level", "exp" -> {
                    completions.addAll(List.of("get", "set", "add", "reset"));
                }
                case "stat" -> {
                    completions.addAll(List.of("get", "set", "add", "reset", "statPoint"));
                }
            }
        } else if (args.length == 3) {
            String type = args[0].toLowerCase();
            switch (type) {
                case "fish" -> {
                    // 플레이어 또는 선택자
                    completions.addAll(getPlayersAndSelectors(args[2]));
                    // 온라인 플레이어 이름 추가
                    Bukkit.getOnlinePlayers().forEach(p -> completions.add(p.getName()));
                }
                case "month" -> {
                    // 월 숫자 탭 완성은 직접 구현하지 않음
                }
                case "level", "exp", "stat" -> {
                    // 플레이어 또는 선택자
                    Bukkit.getOnlinePlayers().forEach(p -> completions.add(p.getName()));
                    completions.addAll(getPlayersAndSelectors(args[2]));
                }
            }
        } else if (args.length == 4) {
            String type = args[0].toLowerCase();
            switch (type) {
                case "fish" -> {
                    // 물고기 ID 목록 반환
                    Set<String> fishIds = EclipsiaPlugin.getFishConfig().getKeys(false);
                    completions.addAll(fishIds);
                }
                case "stat" -> {
                    // stat 하위명령어 제외 statPoint 일 때 처리
                    if (args[1].equalsIgnoreCase("statpoint")) {
                        // 4번째 인자는 플레이어 이름
                        Bukkit.getOnlinePlayers().forEach(p -> completions.add(p.getName()));
                    } else {
                        // stat 종류 목록
                        completions.addAll(PlayerInfoStorage.getStatKeysExceptPoint(null));
                    }
                }
            }
        }
        return completions;
    }

    /**
     * 입력된 문자열(arg)을 기준으로 미리 정해진 선택자와 온라인 플레이어 이름 중
     * 해당 문자열로 시작하는 이름들을 리스트로 반환합니다.
     *
     * @param arg 플레이어 이름 또는 선택자 입력 일부
     * @return 선택자 및 필터링된 플레이어 이름 리스트
     */
    private List<String> getPlayersAndSelectors(String arg) {
        // 기본적으로 사용 가능한 선택자 목록 (@a, @p, @r, @s, @e)로 리스트 초기화
        List<String> list = new ArrayList<>(List.of("@a", "@p", "@r", "@s", "@e"));

        // 현재 접속 중인 플레이어들의 이름 중 arg로 시작하는 이름만 리스트에 추가
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getName().toLowerCase().startsWith(arg.toLowerCase())) {
                list.add(player.getName());
            }
        }

        return list;
    }
}
