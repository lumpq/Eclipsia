package io.snowyblossom126.eclipsia.elements;

import io.snowyblossom126.elementapi.api.elements.Element;

public class Rot extends Element {
    private static final Rot INSTANCE = new Rot();

    private Rot() {
        super("ROT");
    }

    public static Rot INSTANCE() {
        return INSTANCE;
    }
}
