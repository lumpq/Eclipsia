package kr.lumpq126.eclipsia.utilities;

import kr.lumpq126.eclipsia.EclipsiaPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;

public class PlayerPage {
    private static File file;
    private static FileConfiguration config;
    private static EclipsiaPlugin plugin;

    public static void load(EclipsiaPlugin plugin) {
        PlayerPage.plugin = plugin;
        file = new File(PlayerPage.plugin.getDataFolder(), "page.yml");

        if (!file.exists()) {
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                if (!parent.mkdirs()) {
                    PlayerPage.plugin.getLogger().warning("[PageManager] 디렉토리 생성 실패: " + parent.getAbsolutePath());
                }
            }
            try {
                if (file.createNewFile()) {
                    PlayerPage.plugin.getLogger().info("[PageManager] 파일 생성 완료: " + file.getName());
                } else {
                    PlayerPage.plugin.getLogger().warning("[PageManager] 파일 생성 실패: " + file.getName());
                }
            } catch (IOException e) {
                PlayerPage.plugin.getLogger().log(Level.SEVERE, "[PageManager] 파일 생성 중 오류 발생", e);
            }
        }

        config = YamlConfiguration.loadConfiguration(file);
    }

    public static void save() {
        if (config == null || file == null) return;

        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "[PageManager] 파일 저장 실패", e);
        }
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
