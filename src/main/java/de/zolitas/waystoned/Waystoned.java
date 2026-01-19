package de.zolitas.waystoned;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(Waystoned.MODID)
public class Waystoned {
  public static final String MODID = "waystoned";
  private static final Logger LOGGER = LogUtils.getLogger();

  public Waystoned(IEventBus modEventBus, ModContainer modContainer) {
    LOGGER.info("Waystoned initialized!");
  }
}
