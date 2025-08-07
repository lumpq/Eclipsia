package kr.lumpq126.eclipsia.utilities.manager;

import kr.lumpq126.eclipsia.EclipsiaPlugin;
import kr.lumpq126.eclipsia.level.event.PlayerLevelUpEvent;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class PlayerInfoManager {
    private static File file;
    private static FileConfiguration config;

    public static void load(EclipsiaPlugin plugin) {
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
        config = YamlConfiguration.loadConfiguration(file);
    }

    public static void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            EclipsiaPlugin.getInstance().getLogger().log(Level.SEVERE, "[Eclipsia] PlayerInfo.yml 저장 실패", e);
        }
    }

    public static FileConfiguration getConfig() {
        return config;
    }

    // ────────────────────── UTIL ──────────────────────

    private static String path(Player player, String... keys) {
        return player.getUniqueId() + "." + String.join(".", keys);
    }

    private static int getRequiredExp(int level) {
        if (level >= 999) return Integer.MAX_VALUE;
        double base = 10;
        double exponent = 2.25;
        return (int) (base * Math.pow(level, exponent));
    }

    // ────────────────────── INITIAL ──────────────────────

    public static void playerInitialSetting(Player player) {
        if (config.getKeys(false).contains(player.getUniqueId().toString())) return;
        setLevel(player, 1);
        setExp(player, 0);
        setStatPoint(player, 5);
        setStat(player, "str", 5);
        setStat(player, "con", 5);
        setStat(player, "agi", 5);
        setStat(player, "dex", 5);
        setStat(player, "int", 5);
        setStat(player, "wis", 5);
    }

    // ────────────────────── LEVEL / EXP ──────────────────────

    public static int getLevel(Player player) {
        return config.getInt(path(player, "level"), 1);
    }

    public static void setLevel(Player player, int level) {
        config.set(path(player, "level"), level);
        save();
    }

    public static void addLevel(Player player, int amount) {
        setLevel(player, getLevel(player) + amount);
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

        // 이미 맥스레벨 도달 시 경험치 초기화 및 추가 경험치 무시
        if (currentLevel >= 999) {
            setExp(player, 0);
            return;
        }

        double currentExp = getExp(player);
        double newExp = currentExp + exp;
        int newLevel = currentLevel;

        while (newExp >= getRequiredExp(newLevel)) {
            newExp -= getRequiredExp(newLevel);
            newLevel++;
            if (newLevel >= 999) {
                newExp = 0;  // 맥스레벨 도달 시 경험치 초기화
                break;
            }
        }

        if (newLevel > currentLevel) {
            int levelGain = newLevel - currentLevel;

            // 레벨업 이벤트 호출
            PlayerLevelUpEvent levelUpEvent = new PlayerLevelUpEvent(player, currentLevel, newLevel);
            Bukkit.getPluginManager().callEvent(levelUpEvent);

            // 레벨당 5 포인트씩 능력치 포인트 지급
            addStatPoint(player, levelGain * 5);
        }

        setLevel(player, newLevel);
        setExp(player, newExp);
    }

    // ────────────────────── STAT POINT ──────────────────────

    public static int getStatPoint(Player player) {
        return config.getInt(path(player, "stat", "point"), 5);
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
        return config.getInt(path(player, "stat", statName), 5);
    }

    public static void setStat(Player player, String statName, int value) {
        config.set(path(player, "stat", statName), value);
        save();
    }

    public static void addStat(Player player, String statName, int amount) {
        setStat(player, statName, getStat(player, statName) + amount);
    }
}
