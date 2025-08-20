package io.snowyblossom126.eclipsia.core.mechanics.elements;

import io.lumpq126.elementapi.api.elements.Element;

public class Electric extends Element {
    private static final Electric INSTANCE = new Electric();

    private Electric() {
        super("ELECTRIC");
    }

    public static Electric INSTANCE() {
        return INSTANCE;
    }
}
