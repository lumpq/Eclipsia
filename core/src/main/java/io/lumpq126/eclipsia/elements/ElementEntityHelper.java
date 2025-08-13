package io.lumpq126.eclipsia.elements;

/**
 * {@link Element} 관련 유틸리티 메서드를 제공하는 클래스.
 * <p>
 * 현재는 속성 이름을 통해 Element 객체를 찾는 기능을 지원한다.
 */
public class ElementEntityHelper {

    /**
     * 게임에서 사용 가능한 모든 속성(Element) 목록.
     * 순서는 {@link Element} 클래스의 선언 순서와 동일하다.
     */
    private static final Element[] ALL_ELEMENTS = {
            Element.NORMAL, Element.FIRE, Element.WATER, Element.EARTH, Element.WIND,
            Element.POISON, Element.LIGHT, Element.DARKNESS, Element.ELECTRIC, Element.ICE,
            Element.METAL, Element.PLANTS, Element.ROT, Element.SHADOW, Element.ANGEL, Element.DEVIL
    };

    /**
     * 주어진 이름에 해당하는 {@link Element} 객체를 반환한다.
     * <p>
     * 대소문자를 구분하지 않는다.
     *
     * @param name 찾을 속성의 이름
     * @return 일치하는 Element 객체, 없으면 {@code null}
     */
    public static Element getElementByName(String name) {
        for (Element e : ALL_ELEMENTS) {
            if (e.getName().equalsIgnoreCase(name)) {
                return e;
            }
        }
        return null;
    }
}
