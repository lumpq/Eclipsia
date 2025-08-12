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
    private Element cachedElement;

    public static void init(JavaPlugin plugin) {
        elementKey = new NamespacedKey(plugin, "element");
    }

    public ElementEntity(Entity entity) {
        this.entity = entity;
    }

    public Element getElement() {
        if (cachedElement != null) return cachedElement;
        PersistentDataContainer data = entity.getPersistentDataContainer();
        String value = data.get(elementKey, PersistentDataType.STRING);
        if (value == null) return cachedElement = Element.NORMAL;
        Element e = ElementEntityHelper.getElementByName(value);
        return cachedElement = (e != null) ? e : Element.NORMAL;
    }

    public void setElement(Element element) {
        if (element == null) element = Element.NORMAL;
        cachedElement = element;
        PersistentDataContainer data = entity.getPersistentDataContainer();
        data.set(elementKey, PersistentDataType.STRING, element.getName());
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
