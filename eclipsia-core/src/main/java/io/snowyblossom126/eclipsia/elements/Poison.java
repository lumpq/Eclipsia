package io.snowyblossom126.eclipsia.elements;

import io.snowyblossom126.elementapi.api.elements.Element;

public class Poison extends Element {
    private static final Poison INSTANCE = new Poison();

    private Poison() {
        super("POISON");
    }

    public static Poison INSTANCE() {
        return INSTANCE;
    }
}
