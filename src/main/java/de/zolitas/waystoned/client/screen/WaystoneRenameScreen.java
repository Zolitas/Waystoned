package de.zolitas.waystoned.client.screen;

import de.zolitas.waystoned.network.WaystoneRenameRequestPacket;
import io.wispforest.owo.ui.base.BaseOwoScreen;
import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.component.LabelComponent;
import io.wispforest.owo.ui.component.TextBoxComponent;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.*;
import lombok.AllArgsConstructor;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
public class WaystoneRenameScreen extends BaseOwoScreen<FlowLayout> {
  private final BlockPos waystonePosition;

  @Override
  protected @NotNull OwoUIAdapter<FlowLayout> createAdapter() {
    return OwoUIAdapter.create(this, Containers::verticalFlow);
  }

  @Override
  protected void build(FlowLayout rootComponent) {
    rootComponent
        .surface(Surface.VANILLA_TRANSLUCENT)
        .alignment(HorizontalAlignment.CENTER, VerticalAlignment.CENTER);

    FlowLayout wrapper = Containers.verticalFlow(Sizing.fill(50), Sizing.content());
    wrapper.padding(Insets.of(20)).surface(Surface.flat(new Color(0, 0, 0, 0.5f).argb()));
    rootComponent.child(wrapper);

    LabelComponent label = Components.label(Component.literal("Waystone Name"));
    label.margins(Insets.of(2));
    wrapper.child(label);

    TextBoxComponent textBox = Components.textBox(Sizing.fill());
    wrapper.child(textBox);

    ButtonComponent button = Components.button(Component.translatable("gui.done"), pressedButton -> {
      String value = textBox.getValue();

      if (!value.isBlank()) {
        PacketDistributor.sendToServer(new WaystoneRenameRequestPacket(value, waystonePosition));
      }

      Minecraft.getInstance().setScreen(null);
    });
    button.horizontalSizing(Sizing.fill());
    wrapper.child(button);
  }
}
