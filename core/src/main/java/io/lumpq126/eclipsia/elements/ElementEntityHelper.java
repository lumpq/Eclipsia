package io.lumpq126.eclipsia.elements;

public class ElementEntityHelper {
    public static Element getElementByName(String name) {
        if (name == null) return null;
        String cleanName = name.trim().toUpperCase();
        for (Element e : Element.values()) {
            if (e.getName().equalsIgnoreCase(cleanName)) {
                return e;
            }
        }
        return null;
    }
}
