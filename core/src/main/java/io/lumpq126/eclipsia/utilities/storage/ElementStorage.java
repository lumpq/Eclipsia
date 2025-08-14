package io.lumpq126.eclipsia.utilities.storage;

import io.lumpq126.eclipsia.elements.Element;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * elements.yml에서 Element 관계를 불러와
 * Element의 relationMatrix에 직접 세팅하는 유틸 클래스.
 */
public class ElementStorage {

    private static final Element[] ALL_ELEMENTS = {
            Element.NORMAL, Element.FIRE, Element.WATER, Element.EARTH, Element.WIND,
            Element.POISON, Element.LIGHT, Element.DARKNESS, Element.ELECTRIC, Element.ICE,
            Element.METAL, Element.PLANTS, Element.ROT, Element.SHADOW, Element.ANGEL, Element.DEVIL
    };

    private static Element getElementByName(String name) {
        for (Element e : ALL_ELEMENTS) {
            if (e.name().equalsIgnoreCase(name)) {
                return e;
            }
        }
        return null;
    }

    /**
     * elements.yml을 로드하여 Element 관계 매트릭스 초기화
     */
    public static void load(JavaPlugin plugin) {
        Logger logger = plugin.getLogger();
        File file = new File(plugin.getDataFolder(), "elements.yml");

        if (!file.exists()) {
            plugin.saveResource("elements.yml", false);
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        // 모든 관계 초기화 (한 번만 호출)
        Element.clearRelations();

        // elements.yml 읽기
        for (String key : Objects.requireNonNull(config.getConfigurationSection("elements")).getKeys(false)) {
            Element element = getElementByName(key);
            if (element == null) {
                logger.warning("⚠ Unknown element in config: " + key);
                continue;
            }

            setRelations(config.getStringList("elements." + key + ".strengths"), element, Element.STRENGTH, logger, key);
            setRelations(config.getStringList("elements." + key + ".ultimate_strengths"), element, Element.ULTIMATE_STRENGTH, logger, key);
            setRelations(config.getStringList("elements." + key + ".weaknesses"), element, Element.WEAKNESS, logger, key);
            setRelations(config.getStringList("elements." + key + ".ultimate_weaknesses"), element, Element.ULTIMATE_WEAKNESS, logger, key);
            setRelations(config.getStringList("elements." + key + ".generals"), element, Element.GENERAL, logger, key);

            // mutual_strengths: 양방향 관계
            for (String s : config.getStringList("elements." + key + ".mutual_strengths")) {
                Element target = getElementByName(s);
                if (target != null) {
                    element.setRelation(target, Element.MUTUAL);
                    target.setRelation(element, Element.MUTUAL);
                } else {
                    logger.warning("⚠ Unknown mutual_strength element in " + key + ": " + s);
                }
            }
        }

        logger.info("✅ Elements loaded from elements.yml");
    }

    /**
     * 지정된 목록에 포함된 요소들과 관계를 설정
     */
    private static void setRelations(List<String> list, Element element, int relationValue, Logger logger, String parentKey) {
        for (String s : list) {
            Element target = getElementByName(s);
            if (target != null) {
                element.setRelation(target, relationValue);
            } else {
                logger.warning("⚠ Unknown element in " + parentKey + ": " + s);
            }
        }
    }
}
