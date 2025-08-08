package kr.lumpq126.eclipsia;

import kr.lumpq126.eclipsia.commands.EclipsiaCommand;
import kr.lumpq126.eclipsia.commands.FishCommand;
import kr.lumpq126.eclipsia.listeners.FishListener;
import kr.lumpq126.eclipsia.listeners.LevelUPListener;
import kr.lumpq126.eclipsia.scheduler.ActionBarScheduler;
import kr.lumpq126.eclipsia.ui.listener.MainGUIEvent;
import kr.lumpq126.eclipsia.utilities.manager.FishCatalogManager;
import kr.lumpq126.eclipsia.utilities.manager.PlayerPageManager;
import kr.lumpq126.eclipsia.utilities.manager.MonthManager;
import kr.lumpq126.eclipsia.utilities.manager.PlayerInfoManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;

public final class EclipsiaPlugin extends JavaPlugin {
    private static EclipsiaPlugin instance;
    private static FileConfiguration fishConfig;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        MonthManager.init(this);
        PlayerPageManager.init(this);
        FishCatalogManager.init(this);
        PlayerInfoManager.init(this);

        File file = new File(getDataFolder(), "fish.yml");
        if (!file.exists()) {
            saveResource("fish.yml", false);
        }

        fishConfig = YamlConfiguration.loadConfiguration(file);

        getServer().getPluginManager().registerEvents(new FishListener(), this);
        getServer().getPluginManager().registerEvents(new MainGUIEvent(), this);
        getServer().getPluginManager().registerEvents(new LevelUPListener(), this);

        Objects.requireNonNull(getCommand("fish")).setExecutor(new FishCommand());
        Objects.requireNonNull(getCommand("eclipsia")).setExecutor(new EclipsiaCommand());

        Objects.requireNonNull(getCommand("eclipsia")).setTabCompleter(new EclipsiaCommand());

        Bukkit.getScheduler().runTaskTimer(this, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                ActionBarScheduler.showLevelAndExp(player);
            }
        }, 0L, 1L);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static EclipsiaPlugin getInstance() {
        return instance;
    }

    public static FileConfiguration getFishConfig() {
        return fishConfig;
    }
}
