package de.zolitas.waystoned.blocks;

import com.mojang.serialization.MapCodec;
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
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

@EventBusSubscriber(modid = Waystoned.MODID)
public class WaystoneBlock extends HorizontalDirectionalBlock {
  public static final MapCodec<WaystoneBlock> CODEC = simpleCodec(WaystoneBlock::new);
  public static final double WAYSTONE_COLLISION_HEIGHT = 0.125;

  public WaystoneBlock(Properties properties) {
    super(properties);
  }

  public WaystoneBlock() {
    this(
        Properties.of()
            .noOcclusion()
    );
  }

  @Override
  protected @NotNull VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter level,
                                                  @NotNull BlockPos pos, @NotNull CollisionContext context) {
    return Shapes.box(0, 0, 0, 1, WAYSTONE_COLLISION_HEIGHT, 1);
  }

  @Override
  protected @NotNull ItemInteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state, @NotNull Level level,
                                                     @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
    if (level.isClientSide()) return ItemInteractionResult.SUCCESS;
    if (level.getServer() == null) return ItemInteractionResult.SUCCESS;
    if (!(player instanceof ServerPlayer serverPlayer)) return ItemInteractionResult.SUCCESS;

    ArrayList<WaystoneLocation> waystones = WaystoneSavedData.get(level.getServer()).getWaystones();
    PacketDistributor.sendToPlayer(serverPlayer, new OpenWaystoneTeleportScreenPacket(waystones, pos, level.dimension()));

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
        .rot(event.getPlacedBlock().getValue(FACING).toYRot())
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

  @Override
  protected @NotNull MapCodec<? extends HorizontalDirectionalBlock> codec() {
    return CODEC;
  }

  public BlockState getStateForPlacement(BlockPlaceContext context) {
    return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
  }

  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(FACING);
  }
}
