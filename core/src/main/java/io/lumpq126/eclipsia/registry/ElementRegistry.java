package io.lumpq126.eclipsia.registry;

import io.lumpq126.eclipsia.EclipsiaPlugin;
import io.lumpq126.eclipsia.core.mechanics.elements.*;
import io.lumpq126.elementapi.api.ElementAPI;

public class ElementRegistry {

    public static void registry(EclipsiaPlugin plugin) {
        ElementAPI api = ElementAPI.getInstance();

        if (api == null) {
            plugin.getLogger().severe("ElementAPI 플러그인을 찾을 수 없습니다.");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
            return;
        }

        api.registerElement(Normal.INSTANCE());
        api.registerElement(Fire.INSTANCE());
        api.registerElement(Water.INSTANCE());
        api.registerElement(Earth.INSTANCE());
        api.registerElement(Wind.INSTANCE());
        api.registerElement(Poison.INSTANCE());
        api.registerElement(Light.INSTANCE());
        api.registerElement(Darkness.INSTANCE());
        api.registerElement(Electric.INSTANCE());
        api.registerElement(Ice.INSTANCE());
        api.registerElement(Metal.INSTANCE());
        api.registerElement(Plants.INSTANCE());
        api.registerElement(Rot.INSTANCE());
        api.registerElement(Shadow.INSTANCE());
        api.registerElement(Angel.INSTANCE());
        api.registerElement(Devil.INSTANCE());
    }
}
