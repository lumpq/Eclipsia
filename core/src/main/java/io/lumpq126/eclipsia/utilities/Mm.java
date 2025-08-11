package io.lumpq126.eclipsia.utilities;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Mm {

    public static Component mm(String input) {
        return MiniMessage.miniMessage().deserialize(input);
    }

    public static Component mm(String input, boolean bold, boolean italic) {
        Component comp = MiniMessage.miniMessage().deserialize(input);
        if (bold) comp = comp.decoration(TextDecoration.BOLD, true);
        if (italic) comp = comp.decoration(TextDecoration.ITALIC, true);
        return comp;
    }

    public static List<Component> mm(List<String> input) {
        if (input == null) {
            return new ArrayList<>();
        }
        return input.stream().map(MiniMessage.miniMessage()::deserialize).collect(Collectors.toList());
    }

    public static List<Component> mm(List<String> input, boolean bold, boolean italic) {
        if (input == null) {
            return new ArrayList<>();
        }
        return input.stream()
                .map(s -> {
                    Component comp = MiniMessage.miniMessage().deserialize(s);
                    if (bold) comp = comp.decoration(TextDecoration.BOLD, true);
                    if (italic) comp = comp.decoration(TextDecoration.ITALIC, true);
                    return comp;
                })
                .collect(Collectors.toList());
    }
}
