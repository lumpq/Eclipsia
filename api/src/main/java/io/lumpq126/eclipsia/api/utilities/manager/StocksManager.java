package io.lumpq126.eclipsia.api.utilities.manager;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class StocksManager {
    private static File file;
    private static FileConfiguration config;
    private static JavaPlugin plugin;

    public static void init(JavaPlugin instance) {
        plugin = instance;
        file = new File(plugin.getDataFolder(), "stocks.yml");

        if (!file.exists()) {
            File parent = file.getParentFile();
            if (parent != null && !parent.exists() && !parent.mkdirs()) {
                plugin.getLogger().warning("[Eclipsia] 디렉토리 생성 실패: " + parent.getAbsolutePath());
            }

            try {
                if (!file.createNewFile()) {
                    plugin.getLogger().warning("[Eclipsia] 파일 생성 실패");
                }
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "[Eclipsia] 파일 생성 중 오류", e);
            }
        }

        config = YamlConfiguration.loadConfiguration(file);
    }

    public static FileConfiguration getConfig() {
        return config;
    }

    public static void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "[Eclipsia] 저장 실패", e);
        }
    }

    public static void reload() {
        config = YamlConfiguration.loadConfiguration(file);
    }

    private static String path(String... keys) {
        return String.join(".", keys);
    }

    public static double getStock(String name) {
        return config.getDouble(path("stocks." + name));
    }

    public static void setStock(String name, double value) {
        config.set(path("stocks." + name), value);
        save();
    }

    public static void addStock(String name, double value) {
        double oldValue = getStock(name);
        double newValue = oldValue + value;
        setStock(path("stocks." + name), newValue);
    }

    public static void randomStock(String name, int ratio) {
        double maxChange = (double) ratio / 100;
        double oldValue = getStock(name);
        double randomRatio = (Math.random() * 2 * maxChange) - maxChange;
        double newValue = oldValue + (oldValue * randomRatio);
        setStock(name, newValue);
    }
}
