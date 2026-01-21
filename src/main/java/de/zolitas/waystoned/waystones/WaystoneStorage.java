package de.zolitas.waystoned.waystones;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.zolitas.waystoned.Waystoned;
import net.minecraft.core.BlockPos;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = Waystoned.MODID)
public class WaystoneStorage {
  private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
  private static final Path WAYSTONES_FILE = ServerLifecycleHooks.getCurrentServer().getServerDirectory().resolve(Waystoned.MODID).resolve("waystones.json");

  public static final List<WaystoneLocation> WAYSTONES = new ArrayList<>();


  public static void save() {
    try {
      Files.createDirectories(WAYSTONES_FILE.getParent());
      try (Writer writer = Files.newBufferedWriter(WAYSTONES_FILE)) {
        JsonObject waystonesObject = new JsonObject();
        JsonArray waystonesArray = new JsonArray();

        for (WaystoneLocation waystone : WAYSTONES) {
          JsonObject locationObject = new JsonObject();
          locationObject.addProperty("x", waystone.getPos().getX());
          locationObject.addProperty("y", waystone.getPos().getY());
          locationObject.addProperty("z", waystone.getPos().getZ());
          waystonesArray.add(locationObject);
        }

        waystonesObject.add("waystones", waystonesArray);
        GSON.toJson(waystonesObject, writer);
      }
    }
    catch (IOException exception) {
      exception.printStackTrace();
    }
  }

  public static void load() {
    if (!Files.exists(WAYSTONES_FILE)) return;

    try (var reader = Files.newBufferedReader(WAYSTONES_FILE)) {
      JsonObject waystonesObject = GSON.fromJson(reader, JsonObject.class);

      WAYSTONES.clear();

      waystonesObject
          .getAsJsonArray("waystones")
          .forEach(location -> {
            JsonObject locationObject = location.getAsJsonObject();
            WaystoneLocation waystone = new WaystoneLocation(new BlockPos(
                locationObject.get("x").getAsInt(),
                locationObject.get("y").getAsInt(),
                locationObject.get("z").getAsInt()
            ));
            WAYSTONES.add(waystone);
          });
    }
    catch (IOException exception) {
      exception.printStackTrace();
    }
  }

  @SubscribeEvent
  private static void onServerStarted(ServerStartingEvent event) {
    load();
  }

  @SubscribeEvent
  private static void onServerStopped(ServerStoppingEvent event) {
    save();
  }
}
