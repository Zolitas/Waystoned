package de.zolitas.waystoned.data;

import lombok.*;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

@Getter
@Builder
@AllArgsConstructor
@ToString
public class WaystoneLocation {
  @Setter
  private String name;
  private final BlockPos pos;
  private final Float rot;
  private final ResourceLocation dimension;
  private final String ownerName;
  private final String ownerUUID;

  public static CompoundTag toCompoundTag(WaystoneLocation waystone) {
    CompoundTag tag = new CompoundTag();
    tag.putString("name", waystone.getName());
    tag.putInt("x", waystone.getPos().getX());
    tag.putInt("y", waystone.getPos().getY());
    tag.putInt("z", waystone.getPos().getZ());
    tag.putFloat("rot", waystone.getRot());
    tag.putString("dimension", waystone.getDimension().toString());
    tag.putString("ownerName", waystone.getOwnerName());
    tag.putString("ownerUUID", waystone.getOwnerUUID());
    return tag;
  }

  public static WaystoneLocation fromCompoundTag(CompoundTag tag) {
    return WaystoneLocation.builder()
        .name(tag.getString("name"))
        .pos(new BlockPos(tag.getInt("x"), tag.getInt("y"), tag.getInt("z")))
        .rot(tag.getFloat("rot"))
        .dimension(ResourceLocation.parse(tag.getString("dimension")))
        .ownerName(tag.getString("ownerName"))
        .ownerUUID(tag.getString("ownerUUID"))
        .build();
  }

  public static final StreamCodec<RegistryFriendlyByteBuf, WaystoneLocation> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.STRING_UTF8, WaystoneLocation::getName,
      BlockPos.STREAM_CODEC, WaystoneLocation::getPos,
      ByteBufCodecs.FLOAT, WaystoneLocation::getRot,
      ResourceLocation.STREAM_CODEC, WaystoneLocation::getDimension,
      ByteBufCodecs.STRING_UTF8, WaystoneLocation::getOwnerName,
      ByteBufCodecs.STRING_UTF8, WaystoneLocation::getOwnerUUID,
      WaystoneLocation::new
  );
}
