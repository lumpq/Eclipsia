package io.lumpq126.eclipsia.skills.storage;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;

public class CooldownStorage {
    private static JavaPlugin plugin;
    private static File cooldownFile;
    private static FileConfiguration cooldownConfig;

    public static void init(JavaPlugin instance) {
        plugin = instance;
        loadCooldownFile();
    }

    private static void loadCooldownFile() {
        cooldownFile = new File(plugin.getDataFolder(), "cooldown.yml");
        if (!cooldownFile.exists()) {
            cooldownFile.getParentFile().mkdirs();
            plugin.saveResource("cooldown.yml", false);
        }
        cooldownConfig = YamlConfiguration.loadConfiguration(cooldownFile);
    }

    public static int getCooldown(UUID playerId, String skillName) {
        String path = "players." + playerId.toString() + "." + skillName;
        return cooldownConfig.getInt(path, 0);
    }

    public static void setCooldown(UUID playerId, String skillName, int seconds) {
        String path = "players." + playerId.toString() + "." + skillName;
        cooldownConfig.set(path, seconds);
        saveCooldownFile();
    }

    public static void tick() {
        for (String playerIdStr : Objects.requireNonNull(cooldownConfig.getConfigurationSection("players")).getKeys(false)) {
            for (String skillName : Objects.requireNonNull(cooldownConfig.getConfigurationSection("players." + playerIdStr)).getKeys(false)) {
                int time = cooldownConfig.getInt("players." + playerIdStr + "." + skillName);
                if (time > 0) {
                    cooldownConfig.set("players." + playerIdStr + "." + skillName, time - 1);
                }
            }
        }
        saveCooldownFile();
    }

    private static void saveCooldownFile() {
        try {
            cooldownConfig.save(cooldownFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "", e);
        }
    }

    public static void start() {
        Bukkit.getScheduler().runTaskTimer(plugin, CooldownStorage::tick, 20L, 20L);
    }
}
