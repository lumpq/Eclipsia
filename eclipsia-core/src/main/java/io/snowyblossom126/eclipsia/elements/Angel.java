package io.snowyblossom126.eclipsia.elements;

import io.snowyblossom126.elementapi.api.elements.Element;

public class Angel extends Element {
    private static final Angel INSTANCE = new Angel();

    private Angel() {
        super("ANGEL");
    }

    public static Angel INSTANCE() {
        return INSTANCE;
    }
}
