package de.zolitas.waystoned.network;

import de.zolitas.waystoned.Waystoned;
import de.zolitas.waystoned.data.WaystoneLocation;
import de.zolitas.waystoned.data.WaystoneSavedData;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class WaystoneRenameRequestPacket implements CustomPacketPayload {
  @Getter
  private final String newName;

  @Getter
  private final BlockPos waystonePosition;

  public static final Type<WaystoneRenameRequestPacket> TYPE =
      new Type<>(ResourceLocation.fromNamespaceAndPath(Waystoned.MODID, "waystone_rename_request"));

  public static void handle(WaystoneRenameRequestPacket packet, IPayloadContext context) {
    if (!(context.player() instanceof ServerPlayer serverPlayer)) return;
    if (serverPlayer.getServer() == null) return;

    context.enqueueWork(() -> {
      ResourceLocation dimensionLocation = serverPlayer.level().dimension().location();
      WaystoneSavedData waystoneSavedData = WaystoneSavedData.get(serverPlayer.getServer());
      WaystoneLocation reqWaystone = waystoneSavedData.getWaystones().stream()
          .filter(waystone -> waystone.getPos().equals(packet.getWaystonePosition()) && waystone.getDimension().equals(dimensionLocation))
          .findFirst().orElse(null);

      if (reqWaystone == null) {
        serverPlayer.sendSystemMessage(Component.translatable("message.waystoned.invalid_request_block").withStyle(ChatFormatting.RED));
        return;
      }

      if (serverPlayer.position().distanceTo(reqWaystone.getPos().getCenter()) > 10) {
        serverPlayer.sendSystemMessage(Component.translatable("message.waystoned.too_far_away").withStyle(ChatFormatting.RED));
        return;
      }

      if (reqWaystone.getName().equals(packet.getNewName())) return;

      if (!reqWaystone.getOwnerUUID().equals(serverPlayer.getGameProfile().getId().toString())) {
        serverPlayer.sendSystemMessage(Component.translatable("message.waystoned.not_owner").withStyle(ChatFormatting.RED));
        return;
      }

      reqWaystone.setName(packet.getNewName());
      waystoneSavedData.setDirty();
    });
  }

  public static final StreamCodec<RegistryFriendlyByteBuf, WaystoneRenameRequestPacket> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.STRING_UTF8,
      WaystoneRenameRequestPacket::getNewName,
      BlockPos.STREAM_CODEC,
      WaystoneRenameRequestPacket::getWaystonePosition,
      WaystoneRenameRequestPacket::new
  );

  @Override
  public @NotNull Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
