package kr.lumpq126.eclipsia.utilities.manager;

import kr.lumpq126.eclipsia.EclipsiaPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

public class FishCatalogManager {
    private static File file;
    private static FileConfiguration config;
    private static EclipsiaPlugin plugin;

    public static void init(EclipsiaPlugin pluginInstance) {
        plugin = pluginInstance;
        file = new File(plugin.getDataFolder(), "fishCatalog.yml");

        if (!file.exists()) {
            File parent = file.getParentFile();
            if (parent != null && !parent.exists() && !parent.mkdirs()) {
                plugin.getLogger().warning("[FishCatalogManager] 디렉토리 생성 실패: " + parent.getAbsolutePath());
            }

            try {
                if (!file.createNewFile()) {
                    plugin.getLogger().warning("[FishCatalogManager] 파일 생성 실패");
                }
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "[FishCatalogManager] 파일 생성 중 오류", e);
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
            plugin.getLogger().log(Level.SEVERE, "[FishCatalogManager] 저장 실패", e);
        }
    }

    public static void reload() {
        config = YamlConfiguration.loadConfiguration(file);
    }

    public static void addUnlockedFish(Player player, String fishId) {
        String uuid = player.getUniqueId().toString();
        String path = uuid + ".unlocked";
        List<String> unlocked = config.getStringList(path);

        if (!unlocked.contains(fishId)) {
            unlocked.add(fishId);
            config.set(path, unlocked);
            save();
        }
    }

    public static List<String> getUnlockedFish(Player player) {
        reload();
        return config.getStringList(player.getUniqueId().toString() + ".unlocked");
    }

    public static boolean isFishUnlocked(Player player, String fishId) {
        reload();
        return getUnlockedFish(player).contains(fishId);
    }
}
