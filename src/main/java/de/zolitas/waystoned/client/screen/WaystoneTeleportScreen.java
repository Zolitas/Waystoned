package de.zolitas.waystoned.client.screen;

import de.zolitas.waystoned.data.WaystoneLocation;
import io.wispforest.owo.ui.base.BaseOwoScreen;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.container.ScrollContainer;
import io.wispforest.owo.ui.core.*;
import lombok.RequiredArgsConstructor;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@RequiredArgsConstructor
public class WaystoneTeleportScreen extends BaseOwoScreen<FlowLayout> {
  private final List<WaystoneLocation> waystones;

  @Override
  protected @NotNull OwoUIAdapter<FlowLayout> createAdapter() {
    return OwoUIAdapter.create(this, Containers::verticalFlow);
  }

  @Override
  protected void build(FlowLayout rootComponent) {
    rootComponent
        .surface(Surface.VANILLA_TRANSLUCENT)
        .alignment(HorizontalAlignment.CENTER, VerticalAlignment.CENTER);

    FlowLayout waystoneContainer = Containers.verticalFlow(Sizing.content(), Sizing.content());
    ScrollContainer<FlowLayout> scrollContainer = Containers.verticalScroll(Sizing.content(), Sizing.fill(), waystoneContainer);

    waystoneContainer.gap(2);

    scrollContainer.padding(Insets.of(10));
    scrollContainer.margins(Insets.of(10));
    scrollContainer.surface(Surface.DARK_PANEL);

    for (WaystoneLocation waystone : waystones) {
      waystoneContainer.child(Components.button(Component.literal(waystone.toString()), button -> {

      }));
    }

    rootComponent.child(scrollContainer);
  }

  public static void openScreen(List<WaystoneLocation> waystones) {
    Minecraft.getInstance().setScreen(new WaystoneTeleportScreen(waystones));
  }
}
