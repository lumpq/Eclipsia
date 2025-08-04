package kr.lumpq126.eclipsia.utility.manager;

import kr.lumpq126.eclipsia.EclipsiaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class MonthManager {
    private final EclipsiaPlugin plugin;
    private final File monthFile;
    private static int currentMonth;

    public MonthManager(EclipsiaPlugin plugin) {
        this.plugin = plugin;
        this.monthFile = new File(plugin.getDataFolder(), "month.yml");
        loadMonth();
        startMonthUpdater();
    }

    private void loadMonth() {
        try {
            if (!monthFile.exists()) {
                File parent = monthFile.getParentFile();
                if (parent != null && !parent.exists()) {
                    boolean created = parent.mkdirs();
                    if (!created) {
                        plugin.getLogger().warning("[MonthManager] 월 파일의 부모 디렉토리 생성 실패: " + parent.getAbsolutePath());
                    }
                }
                boolean createdFile = monthFile.createNewFile();
                if (!createdFile) {
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

    private void saveMonth() {
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

    private void startMonthUpdater() {
        new BukkitRunnable() {
            @Override
            public void run() {
                incrementMonth();
            }
        }.runTaskTimer(plugin, 24000L, 24000L);
    }

    private void incrementMonth() {
        currentMonth++;
        if (currentMonth > 12) currentMonth = 1;
        saveMonth();
        plugin.getLogger().info("[RealTimePlugin] 월이 변경되었습니다. 현재 월: " + currentMonth);
    }

    public static int getCurrentMonth() {
        return currentMonth;
    }

    public void setMonth(int month) {
        currentMonth = month;
        saveMonth();
    }
}

