package io.lumpq126.eclipsia.registry;

import io.lumpq126.eclipsia.EclipsiaPlugin;
import io.lumpq126.eclipsia.core.mechanics.elements.*;
import io.lumpq126.eclipsia.utilities.Log;
import io.lumpq126.elementapi.api.ElementAPI;
import io.lumpq126.elementapi.api.elements.Element;
import io.lumpq126.elementapi.api.elements.ElementRelation;
import io.lumpq126.elementapi.api.elements.relation.BasicRelation;

public class ElementRegistry {
    private static ElementAPI api;

    public static void registry(EclipsiaPlugin plugin) {
        api = ElementAPI.getInstance();

        if (api == null) {
            plugin.getLogger().severe("ElementAPI 플러그인을 찾을 수 없습니다.");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
            return;
        }

        BasicRelation strong = BasicRelation.STRONG;
        BasicRelation weak = BasicRelation.WEAK;
        BasicRelation mutualStrong = BasicRelation.MUTUAL_STRONG;
        BasicRelation mutualWeak = BasicRelation.MUTUAL_WEAK;

        Element normal   = Normal.INSTANCE();
        Element fire     = Fire.INSTANCE();
        Element water    = Water.INSTANCE();
        Element earth    = Earth.INSTANCE();
        Element wind     = Wind.INSTANCE();
        Element poison   = Poison.INSTANCE();
        Element light    = Light.INSTANCE();
        Element darkness = Darkness.INSTANCE();
        Element electric = Electric.INSTANCE();
        Element ice      = Ice.INSTANCE();
        Element metal    = Metal.INSTANCE();
        Element plants   = Plants.INSTANCE();
        Element rot      = Rot.INSTANCE();
        Element shadow   = Shadow.INSTANCE();
        Element angel    = Angel.INSTANCE();
        Element devil    = Devil.INSTANCE();

        register(normal);
        register(fire);
        register(water);
        register(earth);
        register(wind);
        register(poison);
        register(light);
        register(darkness);
        register(electric);
        register(ice);
        register(metal);
        register(plants);
        register(rot);
        register(shadow);
        register(angel);
        register(devil);

        relation(fire, water, weak);
        relation(fire, earth, weak);
        relation(fire, wind, strong);
        relation(fire, ice, strong);
        relation(fire, plants, strong);

        relation(water, earth, strong);
        relation(water, poison, strong);
        relation(water, electric, weak);
        relation(water, plants, weak);
        relation(water, rot, strong);

        relation(earth);
    }

    private static void register(Element element) {
        api = ElementAPI.getInstance();
        try {
            api.registerElement(element);
        } catch (Exception e) {
            Log.log("error", "", e);
        }
    }

    private static void relation(Element from, Element to, ElementRelation relation) {
        api = ElementAPI.getInstance();
        try {
            api.setRelation(from, to, relation);
        } catch (Exception e) {
            Log.log("error", "", e);
        }
    }
}
