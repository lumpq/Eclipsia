package io.lumpq126.eclipsia.utilities.storage;

import io.lumpq126.eclipsia.events.PlayerExpUpEvent;
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

/**
 * 플레이어의 경험치, 레벨, 스탯, 화폐 등의 정보를 저장하고 관리하는 클래스입니다.
 * playerInfo.yml 파일을 통해 데이터를 저장하고 읽으며,
 * 동기화를 위해 락 객체를 사용합니다.
 */
public class PlayerInfoStorage {
    private static final int MAX_LEVEL = 999;
    private static final int INITIAL_LEVEL = 1;
    private static final int INITIAL_STAT = 5;
    private static final int STAT_POINT_PER_LEVEL = 5;
    private static final int INITIAL_SIA = 10000;

    /** 플레이어 정보가 저장된 파일 */
    private static File file;

    /** Yaml 파일 구성 정보 */
    private static FileConfiguration config;

    /** 동기화 락 객체 */
    private static final Object lock = new Object();

    /** 플러그인 인스턴스 */
    private static JavaPlugin plugin;

    // ────────────────────── INIT / RELOAD / SAVE ──────────────────────

    /**
     * 초기화 메서드로 플러그인 인스턴스를 받고
     * playerInfo.yml 파일이 없으면 생성하며 데이터를 로드합니다.
     * @param instance 플러그인 인스턴스
     */
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

    /**
     * playerInfo.yml 파일을 다시 불러와 config를 갱신합니다.
     */
    public static void reload() {
        try {
            config = YamlConfiguration.loadConfiguration(file);
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "[Eclipsia] playerInfo.yml 로딩 중 오류", e);
        }
    }

    /**
     * config 내용을 playerInfo.yml 파일에 저장합니다.
     */
    public static void save() {
        synchronized (lock) {
            try {
                config.save(file);
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "[Eclipsia] playerInfo.yml 저장 실패", e);
            }
        }
    }

    /**
     * 플레이어의 스탯 키 목록을 반환합니다. 단, "point" 키는 제외합니다.
     * @param player 플레이어 객체
     * @return 스탯 키 문자열 Set
     */
    public static Set<String> getStatKeysExceptPoint(Player player) {
        String statPath = path(player, "stat");
        if (config.getConfigurationSection(statPath) == null) return new HashSet<>();

        Set<String> keys = new HashSet<>(Objects.requireNonNull(config.getConfigurationSection(statPath)).getKeys(false));
        keys.remove("point");  // point 키 제외
        return keys;
    }

    // ────────────────────── PATH UTIL ──────────────────────

    /**
     * 플레이어 UUID 기반으로 config 내 경로를 생성합니다.
     * @param player 플레이어 객체
     * @param keys 하위 경로들
     * @return 합쳐진 경로 문자열
     */
    private static String path(Player player, String... keys) {
        return player.getUniqueId() + "." + String.join(".", keys);
    }

    /**
     * 특정 레벨에 필요한 경험치를 계산합니다.
     * @param level 레벨
     * @return 필요한 경험치 (레벨 999 이상이면 0)
     */
    public static double getRequiredExp(int level) {
        if (level >= MAX_LEVEL) return 0;
        return level * 100;
    }

    // ────────────────────── 초기화 ──────────────────────

    /**
     * 플레이어 정보가 없으면 기본 정보로 초기화합니다.
     * @param player 플레이어 객체
     */
    public static void playerInitialSetting(Player player) {
        if (config.contains(player.getUniqueId().toString())) return;
        playerInfoReset(player);
    }

    /**
     * 플레이어 정보를 초기 상태로 리셋합니다.
     * 레벨, 경험치, 스탯 포인트, 각종 스탯 및 화폐 초기화 포함.
     * @param player 플레이어 객체
     */
    public static void playerInfoReset(Player player) {
        setLevel(player, INITIAL_LEVEL);
        setExp(player, 0);
        setStatPoint(player, STAT_POINT_PER_LEVEL);
        setInitialStats(player);
        setInitialCurrencies(player);
    }

    /**
     * 기본 스탯들을 초기값으로 세팅합니다.
     * @param player 플레이어 객체
     */
    private static void setInitialStats(Player player) {
        setStat(player, "str", INITIAL_STAT);
        setStat(player, "con", INITIAL_STAT);
        setStat(player, "agi", INITIAL_STAT);
        setStat(player, "dex", INITIAL_STAT);
        setStat(player, "int", INITIAL_STAT);
        setStat(player, "wis", INITIAL_STAT);
    }

    /**
     * 초기 화폐 (sia) 값을 세팅합니다.
     * @param player 플레이어 객체
     */
    private static void setInitialCurrencies(Player player) {
        setSia(player, INITIAL_SIA);
    }

    /**
     * 플레이어 데이터를 삭제합니다.
     * @param player 플레이어 객체
     */
    public static void deletePlayerData(Player player) {
        synchronized (lock) {
            config.set(player.getUniqueId().toString(), null);
            save();
        }
    }

    // ────────────────────── CURRENCIES ──────────────────────

    /**
     * 플레이어의 sia 화폐를 반환합니다.
     * @param player 플레이어 객체
     * @return sia 값
     */
    public static int getSia(Player player) {
        return config.getInt(path(player, "currencies", "sia"), INITIAL_SIA);
    }

    /**
     * 플레이어의 sia 화폐를 설정합니다.
     * @param player 플레이어 객체
     * @param value 설정할 sia 값
     */
    public static void setSia(Player player, int value) {
        synchronized (lock) {
            config.set(path(player, "currencies", "sia"), value);
            save();
        }
    }

    /**
     * 플레이어의 sia 화폐를 증가시킵니다.
     * @param player 플레이어 객체
     * @param value 더할 sia 값
     */
    public static void addSia(Player player, int value) {
        int oldSia = getSia(player);
        int newSia = oldSia + value;
        setSia(player, newSia);
    }

    // ────────────────────── LEVEL / EXP ──────────────────────

    /**
     * 플레이어 레벨을 반환합니다.
     * @param player 플레이어 객체
     * @return 플레이어 레벨
     */
    public static int getLevel(Player player) {
        return config.getInt(path(player, "level"), INITIAL_LEVEL);
    }

    /**
     * 플레이어 레벨을 설정합니다.
     * 레벨이 기존보다 높으면 레벨업 로직을 호출합니다.
     * @param player 플레이어 객체
     * @param level 설정할 레벨
     */
    public static synchronized void setLevel(Player player, int level) {
        int clampedLevel = Math.min(level, MAX_LEVEL);
        int oldLevel = getLevel(player);
        if (clampedLevel > oldLevel) {
            levelUp(player, oldLevel, clampedLevel);
        } else {
            synchronized (lock) {
                config.set(path(player, "level"), clampedLevel);
                save();
            }
        }
    }

    /**
     * 플레이어 레벨을 증가시킵니다.
     * @param player 플레이어 객체
     * @param amount 증가시킬 레벨 수
     */
    public static synchronized void addLevel(Player player, int amount) {
        int oldLevel = getLevel(player);
        int newLevel = Math.min(oldLevel + amount, MAX_LEVEL);
        levelUp(player, oldLevel, newLevel);
    }

    /**
     * 플레이어 레벨 및 관련 정보를 초기화합니다.
     * @param player 플레이어 객체
     */
    public static synchronized void resetLevel(Player player) {
        playerInfoReset(player);
        save();
    }

    /**
     * 플레이어 경험치를 반환합니다.
     * @param player 플레이어 객체
     * @return 현재 경험치
     */
    public static double getExp(Player player) {
        return config.getDouble(path(player, "exp"), 0.0);
    }

    /**
     * 플레이어 경험치를 설정합니다.
     * 레벨업 조건이 맞으면 레벨업을 수행합니다.
     * @param player 플레이어 객체
     * @param exp 설정할 경험치
     */
    public static synchronized void setExp(Player player, double exp) {
        int currentLevel = getLevel(player);

        if (currentLevel >= MAX_LEVEL) {
            synchronized (lock) {
                config.set(path(player, "exp"), 0);
                save();
            }
            return;
        }

        double currentExp = exp;
        int newLevel = currentLevel;

        while (currentExp >= getRequiredExp(newLevel) && newLevel < MAX_LEVEL) {
            currentExp -= getRequiredExp(newLevel);
            newLevel++;
        }

        if (newLevel > currentLevel) {
            levelUp(player, currentLevel, newLevel);
        }

        synchronized (lock) {
            config.set(path(player, "level"), newLevel);
            config.set(path(player, "exp"), currentExp);
            save();
        }

        Bukkit.getPluginManager().callEvent(new PlayerExpUpEvent(player, 0, currentExp, newLevel));
    }

    /**
     * 플레이어 경험치를 추가합니다.
     * 레벨업 조건이 맞으면 레벨업을 수행합니다.
     * @param player 플레이어 객체
     * @param exp 추가할 경험치
     */
    public static synchronized void addExp(Player player, double exp) {
        int currentLevel = getLevel(player);
        double oldExp = getExp(player);

        if (currentLevel >= MAX_LEVEL) {
            setExp(player, 0);
            return;
        }

        double currentExp = oldExp + exp;
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

        synchronized (lock) {
            config.set(path(player, "level"), newLevel);
            config.set(path(player, "exp"), currentExp);
            save();
        }

        Bukkit.getPluginManager().callEvent(new PlayerExpUpEvent(player, oldExp, currentExp, newLevel));
    }

    /**
     * 플레이어의 레벨업을 처리합니다.
     * 레벨업 시 경험치를 초기화하고 스탯 포인트를 부여하며 이벤트를 호출합니다.
     * @param player 플레이어 객체
     * @param oldLevel 이전 레벨
     * @param newLevel 새 레벨
     */
    private static void levelUp(Player player, int oldLevel, int newLevel) {
        if (newLevel <= oldLevel) return;

        synchronized (lock) {
            config.set(path(player, "level"), newLevel);
            config.set(path(player, "exp"), 0); // 경험치 초기화
            addStatPoint(player, (newLevel - oldLevel) * STAT_POINT_PER_LEVEL);
            save();
        }

        // 레벨업 이벤트 호출 - 사용자 정의 이벤트로 처리
        Bukkit.getPluginManager().callEvent(new PlayerLevelUpEvent(player, oldLevel, newLevel));
    }

    // ────────────────────── STAT POINT ──────────────────────

    /**
     * 플레이어가 사용할 수 있는 스탯 포인트를 반환합니다.
     * @param player 플레이어 객체
     * @return 사용 가능한 스탯 포인트 수
     */
    public static int getStatPoint(Player player) {
        return config.getInt(path(player, "stat", "point"), STAT_POINT_PER_LEVEL);
    }

    /**
     * 플레이어의 스탯 포인트를 설정합니다.
     * @param player 플레이어 객체
     * @param point 설정할 포인트 수
     */
    public static void setStatPoint(Player player, int point) {
        synchronized (lock) {
            config.set(path(player, "stat", "point"), point);
            save();
        }
    }

    /**
     * 플레이어의 스탯 포인트를 증가시킵니다.
     * @param player 플레이어 객체
     * @param amount 증가할 포인트 수
     */
    public static void addStatPoint(Player player, int amount) {
        setStatPoint(player, getStatPoint(player) + amount);
    }

    // ────────────────────── STAT ──────────────────────

    /**
     * 특정 스탯의 값을 반환합니다.
     * @param player 플레이어 객체
     * @param statName 스탯 이름 (ex. "str", "agi", "dex" 등)
     * @return 스탯 값
     */
    public static int getStat(Player player, String statName) {
        return config.getInt(path(player, "stat", statName), INITIAL_STAT);
    }

    /**
     * 특정 스탯의 값을 설정합니다.
     * @param player 플레이어 객체
     * @param statName 스탯 이름
     * @param value 설정할 값
     */
    public static void setStat(Player player, String statName, int value) {
        synchronized (lock) {
            config.set(path(player, "stat", statName), value);
            save();
        }
    }

    /**
     * 특정 스탯 값을 증가시킵니다.
     * @param player 플레이어 객체
     * @param statName 스탯 이름
     * @param amount 증가할 값
     */
    public static void addStat(Player player, String statName, int amount) {
        setStat(player, statName, getStat(player, statName) + amount);
    }
}
