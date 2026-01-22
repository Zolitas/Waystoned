package de.zolitas.waystoned.blocks;

import de.zolitas.waystoned.Waystoned;
import de.zolitas.waystoned.data.WaystoneLocation;
import de.zolitas.waystoned.data.WaystoneSavedData;
import de.zolitas.waystoned.network.OpenWaystoneTeleportScreenPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

@EventBusSubscriber(modid = Waystoned.MODID)
public class WaystoneBlock extends Block {
  public WaystoneBlock() {
    super(
        Properties.of()
            .noOcclusion()
    );
  }

  @Override
  protected @NotNull ItemInteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state, @NotNull Level level,
                                                     @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
    if (level.isClientSide()) return ItemInteractionResult.SUCCESS;
    if (level.getServer() == null) return ItemInteractionResult.SUCCESS;
    if (!(player instanceof ServerPlayer serverPlayer)) return ItemInteractionResult.SUCCESS;

    ArrayList<WaystoneLocation> waystones = WaystoneSavedData.get(level.getServer()).getWaystones();
    PacketDistributor.sendToPlayer(serverPlayer, new OpenWaystoneTeleportScreenPacket(waystones));

    return ItemInteractionResult.SUCCESS;
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
