package io.snowyblossom126.eclipsia.ui.gui;

import io.snowyblossom126.eclipsia.utilities.Mm;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public enum StatType {

    STRENGTH("근력", Material.IRON_SWORD, "<color:#ff6666>", (st, statPoint) -> {
        List<Component> lore = new ArrayList<>();
        lore.add(Mm.mm("<color:#ffdddd>물리 공격력을 증가시킵니다!"));
        lore.add(Component.empty());

        lore.add(Mm.mm("<white>물리공격력증가 <color:#ffbbbb>+ 1.0"));
        lore.add(Component.empty());

        lore.add(Mm.mm("<white>현재 적용중인 능력치 : </white><color:#ff4444>물리공격력 증가 + " + fmt(st * 1.0)));

        return lore;
    }),

    CONSTITUTION("건강", Material.NETHERITE_CHESTPLATE, "<color:#66cc66>", (st, statPoint) -> {
        List<Component> lore = new ArrayList<>();
        lore.add(Mm.mm("<color:#ddffdd>최대 체력을 증가시킵니다!"));
        lore.add(Component.empty());

        lore.add(Mm.mm("<white>최대체력 증가 <color:#bbddbb>+ 1.0%"));
        lore.add(Mm.mm("<white>회복력 증가 <color:#4cb78d>+ 0.1%"));
        lore.add(Component.empty());

        lore.add(Mm.mm("<white>현재 적용중인 능력치 : </white><color:#44aa44>최대체력 증가 + " + fmt(st * 1.0) + "%"));
        lore.add(Mm.mm("<white>현재 적용중인 능력치 : </white><color:#00cc00>회복력 + " + fmt(st * 0.1) + "%"));

        return lore;
    }),

    AGILITY("민첩", Material.FEATHER, "<color:#66ffff>", (st, statPoint) -> {
        List<Component> lore = new ArrayList<>();
        lore.add(Mm.mm("<color:#ddffff>이동 속도와 공격 속도를 증가시킵니다!"));
        lore.add(Component.empty());

        lore.add(Mm.mm("<white>이동속도 증가 <color:#bbdddd>+ 0.2%"));
        if (st >= 2000) lore.add(Mm.mm("<white>공격속도 증가 <color:#88cccc>+ 0.04%"));
        lore.add(Component.empty());

        double moveSpeed = Math.min(st, 2000) * 0.2;
        lore.add(Mm.mm("<white>현재 적용중인 능력치 : </white><color:#44aaaa>이동속도 + " + fmt(moveSpeed) + "%"));
        if (st >= 2000) {
            double atkSpeed = (st - 1999) * 0.04;
            lore.add(Mm.mm("<white>현재 적용중인 능력치 : </white><color:#117777>공격속도 + " + fmt2(atkSpeed) + "%"));
        }

        return lore;
    }),

    DEXTERITY("재주", Material.BOW, "<color:#ffff66>", (st, statPoint) -> {
        List<Component> lore = new ArrayList<>();
        lore.add(Mm.mm("<color:#ffffdd>치명타 확률 및 치명타 피해를 증가시킵니다!"));
        lore.add(Component.empty());

        lore.add(Mm.mm("<white>치명타 확률 증가 <color:#ffffbb>+ 0.1%"));
        if (st >= 1000) lore.add(Mm.mm("<white>치명타 공격력 증가 <color:#dddd88>+ 1.0%"));
        lore.add(Component.empty());

        lore.add(Mm.mm("<white>현재 적용중인 능력치 : </white><color:#aaaa44>치명타 확률 + " + fmt(Math.min(st * 0.1, 100)) + "%"));
        if (st >= 1000) {
            lore.add(Mm.mm("<white>현재 적용중인 능력치 : </white><color:#777711>치명타 피해 + " + fmt((st - 999) * 1.0) + "%"));
        }

        return lore;
    }),

    WISDOM("지혜", Material.ENCHANTED_BOOK, "<color:#cc99ff>", (st, statPoint) -> {
        List<Component> lore = new ArrayList<>();
        lore.add(Mm.mm("<color:#e5d6ff>마나 재생 속도를 증가시킵니다!"));
        lore.add(Component.empty());

        lore.add(Mm.mm("<white>마나 재생 증가 <color:#c9a6ff>+ 0.1"));
        lore.add(Component.empty());

        lore.add(Mm.mm("<white>현재 적용중인 능력치 : </white><color:#a86aff>마나 재생 + " + fmt(st * 0.1)));

        return lore;
    }),

    INTELLIGENCE("지능", Material.WRITABLE_BOOK, "<color:#9999ff>", (st, statPoint) -> {
        List<Component> lore = new ArrayList<>();
        lore.add(Mm.mm("<color:#ccccff>마법 공격력을 증가시킵니다!"));
        lore.add(Component.empty());

        lore.add(Mm.mm("<white>마법 공격력 증가 <color:#9999ff>+ 1.0%"));
        lore.add(Component.empty());

        lore.add(Mm.mm("<white>현재 적용중인 능력치 : </white><color:#6666cc>마법공격력 + " + fmt(st * 1.0) + "%"));

        return lore;
    });

    public final String display;
    public final Material material;
    public final String color;
    public final BiFunction<Integer, Integer, List<Component>> loreFunction;

    StatType(String display, Material material, String color, BiFunction<Integer, Integer, List<Component>> loreFunction) {
        this.display = display;
        this.material = material;
        this.color = color;
        this.loreFunction = loreFunction;
    }

    public static StatType fromName(String name) {
        return switch (name.toLowerCase()) {
            case "str", "strength"      -> STRENGTH;
            case "con", "constitution"  -> CONSTITUTION;
            case "agi", "agility"       -> AGILITY;
            case "dex", "dexterity"     -> DEXTERITY;
            case "wis", "wisdom"        -> WISDOM;
            case "int", "intelligence"  -> INTELLIGENCE;
            default -> null;
        };
    }

    private static String fmt(double value) {
        return String.format("%.1f", value);
    }
    private static String fmt2(double value) {
        return String.format("%.2f", value);
    }
}
