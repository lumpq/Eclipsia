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
 * {@code ElementStorage} 클래스는 elements.yml 파일에서
 * {@link Element} 간의 관계를 불러와
 * 각 Element의 relationMatrix에 세팅하는 유틸리티 클래스입니다.
 * <p>
 * strengths, weaknesses, ultimate strengths/weaknesses 등 다양한 관계를 지원하며,
 * mutual_strengths는 양방향 관계로 자동 설정됩니다.
 */
public class ElementStorage {

    /** 모든 Element를 캐싱 */
    private static final Element[] ALL_ELEMENTS = Element.values();

    /**
     * 이름(String)으로 Element를 검색합니다.
     *
     * @param name 검색할 Element 이름
     * @return 해당 이름의 Element 또는 존재하지 않으면 null
     */
    private static Element getElementByName(String name) {
        for (Element e : ALL_ELEMENTS) {
            if (e.name().equalsIgnoreCase(name)) {
                return e;
            }
        }
        return null;
    }

    /**
     * elements.yml을 로드하여 Element 관계 매트릭스를 초기화합니다.
     *
     * @param plugin 플러그인 인스턴스 (로깅, 파일 경로, 리소스 저장용)
     */
    public static void load(JavaPlugin plugin) {
        Logger logger = plugin.getLogger();
        File file = new File(plugin.getDataFolder(), "elements.yml");

        if (!file.exists()) {
            plugin.saveResource("elements.yml", false);
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        // 모든 관계 초기화
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
                    Element.setRelation(element, target, Element.MUTUAL);
                    // MUTUAL은 Element.setRelation에서 자동으로 양방향 설정됨
                } else {
                    logger.warning("⚠ Unknown mutual_strength element in " + key + ": " + s);
                }
            }
        }

        logger.info("✅ Elements loaded from elements.yml");
    }

    /**
     * 지정된 목록(List)에 포함된 요소들과 관계를 설정합니다.
     *
     * @param list      관계를 설정할 대상 Element 이름 리스트
     * @param from      기준 Element
     * @param relationValue 관계 종류 (Element.STRENGTH, WEAKNESS 등)
     * @param logger    로깅용 Logger
     * @param parentKey config 상 부모 키 이름 (로그 메시지용)
     */
    private static void setRelations(List<String> list, Element from, int relationValue, Logger logger, String parentKey) {
        for (String s : list) {
            Element target = getElementByName(s);
            if (target != null) {
                Element.setRelation(from, target, relationValue);
            } else {
                logger.warning("⚠ Unknown element in " + parentKey + ": " + s);
            }
        }
    }
}
