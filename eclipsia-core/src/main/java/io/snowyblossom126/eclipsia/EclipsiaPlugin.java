package io.snowyblossom126.eclipsia;

import io.snowyblossom126.eclipsia.classes.ClassStorage;
import io.snowyblossom126.eclipsia.commands.EclipsiaCommand;
import io.snowyblossom126.eclipsia.commands.FishCommand;
import io.snowyblossom126.eclipsia.commands.Test;
import io.snowyblossom126.eclipsia.entities.EclipsiaEntity;
import io.snowyblossom126.eclipsia.listeners.FishListener;
import io.snowyblossom126.eclipsia.listeners.LevelUPListener;
import io.snowyblossom126.eclipsia.listeners.MainGUIListener;
import io.snowyblossom126.eclipsia.nms.NMSBridger;
import io.snowyblossom126.eclipsia.nms.NMSHandlerFactory;
import io.snowyblossom126.eclipsia.scheduler.ActionBarScheduler;
import io.snowyblossom126.eclipsia.scheduler.AttributeScheduler;
import io.snowyblossom126.eclipsia.utilities.Log;
import io.snowyblossom126.eclipsia.utilities.Mm;
import io.snowyblossom126.eclipsia.utilities.registry.CustomEnchantmentRegistry;
import io.snowyblossom126.eclipsia.utilities.registry.ElementRegistry;
import io.snowyblossom126.eclipsia.utilities.storage.FishCatalogStorage;
import io.snowyblossom126.eclipsia.utilities.storage.MonthStorage;
import io.snowyblossom126.eclipsia.utilities.storage.PlayerPageStorage;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;
import java.util.logging.Level;

public final class EclipsiaPlugin extends JavaPlugin {
    private static FileConfiguration fishConfig;
    private static EclipsiaPlugin instance;
    private static NMSBridger nms;

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
        ClassStorage.init(this);
        MonthStorage.init(this);
        EclipsiaEntity.init(this);
        PlayerPageStorage.init(this);
        FishCatalogStorage.init(this);

        // 이벤트 리스너 등록
        getServer().getPluginManager().registerEvents(new FishListener(), this);
        getServer().getPluginManager().registerEvents(new MainGUIListener(), this);
        getServer().getPluginManager().registerEvents(new LevelUPListener(), this);

        // 커맨드 등록
        Objects.requireNonNull(getCommand("test")).setExecutor(new Test());
        Objects.requireNonNull(getCommand("fish")).setExecutor(new FishCommand());
        Objects.requireNonNull(getCommand("eclipsia")).setExecutor(new EclipsiaCommand());
        Objects.requireNonNull(getCommand("eclipsia")).setTabCompleter(new EclipsiaCommand());

        // NMS 초기화
        try {
            nms = NMSHandlerFactory.loadNMS();
            getComponentLogger().info(Mm.mm(
                    "<green>NMS 핸들러 활성화 성공! 서버 버전: "
                            + getServer().getBukkitVersion()
                            + ", NMS 버전: "
                            + NMSHandlerFactory.getNMSVersion()
                            + "</green>"));
        } catch (IllegalStateException e) {
            getLogger().severe("NMS 핸들러 활성화 실패: " + e.getMessage());
            getLogger().log(Level.SEVERE, "Exception stacktrace: ", e);
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // 스케줄러 시작
        ActionBarScheduler.start();
        AttributeScheduler.start();

        ElementRegistry.registry(this);
    }

    public void onLoad() {
        CustomEnchantmentRegistry.registry(this);
    }

    public static EclipsiaPlugin getInstance() {
        return instance;
    }

    public static FileConfiguration getFishConfig() {
        return fishConfig;
    }

    public static NMSBridger getNms() {
        return nms;
    }
}