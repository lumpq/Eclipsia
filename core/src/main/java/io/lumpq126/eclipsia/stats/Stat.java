package io.lumpq126.eclipsia.stats;

import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.UUID;

/**
 * 플레이어 스탯 시스템
 * - Entity 기반
 * - 최대 저장 슬롯 제한 없음
 * - Map 대신 ArrayList로 UUID/스탯 데이터 관리
 */
public enum Stat {

    STRENGTH,     // 힘
    CONSTITUTION, // 체력
    AGILITY,      // 민첩
    DEXTERITY,    // 손재주
    INTELLIGENCE, // 지능
    WISDOM;       // 지혜

    private static final int INITIAL_STAT = 5;
    private static final int INITIAL_POINT = 5;

    /** UUID 리스트 */
    private static final ArrayList<UUID> uuidTable = new ArrayList<>();
    /** 스탯 값 저장: [playerIndex][statIndex] */
    public static final ArrayList<int[]> statValues = new ArrayList<>();
    /** 스탯 포인트 저장 */
    public static final ArrayList<Integer> statPoints = new ArrayList<>();

    /** UUID로 인덱스 찾기 (없으면 새로 생성) */
    public static int getEntityIndex(Entity entity) {
        UUID uuid = entity.getUniqueId();
        int index = uuidTable.indexOf(uuid);
        if (index != -1) {
            return index;
        }
        // 새로 추가
        uuidTable.add(uuid);
        int[] stats = new int[values().length];
        Arrays.fill(stats, INITIAL_STAT);
        statValues.add(stats);
        statPoints.add(INITIAL_POINT);
        return uuidTable.size() - 1;
    }

    /** 이름 → Stat 변환 */
    public static Stat fromName(String name) {
        if (name == null) return null;
        try {
            return Stat.valueOf(name.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /** 모든 데이터 초기화 */
    public static void clearAll() {
        uuidTable.clear();
        statValues.clear();
        statPoints.clear();
    }
}
