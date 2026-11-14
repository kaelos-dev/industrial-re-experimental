package net.kaelos.icre.client.screen.machine.producing;

import net.kaelos.icre.client.screen.base.BaseMachineScreen;
import net.kaelos.icre.menu.machine.producing.IronFurnaceMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import org.jspecify.annotations.NonNull;

public class IronFurnaceScreen extends BaseMachineScreen<IronFurnaceMenu> {
    private static final Identifier TEXTURE = Identifier.withDefaultNamespace("textures/gui/container/furnace.png");
    private static final Identifier LIT_PROGRESS_SPRITE = Identifier.withDefaultNamespace("container/furnace/lit_progress");
    private static final Identifier BURN_PROGRESS_SPRITE = Identifier.withDefaultNamespace("container/furnace/burn_progress");
    private static final int LABEL_COLOR = 0xFF404040;

    public IronFurnaceScreen(IronFurnaceMenu menu, Inventory playerInv, Component title) {
        super(menu, playerInv, title);
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    @Override
    protected Identifier getBackgroundTexture() {
        return TEXTURE;
    }

    @Override
    public void extractBackground(@NonNull GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(guiGraphics, mouseX, mouseY, partialTick);

        if (this.menu.isLit()) {
            int litHeight = Mth.clamp(Mth.ceil(this.menu.getLitProgress() * 13.0F) + 1, 1, 14);
            guiGraphics.blitSprite(
                    RenderPipelines.GUI_TEXTURED,
                    LIT_PROGRESS_SPRITE,
                    14,
                    14,
                    0,
                    14 - litHeight,
                    this.leftPos + 56,
                    this.topPos + 36 + 14 - litHeight,
                    14,
                    litHeight
            );
        }

        int burnWidth = Mth.clamp(Mth.ceil(this.menu.getBurnProgress() * 24.0F), 0, 24);
        if (burnWidth > 0) {
            guiGraphics.blitSprite(
                    RenderPipelines.GUI_TEXTURED,
                    BURN_PROGRESS_SPRITE,
                    24,
                    16,
                    0,
                    0,
                    this.leftPos + 79,
                    this.topPos + 34,
                    burnWidth,
                    16
            );
        }
    }

    @Override
    protected void extractLabels(@NonNull GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY) {
        guiGraphics.text(this.font, this.title, this.titleLabelX, this.titleLabelY, LABEL_COLOR, false);
        guiGraphics.text(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, LABEL_COLOR, false);
    }
}
