package net.kaelos.icre.client.screen.machine.producing;

import net.kaelos.icre.ICRE;
import net.kaelos.icre.client.screen.base.BaseMachineScreen;
import net.kaelos.icre.client.widget.ModeButton;
import net.kaelos.icre.menu.machine.producing.MetalFormerMenu;
import net.kaelos.icre.recipe.metalformer.MetalFormerMode;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import org.jspecify.annotations.NonNull;

public class MetalFormerScreen extends BaseMachineScreen<MetalFormerMenu> {
    private static final Identifier TEXTURE = ICRE.makeId("textures/gui/producing/metal_former_gui.png");

    private Button modeButton;
    private MetalFormerMode lastMode;

    public MetalFormerScreen(MetalFormerMenu menu, Inventory playerInv, Component title) {
        super(menu, playerInv, title);
    }

    @Override
    protected void init() {
        super.init();
        this.modeButton = this.addRenderableWidget(new ModeButton(
                this.leftPos + 75,
                this.topPos + 52,
                this.menu::getMode,
                _ -> {
                    if (this.minecraft.gameMode != null) {
                        this.minecraft.gameMode.handleInventoryButtonClick(
                                this.menu.containerId,
                                MetalFormerMenu.BUTTON_CYCLE_MODE
                        );
                    }
                }
        ));

        syncModeButton(true);
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        syncModeButton(false);
    }

    private void syncModeButton(boolean force) {
        MetalFormerMode current = this.menu.getMode();
        if (force || current != this.lastMode) {
            this.lastMode = current;
            this.modeButton.setMessage(current.getDisplayName());
            this.modeButton.setTooltip(Tooltip.create(current.getDisplayName()));
        }
    }

    @Override
    protected Identifier getBackgroundTexture() {
        return TEXTURE;
    }

    @Override
    public void extractBackground(@NonNull GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(guiGraphics, mouseX, mouseY, partialTick);
        renderEnergyBar(guiGraphics, this.menu.getScaledEnergy(), 145, 17, 14, 52);

        int scaledProgress = this.menu.getScaledProgress();
        if (scaledProgress > 0) {
            blitWidget(
                    guiGraphics,
                    this.leftPos + 74,
                    this.topPos + 35,
                    26,
                    37,
                    scaledProgress,
                    14
            );
        }
    }

    @Override
    protected void extractTooltip(@NonNull GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY) {
        super.extractTooltip(guiGraphics, mouseX, mouseY);
        renderEnergyTooltip(guiGraphics, mouseX, mouseY, this.menu.getEnergy(), this.menu.getMaxEnergy(), 145, 17, 14, 52);
    }
}
