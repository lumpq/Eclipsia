package io.lumpq126.eclipsia.utilities.storage;

import io.lumpq126.eclipsia.elements.Element;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class ElementStorage {

    public static void load(JavaPlugin plugin) {
        File file = new File(plugin.getDataFolder(), "elements.yml");
        if (!file.exists()) {
            plugin.saveResource("elements.yml", false);
        }
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        for (Element e : Element.values()) {
            e.clearRelations();
        }

        for (String key : Objects.requireNonNull(config.getConfigurationSection("elements")).getKeys(false)) {
            Element element;
            try {
                element = Element.valueOf(key.toUpperCase());
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("⚠ Unknown element in config: " + key);
                continue;
            }

            loadList(config.getStringList("elements." + key + ".strengths"), element::addStrength, plugin, element, "strengths");
            loadList(config.getStringList("elements." + key + ".ultimate_strengths"), element::addUltimateStrength, plugin, element, "ultimate_strengths");
            loadList(config.getStringList("elements." + key + ".weaknesses"), element::addWeakness, plugin, element, "weaknesses");
            loadList(config.getStringList("elements." + key + ".ultimate_weaknesses"), element::addUltimateWeakness, plugin, element, "ultimate_weaknesses");
            loadList(config.getStringList("elements." + key + ".generals"), element::addGeneral, plugin, element, "generals");

            // mutualStrengths 양방향 동기화
            for (String s : config.getStringList("elements." + key + ".mutual_strengths")) {
                try {
                    Element target = Element.valueOf(s.toUpperCase());
                    element.addMutualStrength(target);
                    target.addMutualStrength(element);
                } catch (IllegalArgumentException ex) {
                    plugin.getLogger().warning("⚠ Unknown mutual_strength element in " + key + ": " + s);
                }
            }
        }

        // 충돌 관계 검증
        for (Element e : Element.values()) {
            Set<Element> s1 = e.getStrengths();
            Set<Element> s2 = e.getWeaknesses();
            s1.retainAll(s2);
            if (!s1.isEmpty()) {
                plugin.getLogger().warning("⚠ Conflict detected in element " + e.name() + ": in both strengths and weaknesses -> " + s1);
            }
        }

        plugin.getLogger().info("✅ Elements loaded from elements.yml");
    }

    private static void loadList(List<String> list, java.util.function.Consumer<Element> consumer, JavaPlugin plugin, Element parent, String category) {
        for (String s : list) {
            try {
                consumer.accept(Element.valueOf(s.toUpperCase()));
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("⚠ Unknown element in " + parent.name() + " -> " + category + ": " + s);
            }
        }
    }
}
