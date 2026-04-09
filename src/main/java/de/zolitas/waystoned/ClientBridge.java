package de.zolitas.waystoned;

import de.zolitas.waystoned.client.screen.WaystoneTeleportScreen;
import de.zolitas.waystoned.data.WaystoneLocation;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.List;

public class ClientBridge {
  public static void openWaystoneTeleportScreen(List<WaystoneLocation> waystones, BlockPos requestWaystonePosition, ResourceKey<Level> requestDimension) {
    Minecraft.getInstance().setScreen(new WaystoneTeleportScreen(waystones, requestWaystonePosition, requestDimension));
  }
}
