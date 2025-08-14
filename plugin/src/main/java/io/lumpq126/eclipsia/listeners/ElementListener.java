package io.lumpq126.eclipsia.listeners;

import io.lumpq126.eclipsia.elements.Element;
import io.lumpq126.eclipsia.entities.EclipsiaEntity;
import io.lumpq126.eclipsia.events.ElementDamageEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class ElementListener implements Listener {

    /**
     * 1차: 원본 이벤트를 커스텀 이벤트로 변환
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onAttack(EntityDamageByEntityEvent event) {
        // 공격자의 속성을 가져와 attackElement로 전달
        EclipsiaEntity damagerEntity = new EclipsiaEntity(event.getDamager());
        ElementDamageEvent e = new ElementDamageEvent(event, damagerEntity.getElement());
        Bukkit.getPluginManager().callEvent(e);
    }

    /**
     * 2차: 커스텀 이벤트 처리 후 대미지 조정
     */
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onElementAttack(ElementDamageEvent event) {
        Element attackerElement = event.getDamagerElement();
        Element victimElement = event.getVictimElement();

        // 배율 계산
        double multiplier = Element.getDamageMultiplier(attackerElement, victimElement);

        // 기존 대미지
        double baseDamage = event.getOriginalEvent().getDamage();
        double finalDamage = baseDamage * multiplier;

        // 최종 대미지 적용
        event.getOriginalEvent().setDamage(finalDamage);
    }

    /**
     * 관계 값에 따른 대미지 배율 반환
     */
    private double getDamageMultiplier(int relation) {
        return switch (relation) {
            case 10 -> 2.0;  // 양방향
            case 5 -> 2.0;   // 최강점
            case 4 -> 1.5;   // 강점
            case 3 -> 0.5;   // 최약점
            case 2 -> 0.75;  // 약점
            default -> 1.0;  // 일반
        };
    }
}
