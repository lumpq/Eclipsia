package io.lumpq126.eclipsia.utilities.manager;

import io.lumpq126.eclipsia.events.PlayerLevelUpEvent;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;

public class PlayerInfoManager {
    private static final int MAX_LEVEL = 999;
    private static final int INITIAL_LEVEL = 1;
    private static final int INITIAL_STAT = 5;
    private static final int STAT_POINT_PER_LEVEL = 5;
    private static final double EXP_BASE = 10;
    private static final double EXP_EXPONENT = 2.25;
    private static final int INITIAL_SIA = 10000;

    private static File file;
    private static FileConfiguration config;
    private static final Object lock = new Object();

    private static JavaPlugin plugin;

    // ────────────────────── INIT / RELOAD / SAVE ──────────────────────

    public static void init(JavaPlugin instance) {
        plugin = instance;
        file = new File(plugin.getDataFolder(), "playerInfo.yml");

        if (!file.exists()) {
            File parent = file.getParentFile();
            if (parent != null && !parent.exists() && !parent.mkdirs()) {
                plugin.getLogger().warning("[Eclipsia] playerInfo.yml 상위 디렉토리 생성 실패: " + parent.getAbsolutePath());
            }

            try {
                if (!file.createNewFile()) {
                    plugin.getLogger().warning("[Eclipsia] playerInfo.yml 파일 생성 실패");
                }
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "[Eclipsia] playerInfo.yml 생성 중 오류", e);
            }
        }

        reload();
    }

    public static void reload() {
        try {
            config = YamlConfiguration.loadConfiguration(file);
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "[Eclipsia] playerInfo.yml 로딩 중 오류", e);
        }
    }

    public static void save() {
        synchronized (lock) {
            try {
                config.save(file);
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "[Eclipsia] playerInfo.yml 저장 실패", e);
            }
        }
    }


    public static Set<String> getStatKeysExceptPoint(Player player) {
        String statPath = path(player, "stat");
        if (config.getConfigurationSection(statPath) == null) return new HashSet<>();

        Set<String> keys = Objects.requireNonNull(config.getConfigurationSection(statPath)).getKeys(false);
        keys.remove("point");  // point 키 제외
        return keys;
    }

    // ────────────────────── PATH UTIL ──────────────────────

    private static String path(Player player, String... keys) {
        return player.getUniqueId() + "." + String.join(".", keys);
    }

    public static int getRequiredExp(int level) {
        if (level >= MAX_LEVEL) return Integer.MAX_VALUE;
        return (int) (EXP_BASE * Math.pow(level, EXP_EXPONENT));
    }

    // ────────────────────── 초기화 ──────────────────────

    public static void playerInitialSetting(Player player) {
        if (config.contains(player.getUniqueId().toString())) return;
        playerInfoReset(player);
    }

    public static void playerInfoReset(Player player) {
        setLevel(player, INITIAL_LEVEL);
        setExp(player, 0);
        setStatPoint(player, STAT_POINT_PER_LEVEL);
        setInitialStats(player);
        setInitialCurrencies(player);
    }

    private static void setInitialStats(Player player) {
        setStat(player, "str", INITIAL_STAT);
        setStat(player, "con", INITIAL_STAT);
        setStat(player, "agi", INITIAL_STAT);
        setStat(player, "dex", INITIAL_STAT);
        setStat(player, "int", INITIAL_STAT);
        setStat(player, "wis", INITIAL_STAT);
    }

    private static void setInitialCurrencies(Player player) {
        setSia(player, INITIAL_SIA);
    }

    public static void deletePlayerData(Player player) {
        config.set(player.getUniqueId().toString(), null);
        save();
    }

    // ────────────────────── CURRENCIES ──────────────────────

    public static int getSia(Player player) {
        return config.getInt(path(player, "currencies", "sia"), 10000);
    }

    public static void setSia(Player player, int value) {
        config.set(path(player, "currencies", "sia"), value);
        save();
    }

    public static void addSia(Player player, int value) {
        int oldSia = getSia(player);
        int newSia = oldSia + value;
        setSia(player, newSia);
    }

    // ────────────────────── LEVEL / EXP ──────────────────────

    public static int getLevel(Player player) {
        return config.getInt(path(player, "level"), INITIAL_LEVEL);
    }

    public static void setLevel(Player player, int level) {
        config.set(path(player, "level"), level);
        save();
    }

    public static void addLevel(Player player, int amount) {
        int oldLevel = getLevel(player);
        int newLevel = Math.min(oldLevel + amount, MAX_LEVEL);
        levelUp(player, oldLevel, newLevel);
    }

    public static void resetLevel(Player player) {
        playerInfoReset(player);
        save();
    }

    public static double getExp(Player player) {
        return config.getDouble(path(player, "exp"), 0.0);
    }

    public static void setExp(Player player, double exp) {
        config.set(path(player, "exp"), exp);
        save();
    }

    public static void addExp(Player player, double exp) {
        int currentLevel = getLevel(player);

        if (currentLevel >= MAX_LEVEL) {
            setExp(player, 0);
            return;
        }

        double currentExp = getExp(player) + exp;
        int newLevel = currentLevel;

        while (currentExp >= getRequiredExp(newLevel)) {
            currentExp -= getRequiredExp(newLevel);
            newLevel++;
            if (newLevel >= MAX_LEVEL) {
                currentExp = 0;
                break;
            }
        }

        if (newLevel > currentLevel) {
            levelUp(player, currentLevel, newLevel);
        }

        setLevel(player, newLevel);
        setExp(player, currentExp);
    }

    private static void levelUp(Player player, int oldLevel, int newLevel) {
        if (newLevel <= oldLevel) return;

        setLevel(player, newLevel);
        setExp(player, 0); // 경험치 초기화

        addStatPoint(player, (newLevel - oldLevel) * STAT_POINT_PER_LEVEL);

        // 레벨업 이벤트 호출 - 사용자 정의 이벤트로 처리
        Bukkit.getPluginManager().callEvent(new PlayerLevelUpEvent(player, oldLevel, newLevel));
    }

    // ────────────────────── STAT POINT ──────────────────────

    public static int getStatPoint(Player player) {
        return config.getInt(path(player, "stat", "point"), STAT_POINT_PER_LEVEL);
    }

    public static void setStatPoint(Player player, int point) {
        config.set(path(player, "stat", "point"), point);
        save();
    }

    public static void addStatPoint(Player player, int amount) {
        setStatPoint(player, getStatPoint(player) + amount);
    }

    // ────────────────────── INDIVIDUAL STATS ──────────────────────

    public static int getStat(Player player, String statName) {
        return config.getInt(path(player, "stat", statName), INITIAL_STAT);
    }

    public static void setStat(Player player, String statName, int value) {
        config.set(path(player, "stat", statName), value);
        save();
    }

    public static void addStat(Player player, String statName, int amount) {
        setStat(player, statName, getStat(player, statName) + amount);
    }
}
