package io.snowyblossom126.eclipsia.elements;

import io.snowyblossom126.elementapi.api.elements.Element;

public class Metal extends Element {
    private static final Metal INSTANCE = new Metal();

    private Metal() {
        super("METAL");
    }

    public static Metal INSTANCE() {
        return INSTANCE;
    }
}
