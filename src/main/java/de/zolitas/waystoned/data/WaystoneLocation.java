package de.zolitas.waystoned.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

@Getter
@Builder
@AllArgsConstructor
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
}
