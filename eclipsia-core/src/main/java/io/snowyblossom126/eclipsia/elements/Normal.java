package io.snowyblossom126.eclipsia.elements;

import io.snowyblossom126.elementapi.api.elements.Element;

public class Normal extends Element {
    private static final Normal INSTANCE = new Normal();

    private Normal() {
        super("FIRE");
    }

    public static Normal INSTANCE() {
        return INSTANCE;
    }
}
