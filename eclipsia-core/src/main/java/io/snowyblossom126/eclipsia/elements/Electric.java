package io.snowyblossom126.eclipsia.elements;

import io.snowyblossom126.elementapi.api.elements.Element;

public class Electric extends Element {
    private static final Electric INSTANCE = new Electric();

    private Electric() {
        super("ELECTRIC");
    }

    public static Electric INSTANCE() {
        return INSTANCE;
    }
}
