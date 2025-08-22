package io.snowyblossom126.eclipsia.elements;

import io.snowyblossom126.elementapi.api.elements.Element;

public class Devil extends Element {
    private static final Devil INSTANCE = new Devil();

    private Devil() {
        super("DEVIL");
    }

    public static Devil INSTANCE() {
        return INSTANCE;
    }
}
