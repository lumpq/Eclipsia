package io.lumpq126.eclipsia.utilities.storage;

import io.lumpq126.eclipsia.elements.Element;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;

/**
 * ElementStorage: elements.yml 에서 Element 관계를 불러와
 * Element의 relationMatrix에 직접 세팅하는 구조
 */
public class ElementStorage {

    private static final Element[] ALL_ELEMENTS = {
            Element.NORMAL, Element.FIRE, Element.WATER, Element.EARTH, Element.WIND,
            Element.POISON, Element.LIGHT, Element.DARKNESS, Element.ELECTRIC, Element.ICE,
            Element.METAL, Element.PLANTS, Element.ROT, Element.SHADOW, Element.ANGEL, Element.DEVIL
    };

    private static Element getElementByName(String name) {
        for (Element e : ALL_ELEMENTS) {
            if (e.getName().equalsIgnoreCase(name)) return e;
        }
        return null;
    }

    public static void load(JavaPlugin plugin) {
        File file = new File(plugin.getDataFolder(), "elements.yml");
        if (!file.exists()) plugin.saveResource("elements.yml", false);
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        // 모든 관계 초기화
        for (Element e : ALL_ELEMENTS) Element.clearRelations();

        // elements.yml 로드
        for (String key : Objects.requireNonNull(config.getConfigurationSection("elements")).getKeys(false)) {
            Element element = getElementByName(key);
            if (element == null) {
                plugin.getLogger().warning("⚠ Unknown element in config: " + key);
                continue;
            }

            setRelations(config.getStringList("elements." + key + ".strengths"), element, Element.STRENGTH, plugin, key);
            setRelations(config.getStringList("elements." + key + ".ultimate_strengths"), element, Element.ULTIMATE_STRENGTH, plugin, key);
            setRelations(config.getStringList("elements." + key + ".weaknesses"), element, Element.WEAKNESS, plugin, key);
            setRelations(config.getStringList("elements." + key + ".ultimate_weaknesses"), element, Element.ULTIMATE_WEAKNESS, plugin, key);
            setRelations(config.getStringList("elements." + key + ".generals"), element, Element.GENERAL, plugin, key);

            // mutual_strengths: 양방향
            for (String s : config.getStringList("elements." + key + ".mutual_strengths")) {
                Element target = getElementByName(s);
                if (target != null) {
                    element.setRelation(target, Element.MUTUAL);
                    target.setRelation(element, Element.MUTUAL);
                } else {
                    plugin.getLogger().warning("⚠ Unknown mutual_strength element in " + key + ": " + s);
                }
            }
        }

        plugin.getLogger().info("✅ Elements loaded from elements.yml");
    }

    private static void setRelations(java.util.List<String> list, Element element, int relationValue, JavaPlugin plugin, String parentKey) {
        for (String s : list) {
            Element target = getElementByName(s);
            if (target != null) {
                element.setRelation(target, relationValue);
            } else {
                plugin.getLogger().warning("⚠ Unknown element in " + parentKey + ": " + s);
            }
        }
    }
}
