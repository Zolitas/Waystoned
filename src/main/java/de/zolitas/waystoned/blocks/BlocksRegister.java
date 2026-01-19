package de.zolitas.waystoned.blocks;

import de.zolitas.waystoned.Waystoned;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BlocksRegister {
  public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Waystoned.MODID);

  public static final DeferredBlock<WaystoneBlock> WAYSTONE_BLOCK = BLOCKS.register("waystone", WaystoneBlock::new);
}
