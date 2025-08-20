package io.snowyblossom126.eclipsia.core.mechanics.classes;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * ClassStorage
 * <p>
 * 플레이어의 Class 정보(직업 및 전직 단계)를 Bukkit PersistentDataContainer를 통해
 * 저장 및 불러오는 클래스입니다.
 * </p>
 *
 * <p>
 * PersistentDataContainer를 이용하여 서버 재시작 시에도 데이터를 유지할 수 있습니다.
 * </p>
 */
public class ClassStorage {

    /** 플레이어 직업 저장용 NamespacedKey */
    private static NamespacedKey classKey;

    /** 플레이어 전직 단계 저장용 NamespacedKey */
    private static NamespacedKey stageKey;

    /**
     * 초기화 메서드
     * <p>
     * 서버 시작 시 플러그인 인스턴스를 이용하여 반드시 호출해야 함.
     * </p>
     *
     * @param plugin JavaPlugin 인스턴스
     */
    public static void init(JavaPlugin plugin) {
        classKey = new NamespacedKey(plugin, "player_class");
        stageKey = new NamespacedKey(plugin, "class_stage");
    }

    // -----------------------------
    // 저장 메서드
    // -----------------------------

    /**
     * 플레이어의 Class 정보 저장
     *
     * @param player 저장 대상 플레이어
     * @param clazz  플레이어 직업
     * @param stage  전직 단계
     */
    public static void savePlayerClass(Player player, Class clazz, int stage) {
        PersistentDataContainer container = player.getPersistentDataContainer();
        container.set(classKey, PersistentDataType.STRING, clazz.name());
        container.set(stageKey, PersistentDataType.INTEGER, stage);
    }

    // -----------------------------
    // 로드 메서드
    // -----------------------------

    /**
     * 플레이어의 Class 정보 로드
     *
     * @param player 대상 플레이어
     * @return 플레이어의 ClassInfo 객체 (직업 + 단계)
     */
    public static ClassInfo loadPlayerClass(Player player) {
        PersistentDataContainer container = player.getPersistentDataContainer();
        String className = container.get(classKey, PersistentDataType.STRING);
        int stage = container.getOrDefault(stageKey, PersistentDataType.INTEGER, 0);

        Class clazz = Class.fromNameOrDefault(className);
        return new ClassInfo(clazz, stage);
    }

    // -----------------------------
    // 내부 데이터 클래스
    // -----------------------------

    /**
     * <h2>ClassInfo</h2>
     * <p>
     * 플레이어의 현재 Class와 전직 단계를 나타내는 불변 데이터 클래스
     * </p>
     */
    public record ClassInfo(Class clazz, int stage) { }
}
