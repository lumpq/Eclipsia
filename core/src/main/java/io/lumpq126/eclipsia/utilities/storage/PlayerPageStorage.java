package io.lumpq126.eclipsia.utilities.storage;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;

/**
 * 플레이어별 책 페이지 정보를 저장하고 불러오는 기능을 제공하는 클래스입니다.
 * player의 UUID를 키로 하여 현재 보고 있는 책의 페이지 번호를 기록합니다.
 * 정보는 플러그인 데이터 폴더 내 page.yml 파일에 저장됩니다.
 */
public class PlayerPageStorage {
    /** 페이지 정보를 저장하는 파일 */
    private static File file;

    /** 페이지 정보가 담긴 Yaml 구성 객체 */
    private static FileConfiguration config;

    /** 플러그인 메인 인스턴스 */
    private static JavaPlugin plugin;

    /**
     * 초기화 메서드로 플러그인 인스턴스를 받아
     * 저장 파일과 구성 객체를 준비합니다.
     * 파일이 없으면 새로 생성합니다.
     *
     * @param instance 플러그인 인스턴스
     */
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

    /**
     * 현재 메모리상의 구성 객체를 파일에 저장합니다.
     */
    public static void save() {
        if (config == null || file == null) return;

        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "[Eclipsia] 파일 저장 실패", e);
        }
    }

    /**
     * 저장 파일을 다시 읽어 Yaml 구성 객체를 갱신합니다.
     */
    public static void reload() {
        config = YamlConfiguration.loadConfiguration(file);
    }

    /**
     * 플레이어의 책 페이지 번호를 저장합니다.
     *
     * @param uuid 플레이어 UUID
     * @param page 현재 페이지 번호
     */
    public static void setBookPage(UUID uuid, int page) {
        if (config == null) return;

        config.set(uuid.toString() + ".page", page);
        save();
    }

    /**
     * 플레이어가 현재 보고 있는 책 페이지 번호를 반환합니다.
     * 기본값은 0입니다.
     *
     * @param uuid 플레이어 UUID
     * @return 페이지 번호 (없으면 0)
     */
    public static int getBookPage(UUID uuid) {
        if (config == null) return 0;
        return config.getInt(uuid.toString() + ".page", 0);
    }
}
