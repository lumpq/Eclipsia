package io.lumpq126.eclipsia.utilities;

import org.bukkit.plugin.java.JavaPlugin;

public class Log {
    private static JavaPlugin plugin;

    public static void init(JavaPlugin instance) {
        plugin = instance;
    }

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
