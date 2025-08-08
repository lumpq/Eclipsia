package io.lumpq126.eclipsia;

import io.lumpq126.eclipsia.api.utilities.manager.FishCatalogManager;
import io.lumpq126.eclipsia.api.utilities.manager.MonthManager;
import io.lumpq126.eclipsia.api.utilities.manager.PlayerInfoManager;
import io.lumpq126.eclipsia.api.utilities.manager.PlayerPageManager;
import io.lumpq126.eclipsia.nms.NMSHandler;
import io.lumpq126.eclipsia.nms.NMSHandlerFactory;
import io.lumpq126.eclipsia.commands.EclipsiaCommand;
import io.lumpq126.eclipsia.commands.FishCommand;
import io.lumpq126.eclipsia.listeners.FishListener;
import io.lumpq126.eclipsia.listeners.LevelUPListener;
import io.lumpq126.eclipsia.scheduler.ActionBarScheduler;
import io.lumpq126.eclipsia.listeners.MainGUIEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;
import java.util.logging.Level;

public final class EclipsiaPlugin extends JavaPlugin {
    private static EclipsiaPlugin instance;
    private static FileConfiguration fishConfig;
    private static NMSHandler nms;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        MonthManager.init(this);
        PlayerInfoManager.init(this);
        PlayerPageManager.init(this);
        FishCatalogManager.init(this);

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

        try {
            nms = NMSHandlerFactory.loadNMS();
            getComponentLogger().info(MiniMessage.miniMessage().deserialize(
                    "<green>NMS 핸들러 생성 성공! 서버 버전: " + getServer().getBukkitVersion() + ", NMS 버전: " + NMSHandlerFactory.getNMSVersion() + "</green>"));
        } catch (UnsupportedOperationException e) {
            getLogger().severe("NMS 핸들러 생성 실패: " + e.getMessage());
            getLogger().log(Level.SEVERE, "Exception stacktrace:", e);
            getServer().getPluginManager().disablePlugin(this);
        }
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

    public static NMSHandler getNms() {
        return nms;
    }
}
