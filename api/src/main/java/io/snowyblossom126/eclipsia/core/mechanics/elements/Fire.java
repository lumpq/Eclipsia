package io.snowyblossom126.eclipsia.core.mechanics.elements;

import io.lumpq126.elementapi.api.elements.Element;

public class Fire extends Element {
    private static final Fire INSTANCE = new Fire();

    private Fire() {
        super("FIRE");
    }

    public static Fire INSTANCE() {
        return INSTANCE;
    }
}
