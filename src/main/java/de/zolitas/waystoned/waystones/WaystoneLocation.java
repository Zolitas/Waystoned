package de.zolitas.waystoned.waystones;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.core.BlockPos;

@RequiredArgsConstructor
public class WaystoneLocation {
  @Getter
  private final BlockPos pos;
}
