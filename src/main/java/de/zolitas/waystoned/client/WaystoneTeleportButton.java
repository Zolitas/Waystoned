package de.zolitas.waystoned.client;

import de.zolitas.waystoned.data.WaystoneLocation;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.Insets;
import io.wispforest.owo.ui.core.Sizing;
import io.wispforest.owo.ui.core.Surface;
import net.minecraft.network.chat.Component;

public class WaystoneTeleportButton extends FlowLayout {

  public WaystoneTeleportButton(Sizing horizontalSizing, Sizing verticalSizing, WaystoneLocation waystoneLocation) {
    super(horizontalSizing, verticalSizing, Algorithm.VERTICAL);
    padding(Insets.of(10));
    child(createLine(waystoneLocation.getName(), waystoneLocation.getOwnerName()));
    child(createLine(waystoneLocation.getPos().toShortString(), ClientUtils.prettifySnakeCase(waystoneLocation.getDimension().getPath())));

    updateSurface();
    mouseEnter().subscribe(this::updateSurface);
    mouseLeave().subscribe(this::updateSurface);
  }

  private void updateSurface() {
    surface(hovered ? Surface.PANEL : Surface.DARK_PANEL);
  }

  private static FlowLayout createLine(String... texts) {
    FlowLayout line = Containers.horizontalFlow(Sizing.fill(), Sizing.content());
    line.gap(15);
    for (String text : texts) {
      line.child(Components.label(Component.literal(text)));
    }
    return line;
  }
}
