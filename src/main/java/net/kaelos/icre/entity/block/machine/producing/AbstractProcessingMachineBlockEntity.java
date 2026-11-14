package net.kaelos.icre.entity.block.machine.producing;

import net.kaelos.icre.api.energy.EnergyUtils;
import net.kaelos.icre.api.energy.VoltageTier;
import net.kaelos.icre.block.base.BaseEntityMachineBlock;
import net.kaelos.icre.entity.block.base.ElectricBlockEntity;
import net.kaelos.icre.entity.block.base.ITickableBlockEntity;
import net.kaelos.icre.recipe.ICountedRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.item.ItemResource;
import org.jspecify.annotations.NonNull;

import java.util.Optional;

public abstract class AbstractProcessingMachineBlockEntity<I extends RecipeInput, R extends Recipe<I>> extends ElectricBlockEntity implements ITickableBlockEntity {
    public static final int SLOT_INPUT = 0;
    public static final int SLOT_OUTPUT = 1;
    public static final int SLOT_BATTERY = 2;

    private static final String KEY_PROGRESS = "progress";

    protected int progress = 0;
    protected final int maxProgress;
    protected final int energyPerTick;

    private final RecipeManager.CachedCheck<I, R> quickCheck;

    protected abstract I createRecipeInput(ItemStack inputStack);

    public final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> (int) energyStorage.getEnergyStored();
                case 1 -> (int) energyStorage.getMaxEnergyStored();
                case 2 -> progress;
                case 3 -> maxProgress;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {}

        @Override
        public int getCount() {
            return 4;
        }
    };

    protected AbstractProcessingMachineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state,
                                                   long energyCapacity, VoltageTier tier, int maxProgress, int energyPerTick, RecipeType<R> recipeType) {
        super(type, pos, state, 3, energyCapacity, tier, true, false);
        this.maxProgress = maxProgress;
        this.energyPerTick = energyPerTick;
        this.quickCheck = RecipeManager.createCheck(recipeType);
    }

    @Override
    protected boolean isItemValid(int slot, ItemResource resource) {
        if (slot == SLOT_OUTPUT) {
            return false;
        }

        if (slot == SLOT_BATTERY) {
            return EnergyUtils.canDischarge(resource, this.energyStorage.getTier());
        }

        return super.isItemValid(slot, resource);
    }

    @Override
    public void serverTick(ServerLevel level, BlockPos pos, BlockState state) {
        boolean dirty = EnergyUtils.dischargeItemIntoStorage(this.energyStorage, this.inventory, SLOT_BATTERY);
        ItemStack inputStack = getStack(SLOT_INPUT);

        if (!inputStack.isEmpty()) {
            I recipeInput = createRecipeInput(inputStack);
            Optional<RecipeHolder<R>> recipe = this.quickCheck.getRecipeFor(recipeInput, level);

            if (recipe.isPresent()) {
                R matches = recipe.get().value();
                int consumeCount = matches instanceof ICountedRecipe counted ? counted.inputCount() : 1;
                ItemStack result = matches.assemble(recipeInput);

                if (inputStack.getCount() >= consumeCount && !result.isEmpty() && canInsertIntoOutputSlot(result)) {
                    if (this.energyStorage.consumeEnergy(this.energyPerTick)) {
                        this.progress++;
                        dirty = true;

                        if (this.progress >= this.maxProgress) {
                            craftItem(inputStack, result, consumeCount);
                            this.progress = 0;
                        }
                    }
                } else if (resetProgress()) {
                    dirty = true;
                }
            } else if (resetProgress()) {
                dirty = true;
            }
        } else if (resetProgress()) {
            dirty = true;
        }

        boolean working = this.progress > 0;
        if (state.hasProperty(BaseEntityMachineBlock.ACTIVE) && state.getValue(BaseEntityMachineBlock.ACTIVE) != working) {
            level.setBlock(pos, state.setValue(BaseEntityMachineBlock.ACTIVE, working), 3);
        }

        if (dirty) {
            setChanged();
        }
    }

    private boolean resetProgress() {
        if (this.progress > 0) {
            this.progress = 0;
            return true;
        }

        return false;
    }

    private boolean canInsertIntoOutputSlot(ItemStack result) {
        ItemStack outputStack = getStack(SLOT_OUTPUT);
        if (outputStack.isEmpty()) {
            return true;
        }

        if (!ItemStack.isSameItemSameComponents(outputStack, result)) {
            return false;
        }

        int maxAllowed = Math.min(outputStack.getMaxStackSize(), this.inventory.getCapacityAsInt(SLOT_OUTPUT, ItemResource.EMPTY));
        return outputStack.getCount() + result.getCount() <= maxAllowed;
    }

    private void craftItem(ItemStack inputStack, ItemStack result, int consumeCount) {
        inputStack.shrink(consumeCount);
        setStack(SLOT_INPUT, inputStack);

        ItemStack outputStack = getStack(SLOT_OUTPUT);
        if (outputStack.isEmpty()) {
            setStack(SLOT_OUTPUT, result.copy());
        } else {
            outputStack.grow(result.getCount());
            setStack(SLOT_OUTPUT, outputStack);
        }
    }

    @Override
    protected void saveAdditional(@NonNull ValueOutput output) {
        super.saveAdditional(output);
        output.putInt(KEY_PROGRESS, this.progress);
    }

    @Override
    protected void loadAdditional(@NonNull ValueInput input) {
        super.loadAdditional(input);
        this.progress = input.getIntOr(KEY_PROGRESS, 0);
    }
}
