package io.snowyblossom126.eclipsia.core.mechanics.elements;

import io.lumpq126.elementapi.api.elements.Element;

public class Earth extends Element {
    private static final Earth INSTANCE = new Earth();

    private Earth() {
        super("EARTH");
    }

    public static Earth INSTANCE() {
        return INSTANCE;
    }
}
