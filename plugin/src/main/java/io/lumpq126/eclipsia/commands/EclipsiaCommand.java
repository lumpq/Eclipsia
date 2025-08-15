package io.lumpq126.eclipsia.commands;

import io.lumpq126.eclipsia.EclipsiaPlugin;
import io.lumpq126.eclipsia.entities.EclipsiaEntity;
import io.lumpq126.eclipsia.items.FishItems;
import io.lumpq126.eclipsia.stats.Stat;
import io.lumpq126.eclipsia.utilities.storage.ClassStorage;
import io.lumpq126.eclipsia.utilities.storage.FishCatalogStorage;
import io.lumpq126.eclipsia.utilities.storage.MonthStorage;
import io.lumpq126.eclipsia.utilities.storage.PlayerPageStorage;
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
import java.util.stream.Collectors;

/**
 * 메인 명령어 처리 클래스입니다. (/ec)
 * <p>
 * 지원 서브커맨드:
 * <ul>
 *     <li>fish:     물고기 아이템 지급</li>
 *     <li>month:    월 조회/설정/리셋</li>
 *     <li>level:    레벨 조회/설정/추가/리셋</li>
 *     <li>exp:      경험치 조회/설정/추가/리셋</li>
 *     <li>stat:     스탯 조회/설정/추가/리셋, 스탯포인트 추가</li>
 *     <li>class:    직업 조회/설정/전직 가능/전직 단계/숙련도 조회·설정</li>
 *     <li>sia:      SIA 조회/설정/추가/차감</li>
 *     <li>reload:   설정 리로드</li>
 * </ul>
 *
 * 최적화/수정 사항:
 * <ol>
 *     <li>각 타겟 플레이어별로 EclipsiaEntity 생성하도록 수정 (기존 sender 기준 생성 버그 수정)</li>
 *     <li>stat 유효성 검증을 {@link Stat#fromName(String)} 기반으로 개선</li>
 *     <li>탭 컴플리터 필터 기준을 마지막 인자로 변경하여 자동완성 오동작 해결</li>
 *     <li>중복 로직 유틸화(파싱/메시지/타겟 해석 등)</li>
 *     <li>예외/경계값 체크 강화 및 사용자 메시지 명확화</li>
 * </ol>
 */
public class EclipsiaCommand implements CommandExecutor, TabCompleter {

    // =========================
    // 공통 유틸
    // =========================

    /**
     * 명령어 실행자에게 색깔이 적용된 메시지를 전송합니다.
     *
     * @param sender  명령어 실행자
     * @param message 메시지
     * @param color   색상
     */
    private void sendMessage(CommandSender sender, String message, NamedTextColor color) {
        sender.sendMessage(Component.text(message).color(color));
    }

    /**
     * 숫자 문자열을 정수로 파싱합니다.
     *
     * @param s          입력 문자열
     * @param onErrorMsg 실패 시 사용자에게 보낼 메시지 (null 허용)
     * @param sender     메시지 전송 대상
     * @return 파싱된 정수, 실패 시 null
     */
    private Integer parseIntOrNull(String s, String onErrorMsg, CommandSender sender) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            if (onErrorMsg != null) sendMessage(sender, onErrorMsg, NamedTextColor.RED);
            return null;
        }
    }

    /**
     * 숫자 문자열을 실수로 파싱합니다.
     *
     * @param s          입력 문자열
     * @param onErrorMsg 실패 시 사용자에게 보낼 메시지 (null 허용)
     * @param sender     메시지 전송 대상
     * @return 파싱된 실수, 실패 시 null
     */
    private Double parseDoubleOrNull(String s, String onErrorMsg, CommandSender sender) {
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            if (onErrorMsg != null) sendMessage(sender, onErrorMsg, NamedTextColor.RED);
            return null;
        }
    }

    /**
     * 명령어의 대상이 될 플레이어 목록을 해석합니다. (플레이어명 혹은 선택자)
     *
     * @param sender    명령어 실행자
     * @param targetArg 플레이어명 또는 선택자(@a, @p 등)
     * @return 대상 플레이어 목록 (없으면 빈 리스트)
     */
    private List<Player> resolveTargets(CommandSender sender, String targetArg) {
        List<Player> players = new ArrayList<>();
        try {
            List<Entity> entities = Bukkit.selectEntities(sender, targetArg);
            for (Entity entity : entities) {
                if (entity instanceof Player p) players.add(p);
            }
        } catch (IllegalArgumentException e) {
            // 선택자가 아니면 정확한 닉네임으로 시도
            Player player = Bukkit.getPlayerExact(targetArg);
            if (player != null) players.add(player);
        }
        return players;
    }

    /**
     * 플레이어/선택자 자동완성용 후보를 반환합니다.
     *
     * @param prefix 현재 입력 중인 접두어
     * @return 제안 목록
     */
    private List<String> getPlayersAndSelectors(String prefix) {
        List<String> list = new ArrayList<>(List.of("@a", "@p", "@r", "@s", "@e"));
        String lower = prefix == null ? "" : prefix.toLowerCase(Locale.ROOT);
        for (Player player : Bukkit.getOnlinePlayers()) {
            String name = player.getName();
            if (lower.isEmpty() || name.toLowerCase(Locale.ROOT).startsWith(lower)) {
                list.add(name);
            }
        }
        return list;
    }

    // =========================
    // onCommand
    // =========================

    /**
     * /ec 명령어의 엔트리 포인트입니다.
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender,
                             @NotNull Command command,
                             @NotNull String label,
                             @NotNull String @NotNull [] args) {
        if (args.length == 0) {
            sendMessage(sender, "사용법: /ec <fish|month|level|exp|stat|class|sia|reload> ...", NamedTextColor.RED);
            return false;
        }

        String type = args[0].toLowerCase(Locale.ROOT);

        switch (type) {
            case "fish" -> handleFish(sender, args);
            case "month" -> handleMonth(sender, args);
            case "level" -> handleLevel(sender, args);
            case "exp" -> handleExp(sender, args);
            case "stat" -> handleStat(sender, args);
            case "sia" -> handleSia(sender, args);
            case "class" -> handleClass(sender, args);
            case "reload" -> reload(sender);
            default -> sendMessage(sender, "알 수 없는 명령어입니다.", NamedTextColor.RED);
        }

        return true;
    }

    // =========================
    // class
    // =========================

    /**
     * /ec class 하위 명령어 처리
     * <pre>
     * /ec class get <player|@selector>
     * /ec class set <player|@selector> <class> [stage]
     * /ec class canAdvance <player|@selector> <class>
     * /ec class stage <player|@selector> <class>
     * /ec class proficiency <player|@selector> [value]
     * /ec class reset <player|@selector>
     * /ec class addProficiency <player|@selector> <amount>
     * /ec class removeProficiency <player|@selector> <amount>
     * /ec class stageSet <player|@selector> <stage>
     * /ec class advance <player|@selector>
     * </pre>
     */
    private void handleClass(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sendMessage(sender, "/ec class <get|set|canAdvance|stage|proficiency|reset|addProficiency|removeProficiency|stageSet|advance> <player|@selector> [args...]", NamedTextColor.RED);
            return;
        }

        String sub = args[1].toLowerCase(Locale.ROOT);
        List<Player> targets = resolveTargets(sender, args[2]);
        if (targets.isEmpty()) {
            sendMessage(sender, "대상 플레이어를 찾을 수 없습니다.", NamedTextColor.RED);
            return;
        }

        for (Player target : targets) {
            EclipsiaEntity eEntity = new EclipsiaEntity(target);
            ClassStorage.ClassInfo classInfo = eEntity.getClassInfo();
            io.lumpq126.eclipsia.classes.Class currentClass = classInfo.clazz();
            int currentStage = classInfo.stage();

            switch (sub) {
                case "get" -> sendMessage(sender,
                        target.getName() + "의 직업: " +
                                currentClass.name() + " (" + currentClass.getKoreaName() + "), 전직 단계: " + currentStage +
                                ", 숙련도: " + eEntity.getProfessionProficiency(),
                        NamedTextColor.YELLOW);

                case "set" -> {
                    if (args.length < 4) {
                        sendMessage(sender, "변경할 직업을 입력하세요.", NamedTextColor.RED);
                        return;
                    }
                    io.lumpq126.eclipsia.classes.Class newClass =
                            io.lumpq126.eclipsia.classes.Class.fromNameOrDefault(args[3].toUpperCase());
                    int stage = 0;
                    if (args.length >= 5) {
                        Integer parsed = parseIntOrNull(args[4], "전직 단계는 숫자여야 합니다.", sender);
                        if (parsed == null) return;
                        stage = parsed;
                    }
                    eEntity.setClass(newClass, stage);
                    sendMessage(sender,
                            target.getName() + "의 직업이 " +
                                    newClass.name() + " (" + newClass.getKoreaName() + "), 전직 단계: " + stage + " 으로 변경되었습니다.",
                            NamedTextColor.GREEN);
                }

                case "canadvance" -> {
                    if (args.length < 4) {
                        sendMessage(sender, "확인할 직업을 입력하세요.", NamedTextColor.RED);
                        return;
                    }
                    io.lumpq126.eclipsia.classes.Class targetClass =
                            io.lumpq126.eclipsia.classes.Class.fromName(args[3]);
                    if (targetClass == null) {
                        sendMessage(sender, "존재하지 않는 직업입니다: " + args[3], NamedTextColor.RED);
                        return;
                    }
                    boolean canAdvance =
                            io.lumpq126.eclipsia.classes.Class.canAdvanceTo(currentClass, targetClass);
                    sendMessage(sender,
                            currentClass.name() + " (" + currentClass.getKoreaName() + ") → " +
                                    targetClass.name() + " (" + targetClass.getKoreaName() + ") 전직 " + (canAdvance ? "가능" : "불가"),
                            canAdvance ? NamedTextColor.GREEN : NamedTextColor.RED);
                }

                case "stage" -> {
                    if (args.length < 4) {
                        sendMessage(sender, "조회할 직업을 입력하세요.", NamedTextColor.RED);
                        return;
                    }
                    io.lumpq126.eclipsia.classes.Class targetClass =
                            io.lumpq126.eclipsia.classes.Class.fromName(args[3]);
                    if (targetClass == null) {
                        sendMessage(sender, "존재하지 않는 직업입니다: " + args[3], NamedTextColor.RED);
                        return;
                    }
                    int stage = io.lumpq126.eclipsia.classes.Class.getAdvancementStage(currentClass, targetClass);
                    sendMessage(sender,
                            currentClass.name() + " (" + currentClass.getKoreaName() + ") → " +
                                    targetClass.name() + " (" + targetClass.getKoreaName() + ") 전직 단계: " + stage,
                            NamedTextColor.YELLOW);
                }

                case "proficiency" -> {
                    if (args.length < 4) {
                        sendMessage(sender,
                                target.getName() + " 숙련도: " + eEntity.getProfessionProficiency(),
                                NamedTextColor.YELLOW);
                        return;
                    }
                    Integer value = parseIntOrNull(args[3], "숙련도는 숫자여야 합니다.", sender);
                    if (value == null) return;
                    value = Math.max(0, Math.min(100, value));
                    eEntity.setProfessionProficiency(value);
                    sendMessage(sender, target.getName() + "의 숙련도가 " + value + "으로 설정되었습니다.",
                            NamedTextColor.GREEN);
                }

                case "reset" -> {
                    eEntity.setClass(io.lumpq126.eclipsia.classes.Class.NOVICE, 0);
                    eEntity.setProfessionProficiency(0);
                    sendMessage(sender, target.getName() + "의 직업과 숙련도가 초기화되었습니다.", NamedTextColor.GREEN);
                }

                case "addproficiency" -> {
                    if (args.length < 4) {
                        sendMessage(sender, "추가할 숙련도 값을 입력하세요.", NamedTextColor.RED);
                        return;
                    }
                    Integer amount = parseIntOrNull(args[3], "숙련도는 숫자여야 합니다.", sender);
                    if (amount == null) return;
                    int newValue = Math.max(0, Math.min(100, eEntity.getProfessionProficiency() + amount));
                    eEntity.setProfessionProficiency(newValue);
                    sendMessage(sender, target.getName() + "의 숙련도가 " + amount + "만큼 증가했습니다. 현재: " + newValue,
                            NamedTextColor.GREEN);
                }

                case "removeproficiency" -> {
                    if (args.length < 4) {
                        sendMessage(sender, "감소할 숙련도 값을 입력하세요.", NamedTextColor.RED);
                        return;
                    }
                    Integer amount = parseIntOrNull(args[3], "숙련도는 숫자여야 합니다.", sender);
                    if (amount == null) return;
                    int newValue = Math.max(0, Math.min(100, eEntity.getProfessionProficiency() - amount));
                    eEntity.setProfessionProficiency(newValue);
                    sendMessage(sender, target.getName() + "의 숙련도가 " + amount + "만큼 감소했습니다. 현재: " + newValue,
                            NamedTextColor.GREEN);
                }

                case "stageset" -> {
                    if (args.length < 4) {
                        sendMessage(sender, "설정할 전직 단계를 입력하세요.", NamedTextColor.RED);
                        return;
                    }
                    Integer stageValue = parseIntOrNull(args[3], "전직 단계는 숫자여야 합니다.", sender);
                    if (stageValue == null) return;
                    eEntity.setClass(currentClass, stageValue);
                    sendMessage(sender, target.getName() + "의 전직 단계가 " + stageValue + "으로 설정되었습니다.",
                            NamedTextColor.GREEN);
                }

                case "advance" -> {
                    io.lumpq126.eclipsia.classes.Class next = currentClass.getNextClass();
                    if (next == null) {
                        sendMessage(sender, "더 이상 전직할 수 없습니다.", NamedTextColor.RED);
                        return;
                    }
                    if (!io.lumpq126.eclipsia.classes.Class.canAdvanceTo(currentClass, next)) {
                        sendMessage(sender, "조건을 만족하지 않아 전직할 수 없습니다.", NamedTextColor.RED);
                        return;
                    }
                    eEntity.setClass(next, 0);
                    sendMessage(sender, target.getName() + "이(가) " + next.name() + " (" + next.getKoreaName() + ") 으로 전직했습니다.",
                            NamedTextColor.GREEN);
                }

                default -> sendMessage(sender, "알 수 없는 하위 명령어입니다. 사용: get, set, canAdvance, stage, proficiency, reset, addProficiency, removeProficiency, stageSet, advance",
                        NamedTextColor.RED);
            }
        }
    }

    // =========================
    // sia
    // =========================

    /**
     * /ec sia 하위 명령어 처리
     * <pre>
     * /ec sia get <player|@selector>
     * /ec sia set <player|@selector> <amount>
     * /ec sia add <player|@selector> <amount>
     * /ec sia remove <player|@selector> <amount>
     * </pre>
     */
    private void handleSia(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sendMessage(sender, "/ec sia <get|set|add|remove> <player|@selector> [amount]", NamedTextColor.RED);
            return;
        }

        String sub = args[1].toLowerCase(Locale.ROOT);
        List<Player> targets = resolveTargets(sender, args[2]);
        if (targets.isEmpty()) {
            sendMessage(sender, "대상 플레이어를 찾을 수 없습니다.", NamedTextColor.RED);
            return;
        }

        Integer amount = null;
        // set, add, remove 명령어일 때만 amount 필요
        if (List.of("set", "add", "remove").contains(sub)) {
            if (args.length < 4) {
                sendMessage(sender, "amount 값을 입력하세요.", NamedTextColor.RED);
                return;
            }
            amount = parseIntOrNull(args[3], "SIA 값은 숫자여야 합니다.", sender);
            if (amount == null) return; // 잘못된 숫자 입력 시 중단
        }

        for (Player target : targets) {
            EclipsiaEntity eEntity = new EclipsiaEntity(target);

            switch (sub) {
                case "get" -> sendMessage(sender, target.getName() + "의 SIA: " + eEntity.getSia(), NamedTextColor.YELLOW);
                case "set" -> {
                    if (amount == null) {
                        sendMessage(sender, "amount 값을 입력하세요.", NamedTextColor.RED);
                        return;
                    }
                    eEntity.setSia(amount);
                    sendMessage(sender, target.getName() + "의 SIA가 " + amount + "으로 설정되었습니다.", NamedTextColor.GREEN);
                }
                case "add" -> {
                    if (amount == null) {
                        sendMessage(sender, "amount 값을 입력하세요.", NamedTextColor.RED);
                        return;
                    }
                    eEntity.addSia(amount);
                    sendMessage(sender, target.getName() + "의 SIA가 " + amount + "만큼 증가되었습니다.", NamedTextColor.GREEN);
                }
                case "remove" -> {
                    if (amount == null) {
                        sendMessage(sender, "amount 값을 입력하세요.", NamedTextColor.RED);
                        return;
                    }
                    eEntity.removeSia(amount);
                    sendMessage(sender, target.getName() + "의 SIA가 " + amount + "만큼 감소되었습니다.", NamedTextColor.GREEN);
                }
                default -> sendMessage(sender, "잘못된 하위 명령어입니다. 사용: get, set, add, remove", NamedTextColor.RED);
            }
        }
    }

    // =========================
    // fish
    // =========================

    /**
     * /ec fish give 하위 명령어 처리
     * <pre>
     * /ec fish give <player|@selector> <id> [length] [weight] [count]
     * </pre>
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
        Double length = null, weight = null;
        int count = 1;

        if (args.length > 4) {
            length = parseDoubleOrNull(args[4], "길이는 숫자여야 합니다.", sender);
            if (length == null) return;
        }
        if (args.length > 5) {
            weight = parseDoubleOrNull(args[5], "무게는 숫자여야 합니다.", sender);
            if (weight == null) return;
        }
        if (args.length > 6) {
            Integer parsed = parseIntOrNull(args[6], "개수는 숫자여야 합니다.", sender);
            if (parsed == null) return;
            count = parsed;
        }

        // config fallback 처리
        ConfigurationSection section = EclipsiaPlugin.getFishConfig().getConfigurationSection(id);
        if ((length == null || weight == null) && section == null) {
            sendMessage(sender, "해당 ID의 물고기를 찾을 수 없습니다: " + id, NamedTextColor.RED);
            return;
        }
        double finalLength = length != null ? length : section.getDouble("max-length", 1.0);
        double finalWeight = weight != null ? weight : section.getDouble("max-weight", 1.0);

        for (Player target : targets) {
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

    // =========================
    // month
    // =========================

    /**
     * /ec month 하위 명령어 처리
     * <pre>
     * /ec month
     * /ec month set <1-12>
     * /ec month reset
     * </pre>
     */
    private void handleMonth(CommandSender sender, String[] args) {
        if (args.length == 1) {
            int month = MonthStorage.getCurrentMonth();
            sendMessage(sender, "현재 월: " + month + "월", NamedTextColor.YELLOW);
            return;
        }

        if (args.length == 3 && args[1].equalsIgnoreCase("set")) {
            Integer month = parseIntOrNull(args[2], "월은 숫자여야 합니다.", sender);
            if (month == null) return;
            if (month < 1 || month > 12) {
                sendMessage(sender, "월은 1~12 사이여야 합니다.", NamedTextColor.RED);
                return;
            }
            MonthStorage.setMonth(month);
            sendMessage(sender, "월이 " + month + "월로 설정되었습니다.", NamedTextColor.GREEN);
            return;
        }

        if (args.length == 2 && args[1].equalsIgnoreCase("reset")) {
            MonthStorage.setMonth(1);
            sendMessage(sender, "월이 1월로 리셋되었습니다.", NamedTextColor.GREEN);
            return;
        }

        sendMessage(sender, "사용법: /ec month [set <1-12>|reset]", NamedTextColor.RED);
    }

    // =========================
    // level
    // =========================

    /**
     * /ec level 하위 명령어 처리
     * <pre>
     * /ec level get <player|@selector>
     * /ec level set <player|@selector> <value>
     * /ec level add <player|@selector> <value>
     * /ec level reset <player|@selector>
     * </pre>
     */
    private void handleLevel(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sendMessage(sender, "/ec level <get|set|add|reset> <player|@selector> [value]", NamedTextColor.RED);
            return;
        }

        String sub = args[1].toLowerCase(Locale.ROOT);
        List<Player> targets = resolveTargets(sender, args[2]);
        if (targets.isEmpty()) {
            sendMessage(sender, "대상 플레이어 없음.", NamedTextColor.RED);
            return;
        }

        Integer value = null;
        if (sub.equals("set") || sub.equals("add")) {
            if (args.length < 4) {
                sendMessage(sender, "값을 입력하세요.", NamedTextColor.RED);
                return;
            }
            value = parseIntOrNull(args[3], "숫자 형식이 잘못되었습니다.", sender);
            if (value == null) return;
        }

        for (Player target : targets) {
            EclipsiaEntity eEntity = new EclipsiaEntity(target);
            switch (sub) {
                case "get" -> sendMessage(sender, target.getName() + " 레벨: " + eEntity.getLevel(), NamedTextColor.YELLOW);
                case "reset" -> {
                    eEntity.resetLevel();
                    sendMessage(sender, target.getName() + "의 레벨이 1로 초기화됨.", NamedTextColor.GREEN);
                }
                case "set" -> {
                    eEntity.setLevel(value);
                    sendMessage(sender, target.getName() + "의 레벨이 " + value + "로 설정됨.", NamedTextColor.GREEN);
                }
                case "add" -> {
                    eEntity.addLevel(value);
                    sendMessage(sender, target.getName() + "의 레벨이 +" + value + " 적용됨.", NamedTextColor.GREEN);
                }
                default -> sendMessage(sender, "잘못된 하위 명령어입니다.", NamedTextColor.RED);
            }
        }
    }

    // =========================
    // exp
    // =========================

    /**
     * /ec exp 하위 명령어 처리
     * <pre>
     * /ec exp get <player|@selector>
     * /ec exp set <player|@selector> <value>
     * /ec exp add <player|@selector> <value>
     * /ec exp reset <player|@selector>
     * </pre>
     */
    private void handleExp(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sendMessage(sender, "/ec exp <get|set|add|reset> <player|@selector> [value]", NamedTextColor.RED);
            return;
        }

        String sub = args[1].toLowerCase(Locale.ROOT);
        List<Player> targets = resolveTargets(sender, args[2]);
        if (targets.isEmpty()) {
            sendMessage(sender, "대상 플레이어 없음.", NamedTextColor.RED);
            return;
        }

        Integer value = null;
        if (sub.equals("set") || sub.equals("add")) {
            if (args.length < 4) {
                sendMessage(sender, "값을 입력하세요.", NamedTextColor.RED);
                return;
            }
            value = parseIntOrNull(args[3], "숫자 형식이 잘못되었습니다.", sender);
            if (value == null) return;
        }

        for (Player target : targets) {
            EclipsiaEntity eEntity = new EclipsiaEntity(target);
            switch (sub) {
                case "get" -> sendMessage(sender, target.getName() + " 경험치: " + eEntity.getExp(), NamedTextColor.YELLOW);
                case "reset" -> {
                    eEntity.setExp(0);
                    sendMessage(sender, target.getName() + "의 경험치가 초기화됨.", NamedTextColor.GREEN);
                }
                case "set" -> {
                    eEntity.setExp(value);
                    sendMessage(sender, target.getName() + "의 경험치가 " + value + "로 설정됨.", NamedTextColor.GREEN);
                }
                case "add" -> {
                    eEntity.addExp(value);
                    sendMessage(sender, target.getName() + "의 경험치가 +" + value + " 적용됨.", NamedTextColor.GREEN);
                }
                default -> sendMessage(sender, "잘못된 하위 명령어입니다.", NamedTextColor.RED);
            }
        }
    }

    // =========================
    // stat
    // =========================

    /**
     * /ec stat 하위 명령어 처리
     * <pre>
     * /ec stat get <player|@selector> <statType>
     * /ec stat set <player|@selector> <statType> <value>
     * /ec stat add <player|@selector> <statType> <value>
     * /ec stat reset <player|@selector> <statType>
     *
     * // 스탯 포인트
     * /ec stat point add <player|@selector> <value>
     * </pre>
     */
    private void handleStat(CommandSender sender, String[] args) {
        if (args.length < 4) {
            sendMessage(sender, "/ec stat <get|set|add|reset> <player|@selector> <statType> [value] 또는 /ec stat point add <player|@selector> <value>", NamedTextColor.RED);
            return;
        }

        // 스탯 포인트 처리
        if (args[1].equalsIgnoreCase("point")) {
            if (args.length < 5 || !args[2].equalsIgnoreCase("add")) {
                sendMessage(sender, "/ec stat point add <player|@selector> <value>", NamedTextColor.RED);
                return;
            }

            List<Player> targets = resolveTargets(sender, args[3]);
            if (targets.isEmpty()) {
                sendMessage(sender, "대상 플레이어 없음.", NamedTextColor.RED);
                return;
            }

            Integer value = parseIntOrNull(args[4], "숫자를 입력해주세요.", sender);
            if (value == null) return;

            for (Player p : targets) {
                EclipsiaEntity e = new EclipsiaEntity(p);
                e.addStatPoints(value);
                sendMessage(sender, p.getName() + "의 스탯 포인트 +" + value + " 적용됨.", NamedTextColor.GREEN);
            }
            return;
        }

        // 일반 스탯 처리
        String sub = args[1].toLowerCase(Locale.ROOT);
        List<Player> targets = resolveTargets(sender, args[2]);
        if (targets.isEmpty()) {
            sendMessage(sender, "대상 플레이어 없음.", NamedTextColor.RED);
            return;
        }

        String rawStat = args[3];
        Stat statEnum = Stat.fromName(rawStat);
        if (statEnum == null) {
            sendMessage(sender, "알 수 없는 능력치입니다: " + rawStat, NamedTextColor.RED);
            return;
        }

        Integer value = null;
        if (sub.equals("set") || sub.equals("add")) {
            if (args.length < 5) {
                sendMessage(sender, "값을 입력하세요.", NamedTextColor.RED);
                return;
            }
            value = parseIntOrNull(args[4], "숫자 형식이 잘못되었습니다.", sender);
            if (value == null) return;
        }

        for (Player target : targets) {
            EclipsiaEntity eEntity = new EclipsiaEntity(target);
            switch (sub) {
                case "get" -> {
                    int cur = eEntity.getStat(statEnum);
                    sendMessage(sender, target.getName() + "의 " + statEnum.name() + ": " + cur, NamedTextColor.YELLOW);
                }
                case "reset" -> {
                    eEntity.setStat(statEnum, 0);
                    sendMessage(sender, target.getName() + "의 " + statEnum.name() + "이 초기화되었습니다.", NamedTextColor.GREEN);
                }
                case "set" -> {
                    eEntity.setStat(statEnum, value);
                    sendMessage(sender, target.getName() + "의 " + statEnum.name() + "가 " + value + "로 설정되었습니다.", NamedTextColor.GREEN);
                }
                case "add" -> {
                    eEntity.addStat(statEnum, value);
                    sendMessage(sender, target.getName() + "의 " + statEnum.name() + "가 +" + value + " 적용되었습니다.", NamedTextColor.GREEN);
                }
                default -> sendMessage(sender, "잘못된 하위 명령어입니다.", NamedTextColor.RED);
            }
        }
    }

    // =========================
    // reload
    // =========================

    /**
     * /ec reload 처리: 설정 리로드
     */
    private void reload(CommandSender sender) {
        FishCatalogStorage.reload();
        PlayerPageStorage.reload();
        MonthStorage.reload();
        sendMessage(sender, "설정 파일이 재로드되었습니다.", NamedTextColor.GREEN);
    }

    // =========================
    // Tab Completer
    // =========================

    /**
     * 탭 자동완성 처리.
     * <p>
     * 기존 버그 수정: 항상 args[0]으로 필터하던 문제를 마지막 인자(args[args.length-1]) 기준으로 필터하도록 변경.
     */
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender,
                                                @NotNull Command command,
                                                @NotNull String alias,
                                                @NotNull String @NotNull [] args) {
        List<String> suggestions = new ArrayList<>();
        String first = args.length > 0 ? args[0].toLowerCase(Locale.ROOT) : "";
        String lastToken = args[args.length - 1];

        if (args.length == 1) {
            suggestions.addAll(List.of("fish", "month", "level", "exp", "stat", "reload", "class", "sia"));
            return filterByPrefix(suggestions, lastToken);
        }

        if (args.length == 2) {
            switch (first) {
                case "fish" -> suggestions.add("give");
                case "month" -> suggestions.addAll(List.of("set", "reset"));
                case "level", "exp" -> suggestions.addAll(List.of("get", "set", "add", "reset"));
                case "stat" -> suggestions.addAll(List.of("get", "set", "add", "reset", "point"));
                case "class" -> suggestions.addAll(List.of(
                        "get", "set", "canAdvance", "stage", "proficiency",
                        "reset", "addProficiency", "removeProficiency", "stageSet", "advance"
                ));
                case "sia" -> suggestions.addAll(List.of("get", "set", "add", "remove"));
            }
            return filterByPrefix(suggestions, lastToken);
        }

        if (args.length == 3) {
            switch (first) {
                case "sia" -> {
                    String sub = args[1].toLowerCase(Locale.ROOT);
                    if (sub.equals("get")) {
                        suggestions.addAll(getPlayersAndSelectors(lastToken));
                    } else {
                        suggestions.add("<amount>");
                    }
                }
                case "stat" -> {
                    String sub = args[1].toLowerCase(Locale.ROOT);
                    if (sub.equals("point")) {
                        suggestions.addAll(List.of("add", "remove", "set"));
                    } else {
                        suggestions.addAll(getPlayersAndSelectors(lastToken));
                    }
                }
                case "fish", "level", "exp", "class" -> suggestions.addAll(getPlayersAndSelectors(lastToken));
            }
            return filterByPrefix(suggestions, lastToken);
        }

        if (args.length == 4) {
            switch (first) {
                case "fish" -> {
                    Set<String> keys = EclipsiaPlugin.getFishConfig() != null
                            ? EclipsiaPlugin.getFishConfig().getKeys(false)
                            : Collections.emptySet();
                    suggestions.addAll(keys);
                }
                case "stat" -> {
                    if (args[1].equalsIgnoreCase("point")) {
                        suggestions.addAll(getPlayersAndSelectors(lastToken));
                    } else {
                        suggestions.addAll(List.of("STRENGTH","CONSTITUTION","AGILITY","DEXTERITY","INTELLIGENCE","WISDOM"));
                    }
                }
                case "class" -> {
                    String sub = args[1].toLowerCase(Locale.ROOT);
                    if (sub.equals("set") || sub.equals("canadvance") || sub.equals("stage")) {
                        for (io.lumpq126.eclipsia.classes.Class c : io.lumpq126.eclipsia.classes.Class.values()) {
                            suggestions.add(c.name().toLowerCase());
                        }
                    }
                }
            }
            return filterByPrefix(suggestions, lastToken);
        }

        return filterByPrefix(suggestions, lastToken);
    }

    private List<String> filterByPrefix(Collection<String> source, String prefix) {
        String lower = prefix == null ? "" : prefix.toLowerCase(Locale.ROOT);
        return source.stream()
                .filter(s -> s.toLowerCase(Locale.ROOT).startsWith(lower))
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }
}
