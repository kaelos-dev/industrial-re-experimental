package net.kaelos.icre.entity.block.machine.producing;

import net.kaelos.icre.api.energy.VoltageTier;
import net.kaelos.icre.definitions.ModBlockEntities;
import net.kaelos.icre.definitions.ModMenuTypes;
import net.kaelos.icre.menu.machine.producing.ProcessingMachineMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class ElectricFurnaceBlockEntity extends AbstractProcessingMachineBlockEntity<SingleRecipeInput, SmeltingRecipe> {
    public ElectricFurnaceBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ELECTRIC_FURNACE_BE.get(), pos, state, 400, VoltageTier.LV, 100, 2, RecipeType.SMELTING);
    }

    @Override
    protected SingleRecipeInput createRecipeInput(ItemStack inputStack) {
        return new SingleRecipeInput(inputStack);
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, @NonNull Inventory playerInv, @NonNull Player player) {
        return new ProcessingMachineMenu(ModMenuTypes.ELECTRIC_FURNACE_MENU.get(), containerId, playerInv, this, this.dataAccess);
    }
}
