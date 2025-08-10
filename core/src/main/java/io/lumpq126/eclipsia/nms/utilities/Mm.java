package io.lumpq126.eclipsia.nms.utilities;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Mm {

    public static Component mm(String input) {
        return MiniMessage.miniMessage().deserialize(input);
    }

    public static List<Component> mm(List<String> input) {
        if (input == null) {
            return new ArrayList<>();
        }
        return input.stream().map(MiniMessage.miniMessage()::deserialize).collect(Collectors.toList());
    }
}
