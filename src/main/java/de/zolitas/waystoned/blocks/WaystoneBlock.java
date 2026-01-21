package de.zolitas.waystoned.blocks;

import de.zolitas.waystoned.waystones.WaystoneLocation;
import de.zolitas.waystoned.waystones.WaystoneStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.Nullable;

public class WaystoneBlock extends Block {
  public WaystoneBlock() {
    super(
        Properties.of()
            .noOcclusion()
    );
  }

  @Override
  public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
    super.setPlacedBy(level, pos, state, placer, stack);

    if (!level.isClientSide()) {
      WaystoneStorage.WAYSTONES.add(new WaystoneLocation(pos));
      if (placer instanceof ServerPlayer player) {
        for (WaystoneLocation waystone : WaystoneStorage.WAYSTONES) {
          player.sendSystemMessage(Component.literal("Waystone placed at " + waystone.getPos().toString()));
        }
      }
    }
  }

  @Override
  public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
    if (!level.isClientSide()) {
      WaystoneStorage.WAYSTONES.removeIf(location -> location.getPos().equals(pos));
    }
    return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
  }
}
