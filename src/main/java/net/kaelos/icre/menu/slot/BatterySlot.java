package net.kaelos.icre.menu.slot;

import net.kaelos.icre.api.energy.EnergyUtils;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.IndexModifier;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;
import org.jspecify.annotations.NonNull;

public class BatterySlot extends ResourceHandlerSlot {
    public enum Mode {
        CHARGE,
        DISCHARGE,
        BOTH
    }

    private final Mode mode;

    public BatterySlot(ItemStacksResourceHandler handler, int index, int xPosition, int yPosition, Mode mode) {
        this(handler, handler::set, index, xPosition, yPosition, mode);
    }

    public BatterySlot(ResourceHandler<ItemResource> handler, IndexModifier<ItemResource> slotModifier,
                       int index, int xPosition, int yPosition, Mode mode) {
        super(handler, slotModifier, index, xPosition, yPosition);
        this.mode = mode;
    }

    @Override
    public boolean mayPlace(@NonNull ItemStack stack) {
        if (!super.mayPlace(stack)) {
            return false;
        }

        return switch (this.mode) {
            case CHARGE -> EnergyUtils.canCharge(stack);
            case DISCHARGE -> EnergyUtils.canDischarge(stack);
            case BOTH -> EnergyUtils.canCharge(stack) || EnergyUtils.canDischarge(stack);
        };
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public int getMaxStackSize(@NonNull ItemStack stack) {
        return 1;
    }
}
