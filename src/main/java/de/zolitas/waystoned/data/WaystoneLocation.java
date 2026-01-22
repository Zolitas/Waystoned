package de.zolitas.waystoned.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
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
  private final String name;
  private final BlockPos pos;
  private final ResourceLocation dimension;

  public static CompoundTag toCompoundTag(WaystoneLocation waystone) {
    CompoundTag tag = new CompoundTag();
    tag.putString("name", waystone.getName());
    tag.putInt("x", waystone.getPos().getX());
    tag.putInt("y", waystone.getPos().getY());
    tag.putInt("z", waystone.getPos().getZ());
    tag.putString("dimension", waystone.getDimension().toString());
    return tag;
  }

  public static WaystoneLocation fromCompoundTag(CompoundTag tag) {
    return WaystoneLocation.builder()
        .name(tag.getString("name"))
        .pos(new BlockPos(tag.getInt("x"), tag.getInt("y"), tag.getInt("z")))
        .dimension(ResourceLocation.parse(tag.getString("dimension")))
        .build();
  }

  public static final StreamCodec<RegistryFriendlyByteBuf, WaystoneLocation> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.STRING_UTF8, WaystoneLocation::getName,
      BlockPos.STREAM_CODEC, WaystoneLocation::getPos,
      ResourceLocation.STREAM_CODEC, WaystoneLocation::getDimension,
      WaystoneLocation::new
  );
}
