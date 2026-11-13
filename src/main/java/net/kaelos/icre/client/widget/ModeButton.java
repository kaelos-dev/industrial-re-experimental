package net.kaelos.icre.client.widget;

import net.kaelos.icre.ICRE;
import net.kaelos.icre.recipe.metalformer.MetalFormerMode;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

import java.util.function.Supplier;

public class ModeButton extends Button {
    private static final Identifier BUTTON_IDLE = ICRE.makeId("button_idle");
    private static final Identifier BUTTON_HOVER = ICRE.makeId("button_hover");
    private static final Identifier BUTTON_PRESSED = ICRE.makeId("button_pressed");

    private static final Identifier ICON_ROLLING = ICRE.makeId("icon_rolling");
    private static final Identifier ICON_CUTTING = ICRE.makeId("icon_cutting");
    private static final Identifier ICON_EXTRUDING = ICRE.makeId("icon_extruding");

    private final Supplier<MetalFormerMode> modeSupplier;
    private boolean pressed;

    public ModeButton(int x, int y, Supplier<MetalFormerMode> modeSupplier, OnPress onPress) {
        super(x, y, 18, 18, Component.empty(), onPress, DEFAULT_NARRATION);
        this.modeSupplier = modeSupplier;
    }

    @Override
    public void onClick(@NonNull MouseButtonEvent event, boolean doubleClick) {
        this.pressed = true;
        super.onClick(event, doubleClick);
    }

    @Override
    public void onRelease(@NonNull MouseButtonEvent event) {
        this.pressed = false;
        super.onRelease(event);
    }

    @Override
    protected void extractContents(@NonNull GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (this.pressed && !this.isHovered()) {
            this.pressed = false;
        }

        Identifier background = this.pressed
                ? BUTTON_PRESSED
                : (this.isHovered() ? BUTTON_HOVER : BUTTON_IDLE);
        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, background, this.getX(), this.getY(), 18, 18);

        Identifier icon = switch (this.modeSupplier.get()) {
            case ROLLING -> ICON_ROLLING;
            case CUTTING -> ICON_CUTTING;
            case EXTRUDING -> ICON_EXTRUDING;
        };
        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, icon, this.getX() + 1, this.getY() + 1, 16, 16);
    }
}
