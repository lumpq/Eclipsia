package io.snowyblossom126.eclipsia.stats;

import java.util.Locale;

/**
 * 플레이어 또는 엔티티의 스탯 종류를 정의하는 열거형(Enum)입니다.
 * <p>
 * 각 스탯은 캐릭터 능력치에 직접적으로 영향을 미칩니다.
 */
public enum Stat {
    STRENGTH,     // 힘
    CONSTITUTION, // 체력
    AGILITY,      // 민첩
    DEXTERITY,    // 손재주
    INTELLIGENCE, // 지능
    WISDOM;       // 지혜

    /** 초기 스탯 값 */
    public static final int INITIAL_STAT = 5;

    /** 초기 포인트 값 */
    public static final int INITIAL_POINT = 5;

    /**
     * 이름(String)으로 Stat을 검색합니다.
     *
     * @param name 스탯 이름 (대소문자 구분 없음)
     * @return 해당 이름의 Stat 또는 잘못된 이름이면 null
     */
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
