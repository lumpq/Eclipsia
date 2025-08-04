package kr.lumpq126.eclipsia;

import kr.lumpq126.eclipsia.command.FishCommand;
import kr.lumpq126.eclipsia.command.MonthCommand;
import kr.lumpq126.eclipsia.listener.FishListener;
import kr.lumpq126.eclipsia.utility.FishCatalog;
import kr.lumpq126.eclipsia.utility.PlayerPage;
import kr.lumpq126.eclipsia.utility.manager.MonthManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;
import java.util.logging.Logger;

public final class EclipsiaPlugin extends JavaPlugin {
    private static EclipsiaPlugin instance;
    private static FileConfiguration fishConfig;

    @Override
    public void onEnable() {
        instance = this;

        PlayerPage.load(this);
        PlayerPage.save();
        FishCatalog.load(this);
        FishCatalog.save();

        File folder = new File(getDataFolder(), "fish");
        if (!folder.exists() && !folder.mkdirs()) {
            Logger logger = getLogger();
            logger.warning("디렉터리 생성 실패: " + folder.getAbsolutePath());
        }

        File file = new File(folder, "fish.yml");
        if (!file.exists()) {
            saveResource("fish/fish.yml", false);
        }

        fishConfig = YamlConfiguration.loadConfiguration(file);

        getServer().getPluginManager().registerEvents(new FishListener(), this);

        Objects.requireNonNull(getCommand("month")).setExecutor(new MonthCommand(new MonthManager(this)));
        Objects.requireNonNull(getCommand("fish")).setExecutor(new FishCommand());
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
