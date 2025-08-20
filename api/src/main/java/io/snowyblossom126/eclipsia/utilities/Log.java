package io.snowyblossom126.eclipsia.utilities;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * 플러그인 전역 로그 유틸리티 클래스
 * <p>
 * 플러그인 인스턴스를 초기화한 후(info, warn, error, debug, trace) 로그를 출력할 수 있습니다.
 */
public class Log {
    private static JavaPlugin plugin;

    /**
     * 로그 유틸리티 초기화
     *
     * @param instance 플러그인 인스턴스
     */
    public static void init(JavaPlugin instance) {
        plugin = instance;
    }

    /**
     * 지정한 타입으로 로그를 출력합니다.
     *
     * @param type      로그 타입 ("error", "info", "warn", "debug", "trace")
     * @param message   로그 메시지
     * @param throwable 예외 (없으면 null)
     */
    public static void log(String type, String message, Throwable throwable) {
        if (plugin == null) {
            throw new IllegalStateException("Log plugin not initialized. Call Log.init() first.");
        }
        switch (type.toLowerCase()) {
            case "error" -> plugin.getComponentLogger().error(Mm.mm(message), throwable);
            case "info"  -> plugin.getComponentLogger().info(Mm.mm(message), throwable);
            case "warn"  -> plugin.getComponentLogger().warn(Mm.mm(message), throwable);
            case "debug" -> plugin.getComponentLogger().debug(Mm.mm(message), throwable);
            case "trace" -> plugin.getComponentLogger().trace(Mm.mm(message), throwable);
        }
    }
}
