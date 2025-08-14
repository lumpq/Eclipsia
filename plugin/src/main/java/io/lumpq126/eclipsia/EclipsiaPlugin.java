package io.lumpq126.eclipsia;

import io.lumpq126.eclipsia.commands.EclipsiaCommand;
import io.lumpq126.eclipsia.commands.ElementReload;
import io.lumpq126.eclipsia.commands.FishCommand;
import io.lumpq126.eclipsia.entities.EclipsiaEntity;
import io.lumpq126.eclipsia.listeners.ElementListener;
import io.lumpq126.eclipsia.listeners.FishListener;
import io.lumpq126.eclipsia.listeners.LevelUPListener;
import io.lumpq126.eclipsia.listeners.MainGUIListener;
import io.lumpq126.eclipsia.nms.NMSHandler;
import io.lumpq126.eclipsia.nms.NMSHandlerFactory;
import io.lumpq126.eclipsia.scheduler.ActionBarScheduler;
import io.lumpq126.eclipsia.scheduler.AttributeScheduler;
import io.lumpq126.eclipsia.utilities.Log;
import io.lumpq126.eclipsia.utilities.Mm;
import io.lumpq126.eclipsia.utilities.storage.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;
import java.util.logging.Level;

public final class EclipsiaPlugin extends JavaPlugin {
    private static FileConfiguration fishConfig;
    private static EclipsiaPlugin instance;
    private static NMSHandler nms;

    @Override
    public void onEnable() {
        File file = new File(getDataFolder(), "fish.yml");
        if (!file.exists()) {
            saveResource("fish.yml", false);
        }

        saveDefaultConfig();

        instance = this;
        fishConfig = YamlConfiguration.loadConfiguration(file);

        Log.init(this);
        MonthStorage.init(this);
        EclipsiaEntity.init(this);
        PlayerPageStorage.init(this);
        FishCatalogStorage.init(this);

        ElementStorage.load(this);

        getServer().getPluginManager().registerEvents(new FishListener(), this);
        getServer().getPluginManager().registerEvents(new MainGUIListener(), this);
        getServer().getPluginManager().registerEvents(new LevelUPListener(), this);
        getServer().getPluginManager().registerEvents(new ElementListener(), this);

        Objects.requireNonNull(getCommand("fish")).setExecutor(new FishCommand());
        Objects.requireNonNull(getCommand("eclipsia")).setExecutor(new EclipsiaCommand());
        Objects.requireNonNull(getCommand("element")).setExecutor(new ElementReload(this));

        Objects.requireNonNull(getCommand("eclipsia")).setTabCompleter(new EclipsiaCommand());

        try {
            nms = NMSHandlerFactory.loadNMS();
            getComponentLogger().info(Mm.mm(
                    "<green>NMS 핸들러 활성화 성공! 서버 버전: " 
                    + getServer().getBukkitVersion() 
                    + ", NMS 버전: " 
                    + NMSHandlerFactory.getNMSVersion() 
                    + "</green>"));
        } catch (IllegalStateException e) { // 변경됨
            getLogger().severe("NMS 핸들러 활성화 실패: " + e.getMessage());
            getLogger().log(Level.SEVERE, "Exception stacktrace: ", e);
            getServer().getPluginManager().disablePlugin(this);
            return; // 안전하게 종료
        }

        ActionBarScheduler.start();
        AttributeScheduler.start();
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