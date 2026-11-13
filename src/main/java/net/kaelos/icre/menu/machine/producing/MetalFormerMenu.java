package net.kaelos.icre.menu.machine.producing;

import net.kaelos.icre.definitions.ModMenuTypes;
import net.kaelos.icre.entity.block.machine.producing.MetalFormerBlockEntity;
import net.kaelos.icre.recipe.metalformer.MetalFormerMode;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import org.jspecify.annotations.NonNull;

public class MetalFormerMenu extends ProcessingMachineMenu {
    public static final int BUTTON_CYCLE_MODE = 0;

    public MetalFormerMenu(int containerId, Inventory playerInv, FriendlyByteBuf extraData) {
        this(containerId, playerInv, getBlockEntityFromBuf(playerInv, extraData), new SimpleContainerData(5));
    }

    public MetalFormerMenu(int containerId, Inventory playerInv, MetalFormerBlockEntity blockEntity, ContainerData data) {
        super(ModMenuTypes.METAL_FORMER_MENU.get(), containerId, playerInv, blockEntity, data);
    }

    public MetalFormerMode getMode() {
        return MetalFormerMode.byId(this.data.get(4));
    }

    @Override
    public boolean clickMenuButton(@NonNull Player player, int id) {
        if (id == BUTTON_CYCLE_MODE && this.blockEntity instanceof MetalFormerBlockEntity former) {
            former.cycleMode();
            return true;
        }

        return super.clickMenuButton(player, id);
    }
}
