package net.kaelos.icre.client.screen.machine.generating;

import net.kaelos.icre.ICRE;
import net.kaelos.icre.client.screen.base.BaseMachineScreen;
import net.kaelos.icre.menu.machine.generating.GeneratorMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import org.jspecify.annotations.NonNull;

public class GeneratorScreen extends BaseMachineScreen<GeneratorMenu> {
    private static final Identifier TEXTURE = ICRE.makeId("textures/gui/generator/generator_gui.png");

    public GeneratorScreen(GeneratorMenu menu, Inventory playerInv, Component title) {
        super(menu, playerInv, title);
    }

    @Override
    protected Identifier getBackgroundTexture() {
        return TEXTURE;
    }

    @Override
    public void extractBackground(@NonNull GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(guiGraphics, mouseX, mouseY, partialTick);

        renderEnergyBar(guiGraphics, this.menu.getScaledEnergy(), 110, 17, 14, 52);

        int scaledBurnTime = this.menu.getScaledBurnTime();
        if (scaledBurnTime > 0) {
            blitWidget(
                    guiGraphics,
                    this.leftPos + 56,
                    this.topPos + 37 + 12 - scaledBurnTime,
                    15,
                    12 - scaledBurnTime,
                    10,
                    scaledBurnTime
            );
        }

        int scaledProgress = this.menu.getScaledProgress();
        if (scaledProgress > 0) {
            blitWidget(
                    guiGraphics,
                    this.leftPos + 83,
                    this.topPos + 37,
                    26,
                    2,
                    scaledProgress,
                    10
            );
        }
    }

    @Override
    protected void extractTooltip(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        super.extractTooltip(graphics, mouseX, mouseY);
        renderEnergyTooltip(graphics, mouseX, mouseY, this.menu.getEnergy(), this.menu.getMaxEnergy(), 110, 17, 14, 52);
    }
}
