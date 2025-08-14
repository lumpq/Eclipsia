package io.lumpq126.eclipsia.protocol.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import io.lumpq126.eclipsia.utilities.Log;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.entity.Player;

public class SendActionBarToPacket {

    public static void send(Player player, String message) {
        try {
            // MiniMessage 문자열 -> Adventure Component
            Component component = MiniMessage.miniMessage().deserialize(message);

            // Adventure Component -> JSON
            String json = GsonComponentSerializer.gson().serialize(component);

            PacketContainer packet = new PacketContainer(PacketType.Play.Server.CHAT);
            packet.getChatComponents().write(0, WrappedChatComponent.fromJson(json));
            packet.getBytes().write(0, (byte) 2); // 액션바 위치

            ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
        } catch (Exception e) {
            Log.log("error", "Failed to send actionbar", e);
        }
    }
}
