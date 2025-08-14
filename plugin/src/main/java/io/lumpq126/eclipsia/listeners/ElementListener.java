package io.lumpq126.eclipsia.listeners;

import io.lumpq126.eclipsia.EclipsiaPlugin;
import io.lumpq126.eclipsia.elements.Element;
import io.lumpq126.eclipsia.entities.EclipsiaEntity;
import io.lumpq126.eclipsia.events.ElementDamageEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class ElementListener implements Listener {

    // 1차: 원본 이벤트를 커스텀 이벤트로 변환
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onAttack(EntityDamageByEntityEvent event) {
        EclipsiaEntity eEntity = new EclipsiaEntity(EclipsiaPlugin.getInstance(), event.getEntity());
        ElementDamageEvent e = new ElementDamageEvent(event, eEntity.getElement());
        Bukkit.getPluginManager().callEvent(e);
    }

    // 2차: 커스텀 이벤트 처리 후 대미지 조정
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onElementAttack(ElementDamageEvent event) {
        Element attackerElement = event.getDamagerElement();
        Element victimElement = event.getVictimElement();

        // 관계 값 가져오기
        int relation = attackerElement.getRelation(victimElement);

        // 배율 계산
        double multiplier = getDamageMultiplier(relation);

        // 기존 대미지
        double baseDamage = event.getOriginalEvent().getDamage();
        double finalDamage = baseDamage * multiplier;

        // 최종 대미지 적용
        event.getOriginalEvent().setDamage(finalDamage);
    }

    private double getDamageMultiplier(int relation) {
        return switch (relation) {
            case 10 -> 2.0;  //양방향
            case 5 -> 2.0;   // 최강점
            case 4 -> 1.5;   // 강점
            case 3 -> 0.5;   // 최약점
            case 2 -> 0.75;  // 약점
            default -> 1.0;  // 일반
        };
    }
}
