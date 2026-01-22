package de.zolitas.waystoned.network;

import de.zolitas.waystoned.Waystoned;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = Waystoned.MODID)
public class PacketRegister {
  @SubscribeEvent
  public static void registerPackets(RegisterPayloadHandlersEvent event) {
    PayloadRegistrar registrar = event.registrar("1");
    registrar.playToClient(
        OpenWaystoneTeleportScreenPacket.TYPE,
        OpenWaystoneTeleportScreenPacket.STREAM_CODEC,
        OpenWaystoneTeleportScreenPacket::handle
    );
  }
}
