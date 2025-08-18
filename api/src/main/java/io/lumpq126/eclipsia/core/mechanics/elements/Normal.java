package io.lumpq126.eclipsia.core.mechanics.elements;

import io.lumpq126.elementapi.api.elements.Element;

public class Normal extends Element {
    private static final Normal INSTANCE = new Normal();

    private Normal() {
        super("FIRE");
    }

    public static Normal INSTANCE() {
        return INSTANCE;
    }
}
