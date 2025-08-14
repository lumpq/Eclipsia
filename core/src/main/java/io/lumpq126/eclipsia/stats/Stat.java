package io.lumpq126.eclipsia.stats;

import java.util.Locale;

/**
 * 플레이어/엔티티 스탯 종류
 */
public enum Stat {
    STRENGTH,     // 힘
    CONSTITUTION, // 체력
    AGILITY,      // 민첩
    DEXTERITY,    // 손재주
    INTELLIGENCE, // 지능
    WISDOM;       // 지혜

    public static final int INITIAL_STAT = 5;
    public static final int INITIAL_POINT = 5;

    /** 이름 → Stat 변환 */
    public static Stat fromName(String name) {
        if (name == null) return null;
        try {
            return Stat.valueOf(name.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            System.out.println("[Eclipsia] 잘못된 Stat 이름: " + name);
            return null;
        }
    }
}
