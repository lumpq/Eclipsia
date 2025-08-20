package io.snowyblossom126.eclipsia.nms;

import org.bukkit.Bukkit;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Set;

public final class NMSHandlerFactory {

    /**
     * 버전 매핑 enum
     */
    private enum NmsVersion {
        V1_20_R1(Set.of("1.20-R0.1-SNAPSHOT", "1.20.1-R0.1-SNAPSHOT")),
        V1_20_R2(Set.of("1.20.2-R0.1-SNAPSHOT")),
        V1_20_R3(Set.of("1.20.3-R0.1-SNAPSHOT", "1.20.4-R0.1-SNAPSHOT")),
        V1_20_R4(Set.of("1.20.5-R0.1-SNAPSHOT", "1.20.6-R0.1-SNAPSHOT")),
        V1_21_R1(Set.of("1.21-R0.1-SNAPSHOT", "1.21.1-R0.1-SNAPSHOT")),
        V1_21_R2(Set.of("1.21.2-R0.1-SNAPSHOT", "1.21.3-R0.1-SNAPSHOT")),
        V1_21_R3(Set.of("1.21.4-R0.1-SNAPSHOT")),
        V1_21_R4(Set.of("1.21.5-R0.1-SNAPSHOT")),
        V1_21_R5(Set.of("1.21.6-R0.1-SNAPSHOT", "1.21.7-R0.1-SNAPSHOT", "1.21.8-R0.1-SNAPSHOT"));

        private final Set<String> bukkitVersions;

        NmsVersion(Set<String> bukkitVersions) {
            this.bukkitVersions = bukkitVersions;
        }

        public static NmsVersion fromBukkitVersion(String version) {
            return Arrays.stream(values())
                    .filter(v -> v.bukkitVersions.contains(version))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("지원하지 않는 버전: " + version));
        }
    }

    private NMSHandlerFactory() {} // 인스턴스화 방지

    /**
     * 현재 서버 버전에 맞는 NMSHandler 인스턴스를 로드
     */
    public static NMSHandler loadNMS() {
        String bukkitVersion = Bukkit.getBukkitVersion();
        NmsVersion nmsVersion = NmsVersion.fromBukkitVersion(bukkitVersion);

        try {
            // 패키지 이름: V → v로만 변환, 나머지는 그대로
            String versionName = nmsVersion.name();
            String packageName = "v" + versionName.substring(1);
            String className = "io.lumpq126.eclipsia.nms." + packageName + ".NMSHandler";

            Class<?> clazz = Class.forName(className);
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return (NMSHandler) constructor.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("NMS 핸들러 로드 실패: " + nmsVersion.name(), e);
        }
    }

    /**
     * 현재 서버의 NMS 버전명(v1_xx_Rx) 반환
     */
    public static String getNMSVersion() {
        String bukkitVersion = Bukkit.getBukkitVersion();
        return NmsVersion.fromBukkitVersion(bukkitVersion).name();
    }
}
