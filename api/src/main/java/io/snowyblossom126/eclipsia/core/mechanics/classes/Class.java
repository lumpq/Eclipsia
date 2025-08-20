package io.snowyblossom126.eclipsia.core.mechanics.classes;

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
    NOVICE("초심자"),

    // -----------------------------
    // ⚔ 검사 계열
    // -----------------------------
    GLADIATOR("검투사"),
    KNIGHT("기사"),
    HOLY_KNIGHT("성기사"),
    BERSERKER("광전사"),

    // -----------------------------
    // ⚔+🔮 하이브리드 계열
    // -----------------------------
    SPELLBLADE("마검사"),

    // -----------------------------
    // 🗡 도둑 계열
    // -----------------------------
    ROGUE("도적"),

    // -----------------------------
    // 🗡 살수 계열
    // -----------------------------
    APPRENTICE_SLAYER("견습살수"),
    THIRD_RATE_SLAYER("삼류살수"),
    SECOND_RATE_SLAYER("이류살수"),
    FIRST_RATE_SLAYER("일류살수"),
    ELITE_SLAYER("특급살수"),
    MASTERLESS_SLAYER("무급살수"),
    CRIMSON_SHADOW("적영"),
    DARK_SHADOW("암영"),
    VOID_SHADOW("무영"),

    // -----------------------------
    // 🥋 무인 계열
    // -----------------------------
    THIRD_RATE_MARTIAL("삼류무인"),
    SECOND_RATE_MARTIAL("이류무인"),
    FIRST_RATE_MARTIAL("일류무인"),
    PEAK_MARTIAL("절정무인"),
    TRANSCENDENT_MARTIAL("초절정"),
    GRANDMASTER_MARTIAL("무극"),

    // -----------------------------
    // 🔮 마도 계열
    // -----------------------------
    APPRENTICE_WARLOCK("마졸"),
    DEMONIC_ARCHER("마궁"),
    WICKED_BANDIT("흉적"),
    DEMON_STALKER("살귀"),
    EVIL_WRAITH("악귀"),
    DEATH_REAPER("살성"),
    ARCANE_MASTER("마선"),
    DEMON_LORD("마존"),
    CELESTIAL_ARCHMAGE("천마선"),
    HEAVENLY_DEMON("천마"),

    // -----------------------------
    // 🏹 궁수 계열
    // -----------------------------
    ARCHER("궁사"),
    MASTER_ARCHER("명궁"),
    ELITE_MARKSMAN("고궁"),
    DIVINE_ARCHER("신궁"),
    SAGE_ARCHER("현궁"),
    LEGENDARY_ARCHER("역궁"),

    // -----------------------------
    // 🐾 사냥꾼 계열
    // -----------------------------
    NOVICE_HUNTER("견습 사냥꾼"),
    SKILLED_HUNTER("능숙한 사냥꾼"),
    MASTER_HUNTER("숙달된 사냥꾼"),
    SLAYER_HUNTER("학살자"),
    DIVINE_SLAYER("신살자"),

    // -----------------------------
    // 📜 마법사 계열
    // -----------------------------
    APPRENTICE_SORCERER("견습마법사"),
    SORCERER("마법사"),

    // -----------------------------
    // 📜 마도사 계열
    // -----------------------------
    MAGE("마도사"),
    ARCHMAGE("대마도사"),
    SAGE("현자"),

    // -----------------------------
    // ✝ 성직자 계열
    // -----------------------------
    APPRENTICE_CLERIC("견습 성직자"),
    CLERIC("성직자"),
    BISHOP("주교"),
    APOSTLE("사도"),
    PROPHET("교주"),
    SAVIOR("구세주"),
    DEMIGOD("반신"),

    // -----------------------------
    // 🔮 마술사 계열
    // -----------------------------
    CURSE_WEAVER("저주술사"),
    ELEMENTALIST("정령사"),
    ELEMENTAL_SUMMONER("정령 소환사"),
    ARCANE_DESTROYER("파괴술사"),
    ILLUSIONIST("둔갑술사"),
    GRAND_ENCHANTER("강마술사"),

    // -----------------------------
    // 🩺 의술사 계열
    // -----------------------------
    APPRENTICE_HEALER("견습 의술사"),
    HEALER("의술사"),
    MASTER_HEALER("숙달된 의술사"),

    // -----------------------------
    // ⚗ 의약 제조사 계열
    // -----------------------------
    APPRENTICE_PHARMACIST("견습 의약사"),
    PHARMACIST("의약사"),
    MASTER_PHARMACIST("숙달된 의약사"),
    HERBALIST("약선"),
    DIVINE_HERBALIST("약신"),

    // -----------------------------
    // 👑 왕 계열
    // -----------------------------
    LICH_KING("사왕"),
    WARLORD("패왕"),

    // -----------------------------
    // 🌿 신 계열 - 자연
    // -----------------------------
    GOD_OF_FIRE("불의 신"),
    GOD_OF_WATER("물의 신"),
    GOD_OF_EARTH("땅의 신"),
    GOD_OF_WIND("바람의 신"),
    GOD_OF_POISON("독의 신"),
    GOD_OF_LIGHT("빛의 신"),
    GOD_OF_DARKNESS("어둠의 신"),
    GOD_OF_THUNDER("전기의 신"),
    GOD_OF_ICE("얼음의 신"),
    GOD_OF_DECAY("부패의 신"),
    GOD_OF_STEEL("강철의 신"),
    GOD_OF_FLORA("식물의 신"),
    GOD_OF_SHADOW("그림자의 신"),
    GOD_OF_NATURE("자연의 신"),

    // -----------------------------
    // ☯ 신 계열 - 천하
    // -----------------------------
    GOD_OF_HEAVEN("천계의 신"),
    GOD_OF_HELL("악계의 신"),

    // -----------------------------
    // ⚡ 신 계열 - 권능
    // -----------------------------
    GOD_OF_LIFE("생명의 신"),
    GOD_OF_DESTRUCTION("파괴의 신"),
    GOD_OF_CHAOS("혼돈의 신"),
    GOD_OF_DIMENSIONS("차원의 신"),
    GOD_OF_PLANETS("행성의 신"),
    GOD_OF_THE_UNIVERSE("우주의 신"),
    GOD_OF_TIME("시간의 신"),
    GOD_OF_SPACE("공간의 신"),
    GOD_OF_SPACETIME("시공의 신"),
    GOD_OF_CREATION("창조의 신"),
    GOD_OF_TIMELINES("시간선의 신");

    // -----------------------------
    // 한글명 필드
    // -----------------------------
    private final String koreanName;

    Class(String koreanName) {
        this.koreanName = koreanName;
    }

    /**
     * 한글명 반환
     */
    public String getKoreaName() {
        return koreanName;
    }

    // -----------------------------
    // 전직 매트릭스 필드
    // -----------------------------
    private static final int SIZE = values().length;
    private static final int[][] advancementMatrix = new int[SIZE][SIZE];
    private static final int[] maxAdvancements = new int[SIZE];
    private static final Set<Class> FIRST_TIER_CLASSES = EnumSet.of(
            APPRENTICE_SLAYER, THIRD_RATE_MARTIAL, APPRENTICE_WARLOCK, ARCHER,
            NOVICE_HUNTER, APPRENTICE_SORCERER, MAGE, APPRENTICE_CLERIC,
            APPRENTICE_HEALER, APPRENTICE_PHARMACIST, LICH_KING
    );

    public static Class getDefault() {
        return NOVICE;
    }

    public static Class fromNameOrDefault(String name) {
        Class c = fromName(name);
        return c != null ? c : getDefault();
    }

    public static Class fromName(String name) {
        if (name == null) return null;
        try {
            return Class.valueOf(name.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    public static void setAdvancement(Class from, Class to, int stage) {
        if (!FIRST_TIER_CLASSES.contains(from)) {
            throw new IllegalArgumentException("첫 번째 인수(from)는 반드시 1차 전직 클래스여야 합니다: " + from);
        }
        if (stage <= 0) throw new IllegalArgumentException("전직 단계(stage)는 1 이상이어야 합니다.");
        advancementMatrix[from.ordinal()][to.ordinal()] = stage;
    }

    public static void setMaxAdvancement(Class c, int max) {
        maxAdvancements[c.ordinal()] = max;
    }

    public static boolean canAdvanceTo(Class from, Class to) {
        return advancementMatrix[from.ordinal()][to.ordinal()] > 0;
    }

    public static int getMaxAdvancement(Class c) {
        return maxAdvancements[c.ordinal()];
    }

    public static void clearAdvancements() {
        for (int[] row : advancementMatrix) {
            Arrays.fill(row, 0);
        }
    }

    @Override
    public String toString() {
        return name();
    }
}
