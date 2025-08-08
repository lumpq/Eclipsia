package kr.lumpq126.eclipsia.fish.items;

import kr.lumpq126.eclipsia.EclipsiaPlugin;
import kr.lumpq126.eclipsia.utilities.manager.FishCatalogManager;
import kr.lumpq126.eclipsia.utilities.calc.FishGradeCalculator;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FishItems {
    private static final MiniMessage MINI = MiniMessage.miniMessage();

    private static Component mm(String input) {
        return MINI.deserialize(Objects.requireNonNullElse(input, "<red>Unknown"));
    }

    private static List<Component> mm(List<String> input) {
        if (input == null) return new ArrayList<>();
        return input.stream().map(MINI::deserialize).collect(Collectors.toList());
    }

    private static Component replacePlaceholders(Component component, double length, double weight,
                                                 String lengthUnit, String weightUnit, Component grade) {
        return component
                .replaceText(TextReplacementConfig.builder().match("<length>")
                        .replacement(Component.text(String.format("%.1f", length))).build())
                .replaceText(TextReplacementConfig.builder().match("<weight>")
                        .replacement(Component.text(String.format("%.1f", weight))).build())
                .replaceText(TextReplacementConfig.builder().match("<length.unit>")
                        .replacement(Component.text(lengthUnit)).build())
                .replaceText(TextReplacementConfig.builder().match("<weight.unit>")
                        .replacement(Component.text(weightUnit)).build())
                .replaceText(TextReplacementConfig.builder().match("<grade>")
                        .replacement(grade).build());
    }

    private static String formatMonthList(List<Integer> months) {
        return months.stream()
                .sorted()
                .map(m -> m + "월")
                .collect(Collectors.joining(", "));
    }

    public static ItemStack fish(Player p, String id, double length, double weight) {
        var fishConfig = EclipsiaPlugin.getFishConfig();
        var section = fishConfig.getConfigurationSection(id);
        if (section == null) return null;

        Component displayName = mm(section.getString("display.name"));
        List<Component> generalLore = mm(section.getStringList("display.general-lore"));
        List<Component> explainLore = mm(section.getStringList("display.explain-lore"));

        String lengthUnit = section.getString("length-unit", "cm");
        String weightUnit = section.getString("weight-unit", "g");
        double minLength = section.getDouble("min-length", 0.0);
        double maxLength = section.getDouble("max-length", 1000.0);
        double minWeight = section.getDouble("min-weight", 0.0);
        double maxWeight = section.getDouble("max-weight", 1000.0);

        String gradeStr = FishGradeCalculator.getGrade(length, minLength, maxLength, weight, minWeight, maxWeight);
        Component grade = mm(gradeStr);

        List<Component> baseLore = new ArrayList<>(generalLore);
        baseLore.addAll(explainLore);

        List<Component> finalLore = baseLore.stream()
                .map(c -> replacePlaceholders(c, length, weight, lengthUnit, weightUnit, grade))
                .collect(Collectors.toList());

        int customModelData = section.getInt("custom-model-data", 0);

        ItemStack fish = new ItemStack(Material.COD);
        ItemMeta meta = fish.getItemMeta();
        if (meta == null) return null;

        meta.displayName(displayName);
        meta.lore(finalLore);
        meta.setCustomModelData(customModelData);
        fish.setItemMeta(meta);

        p.sendMessage(mm(String.format(
                "<white>길이 <dark_aqua>%.1f %s<white>, 무게 <gold>%.1f %s<white>의 <bold>%s</bold><white>를 낚았다! <bold><gray>(등급: <white>%s<gray>)</bold>",
                length, lengthUnit, weight, weightUnit, section.getString("display.name", "Unknown Fish"), gradeStr)));

        if (!FishCatalogManager.isFishUnlocked(p, id)) {
            FishCatalogManager.addUnlockedFish(p, id);
            p.sendMessage(mm(
                    "<bold><white>도감에 새로운 물고기</white> <bold>" +
                            section.getString("display.name", "Unknown Fish") +
                            " </bold><white>가 등록되었습니다!</white>"
            ));
        }

        return fish;
    }

    public static ItemStack fishBook(String id) {
        var fishConfig = EclipsiaPlugin.getFishConfig();
        var section = fishConfig.getConfigurationSection(id);
        if (section == null) return null;

        Component displayName = mm(section.getString("display.name"));
        String lengthUnit = section.getString("length-unit", "cm");
        String weightUnit = section.getString("weight-unit", "g");
        double minLength = section.getDouble("min-length", 0.0);
        double maxLength = section.getDouble("max-length", 1000.0);
        double minWeight = section.getDouble("min-weight", 0.0);
        double maxWeight = section.getDouble("max-weight", 1000.0);
        int customModelData = section.getInt("custom-model-data", 0);

        List<Component> loreComponents = new ArrayList<>();
        loreComponents.add(mm("<italic:false><yellow>최소 길이 <white>: <dark_aqua>" + minLength + " " + lengthUnit));
        loreComponents.add(mm("<italic:false><yellow>최대 길이 <white>: <dark_aqua>" + maxLength + " " + lengthUnit));
        loreComponents.add(Component.empty());
        loreComponents.add(mm("<italic:false><yellow>최소 무게 <white>: <gold>" + minWeight + " " + weightUnit));
        loreComponents.add(mm("<italic:false><yellow>최대 무게 <white>: <gold>" + maxWeight + " " + weightUnit));
        loreComponents.add(Component.empty());

        var condition = section.getConfigurationSection("capture-condition");
        if (condition != null) {
            if (condition.getBoolean("always-can-capture", false)) {
                loreComponents.add(mm("<italic:false><blue>항상 잡힌다"));
            }

            List<Integer> canCaptureMonths = condition.getIntegerList("can-capture-month");
            if (!canCaptureMonths.isEmpty()) {
                loreComponents.add(mm("<italic:false><green>잡히는 달: <gray>" + formatMonthList(canCaptureMonths)));
            }

            var cantCaptureMonthSection = condition.getConfigurationSection("can't-capture-month");
            if (cantCaptureMonthSection != null && cantCaptureMonthSection.getBoolean("enable", false)) {
                List<Integer> cantCaptureMonths = cantCaptureMonthSection.getIntegerList("month");
                if (!cantCaptureMonths.isEmpty()) {
                    loreComponents.add(mm("<italic:false><red>잡히지 않는 달: <gray>" + formatMonthList(cantCaptureMonths)));
                }
            }

            var largeObjectCaptureSection = condition.getConfigurationSection("large-object-capture");
            if (largeObjectCaptureSection != null && largeObjectCaptureSection.getBoolean("enable", false)) {
                List<Integer> largeMonths = largeObjectCaptureSection.getIntegerList("month");
                if (!largeMonths.isEmpty()) {
                    loreComponents.add(mm("<italic:false><aqua>큰 개체가 잡히는 달: <gray>" + formatMonthList(largeMonths)));
                }
            }

            var smallObjectCaptureSection = condition.getConfigurationSection("small-object-capture");
            if (smallObjectCaptureSection != null && smallObjectCaptureSection.getBoolean("enable", false)) {
                List<Integer> smallMonths = smallObjectCaptureSection.getIntegerList("month");
                if (!smallMonths.isEmpty()) {
                    loreComponents.add(mm("<italic:false><light_purple>작은 개체가 잡히는 달: <gray>" + formatMonthList(smallMonths)));
                }
            }
        }

        loreComponents.add(Component.empty());
        loreComponents.addAll(mm(section.getStringList("display.explain-lore")));
        loreComponents.add(Component.empty());
        loreComponents.add(mm("<italic:false><white>" + id));

        ItemStack fish = new ItemStack(Material.COD);
        ItemMeta meta = fish.getItemMeta();
        if (meta == null) return null;

        meta.displayName(displayName);
        meta.lore(loreComponents);
        meta.setCustomModelData(customModelData);
        fish.setItemMeta(meta);

        return fish;
    }
}
