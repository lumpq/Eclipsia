package io.lumpq126.eclipsia.utilities.storage;

import io.lumpq126.eclipsia.elements.Element;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;

public class ElementStorage {

    private static Element getElementByName(String name) {
        if (name == null) return null;
        String cleanName = name.trim().toUpperCase();
        for (Element e : Element.values()) {
            if (e.getName().equalsIgnoreCase(cleanName)) {
                return e;
            }
        }
        return null;
    }

    public static void load(JavaPlugin plugin) {
        File file = new File(plugin.getDataFolder(), "elements.yml");
        if (!file.exists()) {
            plugin.saveResource("elements.yml", false);
        }
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        // 모든 엘리먼트 관계 초기화
        for (Element e : Element.values()) {
            e.clearRelations();
        }

        for (String key : Objects.requireNonNull(config.getConfigurationSection("elements")).getKeys(false)) {
            Element element = getElementByName(key);
            if (element == null) {
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
                Element target = getElementByName(s);
                if (target != null) {
                    element.addMutualStrength(target);
                    target.addMutualStrength(element);
                } else {
                    plugin.getLogger().warning("⚠ Unknown mutual_strength element in " + key + ": " + s);
                }
            }
        }

        // 충돌 관계 검증
        for (Element e : Element.values()) {
            Set<Element> common = new HashSet<>(e.getStrengths());
            common.retainAll(e.getWeaknesses());

            // mutualStrengths 양방향 요소 제외
            common.removeIf(other -> e.getMutualStrengths().contains(other) && other.getMutualStrengths().contains(e));

            if (!common.isEmpty()) {
                plugin.getLogger().warning("⚠ Conflict detected in element " + e.getName()
                        + ": in both strengths and weaknesses (excluding mutual strengths) -> " + common);
            }
        }

        plugin.getLogger().info("✅ Elements loaded from elements.yml");
    }

    private static void loadList(List<String> list, java.util.function.Consumer<Element> consumer,
                                 JavaPlugin plugin, Element parent, String category) {
        for (String s : list) {
            Element e = getElementByName(s);
            if (e != null) {
                consumer.accept(e);
            } else {
                plugin.getLogger().warning("⚠ Unknown element in " + parent.getName() + " -> " + category + ": " + s);
            }
        }
    }
}
