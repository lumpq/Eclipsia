package io.lumpq126.eclipsia.utilities.storage;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

/**
 * 월 정보를 저장하고 관리하는 클래스입니다.
 * 플러그인 폴더 내의 month.yml 파일을 통해 현재 월 정보를 읽고 쓰며,
 * 주기적으로 월을 증가시키는 작업을 수행합니다.
 */
public class MonthStorage {
    /** 월 정보를 저장하는 파일 */
    private static File file;

    /** Yaml 파일의 구성 정보 */
    private static FileConfiguration config;

    /** 현재 월 (1~12) */
    private static int currentMonth;

    /** 플러그인 인스턴스 */
    private static JavaPlugin plugin;

    /**
     * 초기화 메서드로, 플러그인 인스턴스를 받아 저장 파일을 지정하고
     * 월 정보를 불러오며 월 갱신 작업을 시작합니다.
     * @param instance 플러그인 메인 클래스(JavaPlugin 인스턴스)
     */
    public static void init(JavaPlugin instance) {
        plugin = instance;
        file = new File(plugin.getDataFolder(), "month.yml");
        loadMonth();
        startMonthUpdater();
    }

    /**
     * month.yml 파일에서 현재 월 정보를 로드합니다.
     * 파일이 없으면 새로 생성하고 초기 월을 1로 설정합니다.
     */
    private static void loadMonth() {
        try {
            if (!file.exists()) {
                File parent = file.getParentFile();
                if (parent != null && !parent.exists()) {
                    if (!parent.mkdirs()) {
                        plugin.getLogger().warning("[Eclipsia] 월 파일의 부모 디렉토리 생성 실패: " + parent.getAbsolutePath());
                    }
                }
                if (!file.createNewFile()) {
                    plugin.getLogger().warning("[Eclipsia] 월 파일 생성 실패: " + file.getAbsolutePath());
                }
                currentMonth = 1;
                saveMonth();
            } else {
                Yaml yaml = new Yaml();
                Map<String, Object> data = yaml.load(Files.newBufferedReader(file.toPath()));
                currentMonth = (int) data.getOrDefault("month", 1);
            }
        } catch (IOException e) {
            plugin.getLogger().severe("[Eclipsia] 월 파일 로드 실패: " + e.getMessage());
            plugin.getLogger().severe(java.util.Arrays.toString(e.getStackTrace()));
            currentMonth = 1;
        }
    }

    /**
     * YamlConfiguration을 다시 불러와서 config를 갱신합니다.
     */
    public static void reload() {
        config = YamlConfiguration.loadConfiguration(file);
    }

    /**
     * 현재 월 정보를 month.yml 파일에 저장합니다.
     */
    private static void saveMonth() {
        try (FileWriter writer = new FileWriter(file)) {
            Yaml yaml = new Yaml();
            Map<String, Object> data = new HashMap<>();
            data.put("month", currentMonth);
            yaml.dump(data, writer);
        } catch (IOException e) {
            plugin.getLogger().severe("[Eclipsia] 월 파일 저장 실패: " + e.getMessage());
            plugin.getLogger().severe(java.util.Arrays.toString(e.getStackTrace()));
        }
    }

    /**
     * Bukkit 스케줄러를 이용해 20분마다 월을 1씩 증가시키는 작업을 시작합니다.
     */
    private static void startMonthUpdater() {
        new BukkitRunnable() {
            @Override
            public void run() {
                incrementMonth();
            }
        }.runTaskTimer(plugin, 24000L, 24000L); // 20 ticks * 60s * 20m = 24000L
    }

    /**
     * 월을 1 증가시키며 12월을 넘으면 1월로 초기화하고 저장 및 로그를 남깁니다.
     */
    private static void incrementMonth() {
        currentMonth++;
        if (currentMonth > 12) currentMonth = 1;
        saveMonth();
        plugin.getLogger().info("[Eclipsia] 월이 변경되었습니다. 현재 월: " + currentMonth);
    }

    /**
     * 현재 월을 반환합니다.
     * @return 현재 월 (1~12)
     */
    public static int getCurrentMonth() {
        return currentMonth;
    }

    /**
     * 현재 월을 지정한 값으로 설정하고 저장합니다.
     * @param month 설정할 월 (1~12 권장)
     */
    public static void setMonth(int month) {
        currentMonth = month;
        saveMonth();
    }

    public static FileConfiguration getConfig() {
        return config;
    }
}
