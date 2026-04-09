package de.zolitas.waystoned.network;

import de.zolitas.waystoned.Waystoned;
import de.zolitas.waystoned.blocks.BlocksRegister;
import de.zolitas.waystoned.data.WaystoneLocation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class WaystoneTeleportRequestPacket implements CustomPacketPayload {
  @Getter
  private final WaystoneLocation waystone;

  @Getter
  private final BlockPos requestWaystonePosition;

  public static final Type<WaystoneTeleportRequestPacket> TYPE =
      new Type<>(ResourceLocation.fromNamespaceAndPath(Waystoned.MODID, "waystone_teleport_request"));

  public static void handle(WaystoneTeleportRequestPacket packet, IPayloadContext context) {
    if (!(context.player() instanceof ServerPlayer serverPlayer)) return;
    if (serverPlayer.getServer() == null) return;

    context.enqueueWork(() -> {
      WaystoneLocation waystone = packet.getWaystone();
      ServerLevel level = serverPlayer.getServer().getLevel(ResourceKey.create(Registries.DIMENSION, waystone.getDimension()));

      if (level == null) return;

      if (!level.getBlockState(packet.getRequestWaystonePosition()).getBlock().equals(BlocksRegister.WAYSTONE_BLOCK.get())) {
        serverPlayer.sendSystemMessage(Component.translatable("message.waystoned.invalid_request_block").withStyle(ChatFormatting.RED));
        return;
      }
      if (serverPlayer.position().distanceTo(packet.getRequestWaystonePosition().getCenter()) > 10) {
        serverPlayer.sendSystemMessage(Component.translatable("message.waystoned.too_far_away").withStyle(ChatFormatting.RED));
        return;
      }

      serverPlayer.teleportTo(
          level,
          waystone.getPos().getX(),
          waystone.getPos().getY(),
          waystone.getPos().getZ(),
          0,
          0
      );
    });
  }

  public static final StreamCodec<RegistryFriendlyByteBuf, WaystoneTeleportRequestPacket> STREAM_CODEC = StreamCodec.composite(
      WaystoneLocation.STREAM_CODEC,
      WaystoneTeleportRequestPacket::getWaystone,
      BlockPos.STREAM_CODEC,
      WaystoneTeleportRequestPacket::getRequestWaystonePosition,
      WaystoneTeleportRequestPacket::new
  );

  @Override
  public @NotNull Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
