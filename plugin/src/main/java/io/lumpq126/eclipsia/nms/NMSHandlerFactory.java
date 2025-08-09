package io.lumpq126.eclipsia.nms;

import org.bukkit.Bukkit;

public class NMSHandlerFactory {

    public static NMSHandler loadNMS() {
        String version = Bukkit.getBukkitVersion();

        return switch (version) {
            case "1.19-R0.1-SNAPSHOT", "1.19.1-R0.1-SNAPSHOT", "1.19.2-R0.1-SNAPSHOT" -> new io.lumpq126.eclipsia.nms.v1_19_R1.NMSHandler_v1_19_R1();
            case "1.19.3-R0.1-SNAPSHOT" -> new io.lumpq126.eclipsia.nms.v1_19_R2.NMSHandler_v1_19_R2();
            case "1.19.4-R0.1-SNAPSHOT" -> new io.lumpq126.eclipsia.nms.v1_19_R3.NMSHandler_v1_19_R3();
            case "1.20-R0.1-SNAPSHOT", "1.20.1-R0.1-SNAPSHOT" -> new io.lumpq126.eclipsia.nms.v1_20_R1.NMSHandler_v1_20_R1();
            case "1.20.2-R0.1-SNAPSHOT" -> new io.lumpq126.eclipsia.nms.v1_20_R2.NMSHandler_v1_20_R2();
            case "1.20.3-R0.1-SNAPSHOT", "1.20.4-R0.1-SNAPSHOT" -> new io.lumpq126.eclipsia.nms.v1_20_R3.NMSHandler_v1_20_R3();
            case "1.20.5-R0.1-SNAPSHOT", "1.20.6-R0.1-SNAPSHOT" -> new io.lumpq126.eclipsia.nms.v1_20_R4.NMSHandler_v1_20_R4();
            case "1.21-R0.1-SNAPSHOT", "1.21.1-R0.1-SNAPSHOT" -> new io.lumpq126.eclipsia.nms.v1_21_R1.NMSHandler_v1_21_R1();
            case "1.21.2-R0.1-SNAPSHOT", "1.21.3-R0.1-SNAPSHOT" -> new io.lumpq126.eclipsia.nms.v1_21_R2.NMSHandler_v1_21_R2();
            case "1.21.4-R0.1-SNAPSHOT" -> new io.lumpq126.eclipsia.nms.v1_21_R3.NMSHandler_v1_21_R3();
            case "1.21.5-R0.1-SNAPSHOT" -> new io.lumpq126.eclipsia.nms.v1_21_R4.NMSHandler_v1_21_R4();
            case "1.21.6-R0.1-SNAPSHOT", "1.21.7-R0.1-SNAPSHOT", "1.21.8-R0.1-SNAPSHOT" -> new io.lumpq126.eclipsia.nms.v1_21_R5.NMSHandler_v1_21_R5();
            default -> throw new IllegalStateException("지원하지 않는 버전: " + version);
        };
    }

    public static String getNMSVersion() {
        String version = Bukkit.getBukkitVersion();

        switch (version) {
            case "1.19-R0.1-SNAPSHOT", "1.19.1-R0.1-SNAPSHOT", "1.19.2-R0.1-SNAPSHOT" -> { return "v1_19_R1"; }
            case "1.19.3-R0.1-SNAPSHOT" -> { return "v1_19_R2"; }
            case "1.19.4-R0.1-SNAPSHOT" -> { return "v1_19_R3"; }
            case "1.20-R0.1-SNAPSHOT", "1.20.1-R0.1-SNAPSHOT" -> { return "v1_20_R1"; }
            case "1.20.2-R0.1-SNAPSHOT" -> { return "v1_20_R2"; }
            case "1.20.3-R0.1-SNAPSHOT", "1.20.4-R0.1-SNAPSHOT" -> { return "v1_20_R3"; }
            case "1.20.5-R0.1-SNAPSHOT", "1.20.6-R0.1-SNAPSHOT" -> { return "v1_20_R4"; }
            case "1.21-R0.1-SNAPSHOT", "1.21.1-R0.1-SNAPSHOT" -> { return "v1_21_R1"; }
            case "1.21.2-R0.1-SNAPSHOT", "1.21.3-R0.1-SNAPSHOT" -> { return "v1_21_R2"; }
            case "1.21.4-R0.1-SNAPSHOT" -> { return "v1_21_R3"; }
            case "1.21.5-R0.1-SNAPSHOT" -> { return "v1_21_R4"; }
            case "1.21.6-R0.1-SNAPSHOT", "1.21.7-R0.1-SNAPSHOT", "1.21.8-R0.1-SNAPSHOT" -> { return "v1_21_R5"; }
            default -> { return "지원하지 않는 버전"; }
        }
    }
}
