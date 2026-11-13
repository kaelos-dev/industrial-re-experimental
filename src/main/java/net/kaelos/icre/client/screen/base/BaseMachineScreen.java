package net.kaelos.icre.client.screen.base;

import net.kaelos.icre.ICRE;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jspecify.annotations.NonNull;

public abstract class BaseMachineScreen<M extends AbstractContainerMenu> extends AbstractContainerScreen<M> {
    protected static final Identifier WIDGETS = ICRE.makeId("textures/gui/widgets.png");

    protected BaseMachineScreen(M menu, Inventory playerInv, Component title) {
        super(menu, playerInv, title);
    }

    protected abstract Identifier getBackgroundTexture();

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = 8;
        this.titleLabelY = 6;
        this.inventoryLabelX = 8;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    public void extractRenderState(@NonNull GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(guiGraphics, mouseX, mouseY, partialTick);
        this.extractTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    public void extractBackground(@NonNull GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.blit(
                RenderPipelines.GUI_TEXTURED,
                getBackgroundTexture(),
                this.leftPos,
                this.topPos,
                0.0F,
                0.0F,
                this.imageWidth,
                this.imageHeight,
                256,
                256
        );
    }

    @Override
    protected void extractLabels(@NonNull GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY) {}

    protected void blitWidget(GuiGraphicsExtractor guiGraphics, int x, int y, int u, int v, int width, int height) {
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, WIDGETS, x, y, (float) u, (float) v, width, height, 256, 256);
    }

    protected void renderEnergyBar(GuiGraphicsExtractor guiGraphics, int scaledEnergy,
                                   int energyBarPosX, int energyBarPosY, int energyBarWidth, int energyBarHeight) {
        if (scaledEnergy > 0) {
            blitWidget(
                    guiGraphics,
                    this.leftPos + energyBarPosX,
                    this.topPos + energyBarPosY + (energyBarHeight - scaledEnergy),
                    0,
                    energyBarHeight - scaledEnergy,
                    energyBarWidth,
                    scaledEnergy
            );
        }
    }

    protected void renderEnergyTooltip(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, int energy, int maxEnergy,
                                       int energyBarPosX, int energyBarPosY, int energyBarWidth, int energyBarHeight) {
        if (isHovering(energyBarPosX, energyBarPosY, energyBarWidth, energyBarHeight, mouseX, mouseY)) {
            Component tooltip = Component.literal(energy + " / " + maxEnergy + " EU");
            guiGraphics.setTooltipForNextFrame(this.font, tooltip, mouseX, mouseY);
        }
    }
}
