package io.lumpq126.eclipsia.nms.utilities.manager;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;

public class PlayerPageManager {
    private static File file;
    private static FileConfiguration config;
    private static JavaPlugin plugin;

    public static void init(JavaPlugin instance) {
        plugin = instance;
        file = new File(plugin.getDataFolder(), "page.yml");

        if (!file.exists()) {
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                if (!parent.mkdirs()) {
                    plugin.getLogger().warning("[Eclipsia] 디렉토리 생성 실패: " + parent.getAbsolutePath());
                }
            }
            try {
                if (file.createNewFile()) {
                    plugin.getLogger().info("[Eclipsia] 파일 생성 완료: " + file.getName());
                } else {
                    plugin.getLogger().warning("[Eclipsia] 파일 생성 실패: " + file.getName());
                }
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "[Eclipsia] 파일 생성 중 오류 발생", e);
            }
        }

        config = YamlConfiguration.loadConfiguration(file);
    }

    public static void save() {
        if (config == null || file == null) return;

        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "[Eclipsia] 파일 저장 실패", e);
        }
    }

    public static void reload() {
        config = YamlConfiguration.loadConfiguration(file);
    }

    public static void setBookPage(UUID uuid, int page) {
        if (config == null) return;

        config.set(uuid.toString() + ".page", page);
        save();
    }

    public static int getBookPage(UUID uuid) {
        if (config == null) return 0;
        return config.getInt(uuid.toString() + ".page", 0);
    }
}
