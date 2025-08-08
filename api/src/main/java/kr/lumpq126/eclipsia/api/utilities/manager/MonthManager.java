package kr.lumpq126.eclipsia.api.utilities.manager;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class MonthManager {
    private static File monthFile;
    private static int currentMonth;
    private static JavaPlugin plugin;

    public static void init(JavaPlugin instance) {
        plugin = instance;
        monthFile = new File(plugin.getDataFolder(), "month.yml");
        loadMonth();
        startMonthUpdater();
    }

    private static void loadMonth() {
        try {
            if (!monthFile.exists()) {
                File parent = monthFile.getParentFile();
                if (parent != null && !parent.exists()) {
                    if (!parent.mkdirs()) {
                        plugin.getLogger().warning("[MonthManager] 월 파일의 부모 디렉토리 생성 실패: " + parent.getAbsolutePath());
                    }
                }
                if (!monthFile.createNewFile()) {
                    plugin.getLogger().warning("[MonthManager] 월 파일 생성 실패: " + monthFile.getAbsolutePath());
                }
                currentMonth = 1;
                saveMonth();
            } else {
                Yaml yaml = new Yaml();
                Map<String, Object> data = yaml.load(Files.newBufferedReader(monthFile.toPath()));
                currentMonth = (int) data.getOrDefault("month", 1);
            }
        } catch (IOException e) {
            plugin.getLogger().severe("[MonthManager] 월 파일 로드 실패: " + e.getMessage());
            plugin.getLogger().severe(java.util.Arrays.toString(e.getStackTrace()));
            currentMonth = 1;
        }
    }

    private static void saveMonth() {
        try (FileWriter writer = new FileWriter(monthFile)) {
            Yaml yaml = new Yaml();
            Map<String, Object> data = new HashMap<>();
            data.put("month", currentMonth);
            yaml.dump(data, writer);
        } catch (IOException e) {
            plugin.getLogger().severe("[MonthManager] 월 파일 저장 실패: " + e.getMessage());
            plugin.getLogger().severe(java.util.Arrays.toString(e.getStackTrace()));
        }
    }

    private static void startMonthUpdater() {
        new BukkitRunnable() {
            @Override
            public void run() {
                incrementMonth();
            }
        }.runTaskTimer(plugin, 24000L, 24000L); // 20 ticks * 60s * 20m = 24000L
    }

    private static void incrementMonth() {
        currentMonth++;
        if (currentMonth > 12) currentMonth = 1;
        saveMonth();
        plugin.getLogger().info("[Eclipsia] 월이 변경되었습니다. 현재 월: " + currentMonth);
    }

    public static int getCurrentMonth() {
        return currentMonth;
    }

    public static void setMonth(int month) {
        currentMonth = month;
        saveMonth();
    }
}
