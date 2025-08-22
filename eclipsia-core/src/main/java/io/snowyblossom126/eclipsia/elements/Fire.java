package io.snowyblossom126.eclipsia.elements;

import io.snowyblossom126.elementapi.api.elements.Element;

public class Fire extends Element {
    private static final Fire INSTANCE = new Fire();

    private Fire() {
        super("FIRE");
    }

    public static Fire INSTANCE() {
        return INSTANCE;
    }
}
