package de.zolitas.waystoned;

import de.zolitas.waystoned.client.screen.WaystoneTeleportScreen;
import de.zolitas.waystoned.data.WaystoneLocation;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;

import java.util.List;

public class ClientBridge {
  public static void openWaystoneTeleportScreen(List<WaystoneLocation> waystones, BlockPos requestWaystonePosition) {
    Minecraft.getInstance().setScreen(new WaystoneTeleportScreen(waystones, requestWaystonePosition));
  }
}
