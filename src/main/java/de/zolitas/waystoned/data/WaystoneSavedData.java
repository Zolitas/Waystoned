package de.zolitas.waystoned.data;

import lombok.Getter;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.saveddata.SavedData;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

@EventBusSubscriber
public class WaystoneSavedData extends SavedData {
  @Getter
  private final ArrayList<WaystoneLocation> waystones = new ArrayList<>();

  private static final String WAYSTONES_KEY = "waystones";

  public static WaystoneSavedData get(MinecraftServer server) {
    return server.overworld().getDataStorage().computeIfAbsent(
        new SavedData.Factory<>(WaystoneSavedData::new, WaystoneSavedData::load),
        "waystone_data"
    );
  }

  @Override
  public @NotNull CompoundTag save(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider provider) {
    ListTag waystonesTag = new ListTag();
    waystones.stream()
        .map(WaystoneLocation::toCompoundTag)
        .forEach(waystonesTag::add);
    tag.put(WAYSTONES_KEY, waystonesTag);

    return tag;
  }

  private static WaystoneSavedData load(CompoundTag tag, HolderLookup.Provider provider) {
    WaystoneSavedData data = new WaystoneSavedData();
    tag.getList(WAYSTONES_KEY, 10).stream()
        .map(CompoundTag.class::cast)
        .map(WaystoneLocation::fromCompoundTag)
        .forEach(data.waystones::add);

    return data;
  }

  @SubscribeEvent
  private void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
    if (!(event.getEntity() instanceof ServerPlayer player)) return;

    // rename waystones when the name changes
    waystones
        .stream()
        .filter(waystone -> waystone.getOwnerUUID().equals(player.getGameProfile().getId().toString()))
        .filter(waystone -> !(waystone.getName().equals(player.getGameProfile().getName())))
        .forEach(waystone -> waystone.setName(player.getGameProfile().getName()));
  }
}
