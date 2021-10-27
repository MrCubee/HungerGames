package fr.mrcubee.hungergames.listeners.packet;

import fr.mrcubee.bukkit.events.PacketReceiveEvent;
import fr.mrcubee.bukkit.packet.GenericPacket;
import fr.mrcubee.bukkit.packet.GenericPacketPlayInSettings;
import fr.mrcubee.util.LangUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PacketReceived implements Listener {

    @EventHandler
    public void event(PacketReceiveEvent event) {
        GenericPacket genericPacket = event.getPacket();
        GenericPacketPlayInSettings settingPacket;

        if (genericPacket instanceof GenericPacketPlayInSettings) {
            settingPacket = (GenericPacketPlayInSettings) genericPacket;
            LangUtil.updatePlayerLocale(event.getReceiver(), settingPacket.getLocale());
        }
    }

}
