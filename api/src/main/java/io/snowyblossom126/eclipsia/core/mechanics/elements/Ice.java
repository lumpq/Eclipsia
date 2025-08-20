package io.snowyblossom126.eclipsia.core.mechanics.elements;

import io.lumpq126.elementapi.api.elements.Element;

public class Ice extends Element {
    private static final Ice INSTANCE = new Ice();

    private Ice() {
        super("ICE");
    }

    public static Ice INSTANCE() {
        return INSTANCE;
    }
}
