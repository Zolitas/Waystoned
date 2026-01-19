package de.zolitas.waystoned.items;

import de.zolitas.waystoned.Waystoned;
import de.zolitas.waystoned.blocks.BlocksRegister;
import net.minecraft.world.item.BlockItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ItemsRegister {
  public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Waystoned.MODID);

  public static final DeferredItem<BlockItem> WAYSTONE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(BlocksRegister.WAYSTONE_BLOCK);
}
