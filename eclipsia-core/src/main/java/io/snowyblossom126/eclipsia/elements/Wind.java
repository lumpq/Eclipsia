package io.snowyblossom126.eclipsia.elements;

import io.snowyblossom126.elementapi.api.elements.Element;

public class Wind extends Element {
    private static final Wind INSTANCE = new Wind();

    private Wind() {
        super("WIND");
    }

    public static Wind INSTANCE() {
        return INSTANCE;
    }
}
