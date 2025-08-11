package io.lumpq126.eclipsia;

import io.lumpq126.eclipsia.commands.EclipsiaCommand;
import io.lumpq126.eclipsia.commands.FishCommand;
import io.lumpq126.eclipsia.listeners.FishListener;
import io.lumpq126.eclipsia.listeners.LevelUPListener;
import io.lumpq126.eclipsia.listeners.MainGUIListener;
import io.lumpq126.eclipsia.nms.NMSHandler;
import io.lumpq126.eclipsia.nms.NMSHandlerFactory;
import io.lumpq126.eclipsia.scheduler.AttributeScheduler;
import io.lumpq126.eclipsia.utilities.Mm;
import io.lumpq126.eclipsia.utilities.manager.FishCatalogManager;
import io.lumpq126.eclipsia.utilities.manager.MonthManager;
import io.lumpq126.eclipsia.utilities.manager.PlayerInfoManager;
import io.lumpq126.eclipsia.utilities.manager.PlayerPageManager;
import io.lumpq126.eclipsia.scheduler.ActionBarScheduler;
import net.dv8tion.jda.api.JDA;
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
    private static JDA jda;

    @Override
    public void onEnable() {
        File file = new File(getDataFolder(), "fish.yml");
        if (!file.exists()) {
            saveResource("fish.yml", false);
        }

        saveDefaultConfig();

        instance = this;
        fishConfig = YamlConfiguration.loadConfiguration(file);

        MonthManager.init(this);
        PlayerInfoManager.init(this);
        PlayerPageManager.init(this);
        FishCatalogManager.init(this);

        getServer().getPluginManager().registerEvents(new FishListener(), this);
        getServer().getPluginManager().registerEvents(new MainGUIListener(), this);
        getServer().getPluginManager().registerEvents(new LevelUPListener(), this);

        Objects.requireNonNull(getCommand("fish")).setExecutor(new FishCommand());
        Objects.requireNonNull(getCommand("eclipsia")).setExecutor(new EclipsiaCommand());

        Objects.requireNonNull(getCommand("eclipsia")).setTabCompleter(new EclipsiaCommand());

        try {
            nms = NMSHandlerFactory.loadNMS();
            getComponentLogger().info(Mm.mm(
                    "<green>NMS 핸들러 활성화 성공! 서버 버전: " + getServer().getBukkitVersion() + ", NMS 버전: " + NMSHandlerFactory.getNMSVersion() + "</green>"));
        } catch (UnsupportedOperationException e) {
            getLogger().severe("NMS 핸들러 활성화 실패: " + e.getMessage());
            getLogger().log(Level.SEVERE, "Exception stacktrace: ", e);
            getServer().getPluginManager().disablePlugin(this);
        }

        /*
        getServer().getScheduler().runTaskAsynchronously(this, () -> {
            try {
                jda = JDABuilder.createDefault(getConfig().getString("token"))
                        .build()
                        .awaitReady();
                getComponentLogger().info(Mm.mm(
                        "<green>디스코드 봇 활성화 성공! 봇 토큰: " + "<gradient:#b266ff:#e5ccff>" + getConfig().getString("token") + "</gradient>"
                ));
            } catch (Exception e) {
                getLogger().severe("디스코드 봇 활성화 실패");
                getLogger().log(Level.SEVERE, "Exception stacktrace: ", e);
                getServer().getPluginManager().disablePlugin(this);
            }
        });

         */

        ActionBarScheduler.start();
        AttributeScheduler.start();
    }

    @Override
    public void onDisable() {
        if (jda != null) {
            jda.shutdown();
        }
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

    public static JDA getJda() {
        return jda;
    }
}
