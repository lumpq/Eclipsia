package io.lumpq126.eclipsia.utilities.storage;

import io.lumpq126.eclipsia.elements.Element;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * 엘리먼트 간의 상성 관계를 YAML 파일로부터 불러오고 관리하는 클래스입니다.
 * <p>
 * elements.yml 파일을 로드하여 각 엘리먼트의 강점, 약점, 상호 강점 관계 등을 초기화합니다.
 */
public class ElementStorage {

    /**
     * 관리하는 모든 엘리먼트 목록
     */
    private static final Element[] ALL_ELEMENTS = {
            Element.NORMAL, Element.FIRE, Element.WATER, Element.EARTH, Element.WIND,
            Element.POISON, Element.LIGHT, Element.DARKNESS, Element.ELECTRIC, Element.ICE,
            Element.METAL, Element.PLANTS, Element.ROT, Element.SHADOW, Element.ANGEL, Element.DEVIL
    };

    /**
     * 엘리먼트 이름으로 해당 Element 객체를 찾아 반환합니다.
     *
     * @param name 엘리먼트 이름 (대소문자 구분 없음)
     * @return 이름과 일치하는 Element 객체, 없으면 null 반환
     */
    private static Element getElementByName(String name) {
        for (Element e : ALL_ELEMENTS) {
            if (e.getName().equalsIgnoreCase(name)) {
                return e;
            }
        }
        return null;
    }

    /**
     * plugin의 data 폴더 내 elements.yml을 읽어 엘리먼트 관계를 초기화합니다.
     * 파일이 없으면 기본 리소스를 저장 후 로드합니다.
     *
     * @param plugin JavaPlugin 인스턴스
     */
    public static void load(JavaPlugin plugin) {
        File file = new File(plugin.getDataFolder(), "elements.yml");
        if (!file.exists()) {
            plugin.saveResource("elements.yml", false);
        }
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        // 모든 엘리먼트 관계 초기화
        for (Element e : ALL_ELEMENTS) {
            e.clearRelations();
        }

        for (String key : Objects.requireNonNull(config.getConfigurationSection("elements")).getKeys(false)) {
            Element element = getElementByName(key.toUpperCase());
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
                Element target = getElementByName(s.toUpperCase());
                if (target != null) {
                    element.addMutualStrength(target);
                    target.addMutualStrength(element);
                } else {
                    plugin.getLogger().warning("⚠ Unknown mutual_strength element in " + key + ": " + s);
                }
            }
        }

        // 충돌 관계 검증 (mutualStrengths 양방향 상성 제외)
        for (Element e : ALL_ELEMENTS) {
            Set<Element> strengths = e.getStrengths();
            Set<Element> weaknesses = e.getWeaknesses();

            // strengths ∩ weaknesses
            strengths.retainAll(weaknesses);

            // mutualStrengths 양방향인 요소들 제외
            strengths.removeIf(other -> e.getMutualStrengths().contains(other) && other.getMutualStrengths().contains(e));

            if (!strengths.isEmpty()) {
                plugin.getLogger().warning("⚠ Conflict detected in element " + e.getName() + ": in both strengths and weaknesses (excluding mutual strengths) -> " + strengths);
            }
        }

        plugin.getLogger().info("✅ Elements loaded from elements.yml");
    }

    /**
     * 리스트에 담긴 엘리먼트 이름을 실제 Element 객체로 변환하여 consumer에 전달합니다.
     * 만약 알 수 없는 이름이 있으면 경고 로그를 남깁니다.
     *
     * @param list    엘리먼트 이름 리스트
     * @param consumer Element 객체를 받는 Consumer 함수
     * @param plugin  JavaPlugin 인스턴스 (로깅용)
     * @param parent  현재 처리 중인 엘리먼트
     * @param category 현재 카테고리명 (strengths, weaknesses 등)
     */
    private static void loadList(List<String> list, java.util.function.Consumer<Element> consumer, JavaPlugin plugin, Element parent, String category) {
        for (String s : list) {
            Element e = getElementByName(s.toUpperCase());
            if (e != null) {
                consumer.accept(e);
            } else {
                plugin.getLogger().warning("⚠ Unknown element in " + parent.getName() + " -> " + category + ": " + s);
            }
        }
    }
}
