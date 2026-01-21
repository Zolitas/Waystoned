package de.zolitas.waystoned.blocks;

import de.zolitas.waystoned.Waystoned;
import de.zolitas.waystoned.data.WaystoneLocation;
import de.zolitas.waystoned.data.WaystoneSavedData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;

@EventBusSubscriber(modid = Waystoned.MODID)
public class WaystoneBlock extends Block {
  public WaystoneBlock() {
    super(
        Properties.of()
            .noOcclusion()
    );
  }

  @SubscribeEvent
  private static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
    if (event.getLevel().isClientSide()) return;
    if (!(event.getLevel() instanceof ServerLevel serverLevel)) return;
    if (!(event.getPlacedBlock().getBlock() instanceof WaystoneBlock)) return;

    WaystoneLocation waystone = WaystoneLocation.builder()
        .name("test")
        .pos(event.getPos())
        .dimension(serverLevel.dimension().location())
        .build();

    WaystoneSavedData savedData = WaystoneSavedData.get(serverLevel.getServer());
    savedData.getWaystones().add(waystone);
    savedData.setDirty();
  }

  @SubscribeEvent
  private static void onBlockBreak(BlockEvent.BreakEvent event) {
    if (event.getLevel().isClientSide()) return;
    if (!(event.getLevel() instanceof ServerLevel serverLevel)) return;
    if (!(event.getState().getBlock() instanceof WaystoneBlock)) return;

    WaystoneSavedData savedData = WaystoneSavedData.get(serverLevel.getServer());
    savedData.getWaystones().removeIf(waystone -> waystone.getPos().equals(event.getPos()) && waystone.getDimension().equals(serverLevel.dimension().location()));
    savedData.setDirty();
  }
}
