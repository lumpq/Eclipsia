package io.lumpq126.eclipsia.elements;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public class ElementEntity {
    private static NamespacedKey elementKey;
    private final Entity entity;

    /** 플러그인 초기화 시 호출 */
    public static void init(JavaPlugin plugin) {
        elementKey = new NamespacedKey(plugin, "element");
    }

    public ElementEntity(Entity entity) {
        this.entity = entity;
    }

    public Element getElement() {
        PersistentDataContainer data = entity.getPersistentDataContainer();
        String value = data.get(elementKey, PersistentDataType.STRING);
        if (value == null) return Element.NORMAL;
        try {
            return Element.valueOf(value);
        } catch (IllegalArgumentException e) {
            return Element.NORMAL;
        }
    }

    public void setElement(Element element) {
        PersistentDataContainer data = entity.getPersistentDataContainer();
        data.set(elementKey, PersistentDataType.STRING, element.name());
    }

    public Entity toEntity() {
        return entity;
    }

    public Player toPlayer() {
        return (entity instanceof Player) ? (Player) entity : null;
    }

    public static ElementEntity parseEntity(Entity e) {
        return new ElementEntity(e);
    }

    public static ElementEntity parsePlayer(Player p) {
        return new ElementEntity(p);
    }
}
