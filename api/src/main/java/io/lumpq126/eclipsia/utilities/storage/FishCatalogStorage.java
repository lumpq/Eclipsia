package io.lumpq126.eclipsia.utilities.storage;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

/**
 * 플레이어가 잠금 해제한 물고기 정보를 저장하고 불러오는 저장소 클래스입니다.
 * fishCatalog.yml 파일을 사용하며, 플레이어 UUID별로 unlocked된 fishId 목록을 관리합니다.
 */
public class FishCatalogStorage {
    private static File file;
    private static FileConfiguration config;
    private static JavaPlugin plugin;

    /**
     * 초기화 메서드로, 플러그인 인스턴스를 받아
     * 저장 파일을 생성하거나 불러옵니다.
     *
     * @param instance JavaPlugin 인스턴스
     */
    public static void init(JavaPlugin instance) {
        plugin = instance;
        file = new File(plugin.getDataFolder(), "fishCatalog.yml");

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

    /**
     * fishCatalog.yml 파일의 FileConfiguration 객체를 반환합니다.
     *
     * @return 저장된 설정 객체
     */
    public static FileConfiguration getConfig() {
        return config;
    }

    /**
     * 변경된 설정을 파일에 저장합니다.
     */
    public static void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "[Eclipsia] 저장 실패", e);
        }
    }

    /**
     * 설정 파일을 다시 불러옵니다.
     */
    public static void reload() {
        config = YamlConfiguration.loadConfiguration(file);
    }

    /**
     * 플레이어가 특정 물고기를 잠금 해제했음을 기록합니다.
     * 이미 잠금 해제된 경우 중복 저장하지 않습니다.
     *
     * @param player 플레이어 객체
     * @param fishId 물고기 ID 문자열
     */
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

    /**
     * 플레이어가 잠금 해제한 물고기 ID 리스트를 반환합니다.
     *
     * @param player 플레이어 객체
     * @return 잠금 해제한 물고기 ID 리스트 (없으면 빈 리스트)
     */
    public static List<String> getUnlockedFish(Player player) {
        reload();
        return config.getStringList(player.getUniqueId().toString() + ".unlocked");
    }

    /**
     * 플레이어가 특정 물고기를 잠금 해제했는지 여부를 반환합니다.
     *
     * @param player 플레이어 객체
     * @param fishId 물고기 ID 문자열
     * @return 잠금 해제했으면 true, 아니면 false
     */
    public static boolean isFishUnlocked(Player player, String fishId) {
        return getUnlockedFish(player).contains(fishId);
    }
}
