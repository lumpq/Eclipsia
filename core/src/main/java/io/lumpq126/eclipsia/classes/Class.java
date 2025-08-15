package io.lumpq126.eclipsia.classes;

import java.util.*;

/**
 * Class
 * <p>
 * 게임 내 직업(Class)과 전직 관계를 정의하는 열거형 클래스.
 * 각 직업은 계열별로 나뉘며, 1차 전직부터 최종 전직까지의 관계를 정의할 수 있습니다.
 * 전직 관계는 {@link #setAdvancement(Class, Class, int)}를 통해 설정하며,
 * 전직 없는 클래스는 {@link #setMaxAdvancement(Class, int)}를 통해 최대 전직 단계를 설정할 수 있습니다.
 * </p>
 *
 * <p>전직 단계(stage)는 1차 전직부터 시작하며, 전직 가능 여부와 단계 조회 기능을 제공합니다.</p>
 */
public enum Class {

    // -----------------------------
    // 기본 클래스
    // -----------------------------
    NOVICE,                // 초심자 (Novice)

    // -----------------------------
    // ⚔ 검사 계열
    // -----------------------------
    GLADIATOR,             // 검투사 (Gladiator)
    KNIGHT,                // 기사 (Knight)
    HOLY_KNIGHT,           // 성기사 (Holy Knight)
    BERSERKER,             // 광전사 (Berserker)

    // -----------------------------
    // ⚔+🔮 하이브리드 계열
    // -----------------------------
    SPELLBLADE,            // 마검사 (Spellblade)

    // -----------------------------
    // 🗡 도둑 계열
    // -----------------------------
    ROGUE,                 // 도적 (Rogue)

    // -----------------------------
    // 🗡 살수 계열
    // -----------------------------
    APPRENTICE_SLAYER,     // 견습살수 (살수 - 1차 전직)
    THIRD_RATE_SLAYER,     // 삼류살수 (살수 - 2차 전직)
    SECOND_RATE_SLAYER,    // 이류살수 (살수 - 3차 전직)
    FIRST_RATE_SLAYER,     // 일류살수 (살수 - 4차 전직)
    ELITE_SLAYER,          // 특급살수 (살수 - 5차 전직)
    MASTERLESS_SLAYER,     // 무급살수 (살수 - 6차 전직)
    CRIMSON_SHADOW,        // 적영 (살수 - 7차 전직)
    DARK_SHADOW,           // 암영 (살수 - 8차 전직)
    VOID_SHADOW,           // 무영 (살수 - 최종 전직)

    // -----------------------------
    // 🥋 무인 계열
    // -----------------------------
    THIRD_RATE_MARTIAL,    // 삼류무인 (무인 - 1차 전직)
    SECOND_RATE_MARTIAL,   // 이류무인 (무인 - 2차 전직)
    FIRST_RATE_MARTIAL,    // 일류무인 (무인 - 3차 전직)
    PEAK_MARTIAL,          // 절정무인 (무인 - 4차 전직)
    TRANSCENDENT_MARTIAL,  // 초절정 (무인 - 5차 전직)
    GRANDMASTER_MARTIAL,   // 무극 (무인 - 최종 전직)

    // -----------------------------
    // 🔮 마도 계열
    // -----------------------------
    APPRENTICE_WARLOCK,    // 마졸 (마도 - 1차 전직)
    DEMONIC_ARCHER,        // 마궁 (마도 - 2차 전직)
    WICKED_BANDIT,         // 흉적 (마도 - 3차 전직)
    DEMON_STALKER,         // 살귀 (마도 - 4차 전직)
    EVIL_WRAITH,           // 악귀 (마도 - 5차 전직)
    DEATH_REAPER,          // 살성 (마도 - 6차 전직)
    ARCANE_MASTER,         // 마선 (마도 - 7차 전직)
    DEMON_LORD,            // 마존 (마도 - 8차 전직)
    CELESTIAL_ARCHMAGE,    // 천마선 (마도 - 9차 전직)
    HEAVENLY_DEMON,        // 천마 (마도 - 최종 전직)

    // -----------------------------
    // 🏹 궁수 계열
    // -----------------------------
    ARCHER,                // 궁사 (궁수 - 1차 전직)
    MASTER_ARCHER,         // 명궁 (궁수 - 2차 전직)
    ELITE_MARKSMAN,        // 고궁 (궁수 - 3차 전직)
    DIVINE_ARCHER,         // 신궁 (궁수 - 4차 전직)
    SAGE_ARCHER,           // 현궁 (궁수 - 5차 전직)
    LEGENDARY_ARCHER,      // 역궁 (궁수 - 최종 전직)

    // -----------------------------
    // 🐾 사냥꾼 계열
    // -----------------------------
    NOVICE_HUNTER,         // 견습 사냥꾼 (사냥꾼 - 1차 전직)
    SKILLED_HUNTER,        // 능숙한 사냥꾼 (사냥꾼 - 2차 전직)
    MASTER_HUNTER,         // 숙달된 사냥꾼 (사냥꾼 - 3차 전직)
    SLAYER_HUNTER,         // 학살자 (사냥꾼 - 4차 전직)
    DIVINE_SLAYER,         // 신살자 (사냥꾼 - 최종 전직)

    // -----------------------------
    // 📜 마법사 계열
    // -----------------------------
    APPRENTICE_SORCERER,   // 견습마법사 (마법사 - 1차 전직)
    SORCERER,              // 마법사 (마법사 - 최종 전직)

    // -----------------------------
    // 📜 마도사 계열
    // -----------------------------
    MAGE,                  // 마도사 (마도사 - 1차 전직)
    ARCHMAGE,              // 대마도사 (마도사 - 2차 전직)
    SAGE,                  // 현자 (마도사 - 최종 전직)

    // -----------------------------
    // ✝ 성직자 계열
    // -----------------------------
    APPRENTICE_CLERIC,     // 견습 성직자 (성직자 - 1차 전직)
    CLERIC,                // 성직자 (성직자 - 2차 전직)
    BISHOP,                // 주교 (성직자 - 3차 전직)
    APOSTLE,               // 사도 (성직자 - 4차 전직)
    PROPHET,               // 교주 (성직자 - 5차 전직)
    SAVIOR,                // 구세주 (성직자 - 6차 전직)
    DEMIGOD,               // 반신 (성직자 - 최종 전직)

    // -----------------------------
    // 🔮 마술사 계열
    // -----------------------------
    CURSE_WEAVER,          // 저주술사 (Curse Weaver)
    ELEMENTALIST,          // 정령사 (Elementalist)
    ELEMENTAL_SUMMONER,    // 정령 소환사 (Elemental Summoner)
    ARCANE_DESTROYER,      // 파괴술사 (Arcane Destroyer)
    ILLUSIONIST,           // 둔갑술사 (Illusionist)
    GRAND_ENCHANTER,       // 강마술사 (Grand Enchanter)

    // -----------------------------
    // 🩺 의술사 계열
    // -----------------------------
    APPRENTICE_HEALER,     // 견습 의술사 (의술사 - 1차 전직)
    HEALER,                // 의술사 (의술사 - 2차 전직)
    MASTER_HEALER,         // 숙달된 의술사 (의술사 - 최종 전직)

    // -----------------------------
    // ⚗ 의약 제조사 계열
    // -----------------------------
    APPRENTICE_PHARMACIST, // 견습 의약사 (의약 제조사 - 1차 전직)
    PHARMACIST,            // 의약사 (의약 제조사 - 2차 전직)
    MASTER_PHARMACIST,     // 숙달된 의약사 (의약 제조사 - 3차 전직)
    HERBALIST,             // 약선 (의약 제조사 - 4차 전직)
    DIVINE_HERBALIST,      // 약신 (의약 제조사 - 최종 전직)

    // -----------------------------
    // 👑 왕 계열
    // -----------------------------
    LICH_KING,             // 사왕 (왕 - 1차 전직)
    WARLORD,               // 패왕 (왕 - 최종 전직)

    // -----------------------------
    // 🌿 신 계열 - 자연
    // -----------------------------
    GOD_OF_FIRE,           // 불의 신
    GOD_OF_WATER,          // 물의 신
    GOD_OF_EARTH,          // 땅의 신
    GOD_OF_WIND,           // 바람의 신
    GOD_OF_POISON,         // 독의 신
    GOD_OF_LIGHT,          // 빛의 신
    GOD_OF_DARKNESS,       // 어둠의 신
    GOD_OF_THUNDER,        // 전기의 신
    GOD_OF_ICE,            // 얼음의 신
    GOD_OF_DECAY,          // 부패의 신
    GOD_OF_STEEL,          // 강철의 신
    GOD_OF_FLORA,          // 식물의 신
    GOD_OF_SHADOW,         // 그림자의 신
    GOD_OF_NATURE,         // 자연의 신

    // -----------------------------
    // ☯ 신 계열 - 천하
    // -----------------------------
    GOD_OF_HEAVEN,         // 천계의 신
    GOD_OF_HELL,           // 악계의 신

    // -----------------------------
    // ⚡ 신 계열 - 권능
    // -----------------------------
    GOD_OF_LIFE, GOD_OF_DESTRUCTION, GOD_OF_CHAOS, GOD_OF_DIMENSIONS, GOD_OF_PLANETS,
    GOD_OF_THE_UNIVERSE, GOD_OF_TIME, GOD_OF_SPACE, GOD_OF_SPACETIME,
    GOD_OF_CREATION, GOD_OF_TIMELINES; // 시간선의 신

    // -----------------------------
    // 전직 매트릭스 필드
    // -----------------------------
    private static final int SIZE = values().length;               // 직업 개수
    private static final int[][] advancementMatrix = new int[SIZE][SIZE]; // 전직 관계 매트릭스
    private static final int[] maxAdvancements = new int[SIZE];   // 전직 없는 클래스 최대 단계
    private static final Set<Class> FIRST_TIER_CLASSES = EnumSet.of(
            APPRENTICE_SLAYER, THIRD_RATE_MARTIAL, APPRENTICE_WARLOCK, ARCHER,
            NOVICE_HUNTER, APPRENTICE_SORCERER, MAGE, APPRENTICE_CLERIC,
            APPRENTICE_HEALER, APPRENTICE_PHARMACIST, LICH_KING
    );

    // -----------------------------
    // 기본 메서드
    // -----------------------------

    /**
     * 기본 Class 반환
     * @return NOVICE
     */
    public static Class getDefault() {
        return NOVICE;
    }

    /**
     * 문자열에서 안전하게 Class 반환
     * @param name 문자열 이름
     * @return Class 또는 기본값 NOVICE
     */
    public static Class fromNameOrDefault(String name) {
        Class c = fromName(name);
        return c != null ? c : getDefault();
    }

    /**
     * 문자열에서 Class 반환
     * @param name 문자열 이름
     * @return Class 또는 null
     */
    public static Class fromName(String name) {
        if (name == null) return null;
        try {
            return Class.valueOf(name.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    // -----------------------------
    // 전직 관련 메서드
    // -----------------------------

    /**
     * from → to 전직 관계 설정 (1차 전직 → 다음 단계)
     * @param from 시작 직업 (1차 전직 클래스만 가능)
     * @param to 목표 직업
     * @param stage n차 전직 (1 이상)
     */
    public static void setAdvancement(Class from, Class to, int stage) {
        if (!FIRST_TIER_CLASSES.contains(from)) {
            throw new IllegalArgumentException("첫 번째 인수(from)는 반드시 1차 전직 클래스여야 합니다: " + from);
        }
        if (stage <= 0) throw new IllegalArgumentException("전직 단계(stage)는 1 이상이어야 합니다.");
        advancementMatrix[from.ordinal()][to.ordinal()] = stage;
    }

    /**
     * from 클래스의 전직 없는 클래스 최대 단계 설정
     * @param c 클래스
     * @param max 최대 단계
     */
    public static void setMaxAdvancement(Class c, int max) {
        maxAdvancements[c.ordinal()] = max;
    }

    /**
     * 전직 가능 여부 확인
     * @param from 시작 직업
     * @param to 목표 직업
     * @return 가능 여부
     */
    public static boolean canAdvanceTo(Class from, Class to) {
        return advancementMatrix[from.ordinal()][to.ordinal()] > 0;
    }

    /**
     * from → to 전직 단계 조회
     * @param from 시작 직업
     * @param to 목표 직업
     * @return 전직 단계 (0이면 불가)
     */
    public static int getAdvancementStage(Class from, Class to) {
        return advancementMatrix[from.ordinal()][to.ordinal()];
    }

    /**
     * from 클래스 최대 전직 단계 조회 (전직 없는 클래스 전용)
     * @param c 클래스
     * @return 최대 단계
     */
    public static int getMaxAdvancement(Class c) {
        return maxAdvancements[c.ordinal()];
    }

    /**
     * 모든 전직 관계 초기화
     */
    public static void clearAdvancements() {
        for (int[] row : advancementMatrix) {
            Arrays.fill(row, 0);
        }
    }

    /**
     * Class 이름 반환
     * @return 이름
     */
    @Override
    public String toString() {
        return name();
    }
}
