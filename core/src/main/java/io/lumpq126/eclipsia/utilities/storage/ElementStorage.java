package io.lumpq126.eclipsia.utilities.storage;

import io.lumpq126.eclipsia.elements.Element;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;
import java.util.Objects;

public class ElementStorage {

    public static void load(JavaPlugin plugin) {
        File file = new File(plugin.getDataFolder(), "elements.yml");
        if (!file.exists()) {
            plugin.saveResource("elements.yml", false);
        }
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        // 모든 Element 관계 초기화
        for (Element e : Element.values()) {
            e.clearRelations();
        }

        for (String key : Objects.requireNonNull(config.getConfigurationSection("elements")).getKeys(false)) {
            Element element;
            try {
                element = Element.valueOf(key.toUpperCase());
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Unknown element in config: " + key);
                continue;
            }

            loadList(config.getStringList("elements." + key + ".strengths"), element::addStrength);
            loadList(config.getStringList("elements." + key + ".ultimate_strengths"), element::addUltimateStrength);
            loadList(config.getStringList("elements." + key + ".weaknesses"), element::addWeakness);
            loadList(config.getStringList("elements." + key + ".ultimate_weaknesses"), element::addUltimateWeakness);
            loadList(config.getStringList("elements." + key + ".generals"), element::addGeneral);
            loadList(config.getStringList("elements." + key + ".mutual_strengths"), element::addMutualStrength);
        }

        plugin.getLogger().info("Elements loaded from elements.yml");
    }

    private static void loadList(List<String> list, java.util.function.Consumer<Element> consumer) {
        for (String s : list) {
            try {
                consumer.accept(Element.valueOf(s.toUpperCase()));
            } catch (IllegalArgumentException ignored) {}
        }
    }
}
