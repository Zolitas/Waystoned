package de.zolitas.waystoned.client.screen;

import de.zolitas.waystoned.client.WaystoneTeleportButton;
import de.zolitas.waystoned.data.WaystoneLocation;
import de.zolitas.waystoned.network.WaystoneTeleportRequestPacket;
import io.wispforest.owo.ui.base.BaseOwoScreen;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.component.TextBoxComponent;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.container.ScrollContainer;
import io.wispforest.owo.ui.core.*;
import lombok.RequiredArgsConstructor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@RequiredArgsConstructor
public class WaystoneTeleportScreen extends BaseOwoScreen<FlowLayout> {
  private final List<WaystoneLocation> waystones;
  private final BlockPos requestWaystonePosition;
  private final ResourceKey<Level> requestDimension;
  private TextBoxComponent searchBox;

  @Override
  protected @NotNull OwoUIAdapter<FlowLayout> createAdapter() {
    return OwoUIAdapter.create(this, Containers::verticalFlow);
  }

  @Override
  protected void build(FlowLayout rootComponent) {
    rootComponent
        .surface(Surface.VANILLA_TRANSLUCENT)
        .alignment(HorizontalAlignment.CENTER, VerticalAlignment.CENTER);

    FlowLayout mainContainer = Containers.verticalFlow(Sizing.fill(), Sizing.fill());
    mainContainer.margins(Insets.both(80, 30));
    mainContainer.padding(Insets.of(15));
    mainContainer.gap(10);
    mainContainer.surface(Surface.flat(new Color(0, 0, 0, 0.5f).argb()));

    searchBox = Components.textBox(Sizing.fill());
    mainContainer.child(searchBox);



    FlowLayout waystoneContainer = Containers.verticalFlow(Sizing.fill(), Sizing.content());
    ScrollContainer<FlowLayout> waystoneScrollContainer = Containers.verticalScroll(Sizing.fill(), Sizing.expand(), waystoneContainer);
    waystoneScrollContainer.scrollbar(ScrollContainer.Scrollbar.vanillaFlat());
    waystoneScrollContainer.scrollbarThiccness(5);
    waystoneContainer.margins(Insets.right(7));

    mainContainer.child(waystoneScrollContainer);
    waystoneContainer.gap(5);
    for (WaystoneLocation waystone : waystones) {
      WaystoneTeleportButton teleportButton = new WaystoneTeleportButton(Sizing.fill(), Sizing.content(), waystone);
      waystoneContainer.child(teleportButton);
      teleportButton.mouseDown().subscribe((mouseX, mouseY, button) -> {
        PacketDistributor.sendToServer(new WaystoneTeleportRequestPacket(waystone, requestWaystonePosition, requestDimension));
        Minecraft.getInstance().setScreen(null);
        return true;
      });
    }

    rootComponent.child(mainContainer);
  }

  @Override
  protected void setInitialFocus() {
    setInitialFocus(searchBox);
  }

  @Override
  public @Nullable GuiEventListener getFocused() {
    return searchBox;
  }
}
