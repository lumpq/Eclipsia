package io.snowyblossom126.eclipsia.core.mechanics.elements;

import io.lumpq126.elementapi.api.elements.Element;

public class Light extends Element {
    private static final Light INSTANCE = new Light();

    private Light() {
        super("LIGHT");
    }

    public static Light INSTANCE() {
        return INSTANCE;
    }
}
