package io.lumpq126.eclipsia.core.mechanics.elements;

import io.lumpq126.elementapi.api.elements.Element;

public class Rot extends Element {
    private static final Rot INSTANCE = new Rot();

    private Rot() {
        super("ROT");
    }

    public static Rot INSTANCE() {
        return INSTANCE;
    }
}
