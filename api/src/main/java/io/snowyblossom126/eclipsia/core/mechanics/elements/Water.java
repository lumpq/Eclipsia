package io.snowyblossom126.eclipsia.core.mechanics.elements;

import io.lumpq126.elementapi.api.elements.Element;

public class Water extends Element {
    private static final Water INSTANCE = new Water();

    private Water() {
        super("WATER");
    }

    public static Water INSTANCE() {
        return INSTANCE;
    }
}
