package net.kaelos.icre.entity.block.machine.producing;

import net.kaelos.icre.api.energy.VoltageTier;
import net.kaelos.icre.definitions.ModBlockEntities;
import net.kaelos.icre.definitions.ModRecipes;
import net.kaelos.icre.menu.machine.producing.MetalFormerMenu;
import net.kaelos.icre.recipe.metalformer.MetalFormerInput;
import net.kaelos.icre.recipe.metalformer.MetalFormerMode;
import net.kaelos.icre.recipe.metalformer.MetalFormerRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class MetalFormerBlockEntity extends AbstractProcessingMachineBlockEntity<MetalFormerInput, MetalFormerRecipe> {
    private static final String KEY_MODE = "mode";

    private MetalFormerMode mode = MetalFormerMode.ROLLING;

    public final ContainerData formerData = new ContainerData() {
        @Override
        public int get(int index) {
            return index == 4 ? mode.ordinal() : dataAccess.get(index);
        }

        @Override
        public void set(int index, int value) {}

        @Override
        public int getCount() {
            return 5;
        }
    };

    public MetalFormerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.METAL_FORMER_BE.get(), pos, state, 400, VoltageTier.LV, 200, 10, ModRecipes.METAL_FORMER.get());
    }

    @Override
    protected MetalFormerInput createRecipeInput(ItemStack inputStack) {
        return new MetalFormerInput(inputStack, this.mode);
    }

    public void cycleMode() {
        this.mode = this.mode.next();
        this.progress = 0;
        setChanged();
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, @NonNull Inventory playerInv, @NonNull Player player) {
        return new MetalFormerMenu(containerId, playerInv, this, this.formerData);
    }

    @Override
    protected void saveAdditional(@NonNull ValueOutput output) {
        super.saveAdditional(output);
        output.putInt(KEY_MODE, this.mode.ordinal());
    }

    @Override
    protected void loadAdditional(@NonNull ValueInput input) {
        super.loadAdditional(input);
        this.mode = MetalFormerMode.byId(input.getIntOr(KEY_MODE, 0));
    }
}
