package io.lumpq126.eclipsia.core.mechanics.elements;

import io.lumpq126.elementapi.api.elements.Element;

public class Plants extends Element {
    private static final Plants INSTANCE = new Plants();

    private Plants() {
        super("PLANTS");
    }

    public static Plants INSTANCE() {
        return INSTANCE;
    }
}
