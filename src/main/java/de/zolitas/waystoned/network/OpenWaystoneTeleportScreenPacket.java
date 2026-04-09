package de.zolitas.waystoned.network;

import de.zolitas.waystoned.ClientBridge;
import de.zolitas.waystoned.Waystoned;
import de.zolitas.waystoned.data.WaystoneLocation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@RequiredArgsConstructor
public class OpenWaystoneTeleportScreenPacket implements CustomPacketPayload {
  @Getter
  private final List<WaystoneLocation> waystones;

  @Getter
  private final BlockPos requestWaystonePosition;

  @Getter
  private final ResourceKey<Level> requestDimension;

  public static final Type<OpenWaystoneTeleportScreenPacket> TYPE =
      new Type<>(ResourceLocation.fromNamespaceAndPath(Waystoned.MODID, "open_waystone_teleport_screen"));

  public static void handle(OpenWaystoneTeleportScreenPacket packet, IPayloadContext context) {
    context.enqueueWork(() -> ClientBridge.openWaystoneTeleportScreen(packet.getWaystones(), packet.getRequestWaystonePosition(), packet.getRequestDimension()));
  }

  public static final StreamCodec<RegistryFriendlyByteBuf, OpenWaystoneTeleportScreenPacket> STREAM_CODEC = StreamCodec.composite(
      WaystoneLocation.STREAM_CODEC.apply(ByteBufCodecs.list()),
      OpenWaystoneTeleportScreenPacket::getWaystones,
      BlockPos.STREAM_CODEC,
      OpenWaystoneTeleportScreenPacket::getRequestWaystonePosition,
      ResourceKey.streamCodec(Registries.DIMENSION),
      OpenWaystoneTeleportScreenPacket::getRequestDimension,
      OpenWaystoneTeleportScreenPacket::new
  );

  @Override
  public @NotNull Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
