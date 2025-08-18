package io.lumpq126.eclipsia.core.mechanics.elements;

import io.lumpq126.elementapi.api.elements.Element;

public class Darkness extends Element {
    private static final Darkness INSTANCE = new Darkness();

    private Darkness() {
        super("DARKNESS");
    }

    public static Darkness INSTANCE() {
        return INSTANCE;
    }
}
