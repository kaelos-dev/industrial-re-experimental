package net.kaelos.icre.menu.slot;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.FuelValues;
import net.neoforged.neoforge.transfer.IndexModifier;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;
import org.jspecify.annotations.NonNull;

public class FuelSlot extends ResourceHandlerSlot {
    private final FuelValues fuelValues;

    public FuelSlot(Level level, ItemStacksResourceHandler handler, int index, int xPosition, int yPosition) {
        this(level.fuelValues(), handler, handler::set, index, xPosition, yPosition);
    }

    public FuelSlot(FuelValues fuelValues, ItemStacksResourceHandler handler, int index, int xPosition, int yPosition) {
        this(fuelValues, handler, handler::set, index, xPosition, yPosition);
    }

    public FuelSlot(FuelValues fuelValues, ResourceHandler<ItemResource> handler, IndexModifier<ItemResource> slotModifier,
                    int index, int xPosition, int yPosition) {
        super(handler, slotModifier, index, xPosition, yPosition);
        this.fuelValues = fuelValues;
    }

    @Override
    public boolean mayPlace(@NonNull ItemStack stack) {
        return stack.getBurnTime(RecipeType.SMELTING, this.fuelValues) > 0 || stack.is(Items.BUCKET);
    }

    @Override
    public int getMaxStackSize(@NonNull ItemStack stack) {
        return stack.is(Items.BUCKET) ? 1 : super.getMaxStackSize(stack);
    }
}
