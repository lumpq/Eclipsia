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

        // 불 🔥
        relation(fire, plants, strong);
        relation(fire, ice, strong);
        relation(fire, poison, strong);
        relation(fire, water, weak);
        relation(fire, earth, weak);

        // 물 💧
        relation(water, fire, strong);
        relation(water, earth, strong);
        relation(water, poison, strong);
        relation(water, rot, strong);
        relation(water, electric, weak);
        relation(water, plants, weak);

        // 땅 🌍
        relation(earth, electric, strong);
        relation(earth, metal, strong);
        relation(earth, wind, weak);
        relation(earth, ice, weak);

        // 바람 💨
        relation(wind, plants, strong);
        relation(wind, earth, strong);
        relation(wind, ice, weak);
        relation(wind, metal, weak);

        // 번개 ⚡
        relation(electric, water, strong);
        relation(electric, metal, strong);
        relation(electric, earth, weak);

        // 얼음 ❄️
        relation(ice, earth, strong);
        relation(ice, plants, strong);
        relation(ice, wind, strong);
        relation(ice, fire, weak);

        // 식물 🌱
        relation(plants, water, strong);
        relation(plants, earth, strong);
        relation(plants, fire, weak);
        relation(plants, ice, weak);
        relation(plants, wind, weak);

        // 빛 ☀️ ↔ 어둠 🌑
        relation(light, darkness, mutualStrong);
        // 빛 ☀️ ↔ 그림자 🌒
        relation(light, shadow, mutualStrong);
        // 천사 😇 ↔ 악마 😈
        relation(angel, devil, mutualStrong);

        // 추가 단방향
        relation(light, devil, strong);
        relation(light, rot, strong);

        relation(darkness, angel, strong);

        // 독 ☠️
        relation(poison, plants, strong);
        relation(poison, light, weak);
        relation(poison, water, weak);

        // 부패 🦠
        relation(rot, light, weak);
        relation(rot, fire, weak);
        relation(rot, water, weak);
        relation(rot, darkness, strong);
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