package net.kaelos.icre.client.screen.machine.producing;

import net.kaelos.icre.client.screen.base.BaseMachineScreen;
import net.kaelos.icre.menu.machine.producing.ProcessingMachineMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import org.jspecify.annotations.NonNull;

public class ProcessingMachineScreen extends BaseMachineScreen<ProcessingMachineMenu> {
    private final Identifier backgroundTexture;
    private final int progressY;
    private final int progressU;
    private final int progressV;
    private final int progressHeight;

    public ProcessingMachineScreen(
            ProcessingMachineMenu menu,
            Inventory playerInv,
            Component title,
            Identifier backgroundTexture,
            int progressY,
            int progressU,
            int progressV,
            int progressHeight
    ) {
        super(menu, playerInv, title);
        this.backgroundTexture = backgroundTexture;
        this.progressY = progressY;
        this.progressU = progressU;
        this.progressV = progressV;
        this.progressHeight = progressHeight;
    }

    @Override
    protected Identifier getBackgroundTexture() {
        return this.backgroundTexture;
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
                    this.topPos + this.progressY,
                    this.progressU,
                    this.progressV,
                    scaledProgress,
                    this.progressHeight
            );
        }
    }

    @Override
    protected void extractTooltip(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY) {
        super.extractTooltip(guiGraphics, mouseX, mouseY);
        renderEnergyTooltip(guiGraphics, mouseX, mouseY, this.menu.getEnergy(), this.menu.getMaxEnergy(), 145, 17, 14, 52);
    }
}
