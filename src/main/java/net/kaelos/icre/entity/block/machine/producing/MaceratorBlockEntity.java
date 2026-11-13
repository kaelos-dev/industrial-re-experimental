package net.kaelos.icre.entity.block.machine.producing;

import net.kaelos.icre.api.energy.VoltageTier;
import net.kaelos.icre.definitions.ModBlockEntities;
import net.kaelos.icre.definitions.ModMenuTypes;
import net.kaelos.icre.definitions.ModRecipes;
import net.kaelos.icre.menu.machine.producing.ProcessingMachineMenu;
import net.kaelos.icre.recipe.MaceratorRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class MaceratorBlockEntity extends AbstractProcessingMachineBlockEntity<SingleRecipeInput, MaceratorRecipe> {
    public MaceratorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MACERATOR_BE.get(), pos, state, 400, VoltageTier.LV, 100, 2, ModRecipes.MACERATOR.get());
    }

    @Override
    protected SingleRecipeInput createRecipeInput(ItemStack inputStack) {
        return new SingleRecipeInput(inputStack);
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, @NonNull Inventory playerInv, @NonNull Player player) {
        return new ProcessingMachineMenu(ModMenuTypes.MACERATOR_MENU.get(), containerId, playerInv, this, this.dataAccess);
    }
}
