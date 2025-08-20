package io.snowyblossom126.eclipsia.core.mechanics.elements;

import io.lumpq126.elementapi.api.elements.Element;

public class Shadow extends Element {
    private static final Shadow INSTANCE = new Shadow();

    private Shadow() {
        super("SHADOW");
    }

    public static Shadow INSTANCE() {
        return INSTANCE;
    }
}
