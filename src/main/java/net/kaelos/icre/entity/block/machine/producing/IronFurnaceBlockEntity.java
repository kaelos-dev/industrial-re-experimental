package net.kaelos.icre.entity.block.machine.producing;

import net.kaelos.icre.block.base.BaseEntityMachineBlock;
import net.kaelos.icre.definitions.ModBlockEntities;
import net.kaelos.icre.entity.block.base.ITickableBlockEntity;
import net.kaelos.icre.entity.block.base.InventoryBlockEntity;
import net.kaelos.icre.menu.machine.producing.IronFurnaceMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.item.ItemResource;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public class IronFurnaceBlockEntity extends InventoryBlockEntity implements ITickableBlockEntity {
    public static final int SLOT_INPUT = 0;
    public static final int SLOT_FUEL = 1;
    public static final int SLOT_OUTPUT = 2;

    private static final int DEFAULT_COOK_TIME = 160;
    private static final String KEY_BURN_TIME = "burn_time";
    private static final String KEY_BURN_DURATION = "burn_duration";
    private static final String KEY_COOK_PROGRESS = "cook_progress";
    private static final String KEY_COOK_TOTAL_TIME = "cook_total_time";

    private int burnTime = 0;
    private int burnDuration = 0;
    private int cookProgress = 0;
    private int cookTotalTime = DEFAULT_COOK_TIME;

    private final RecipeManager.CachedCheck<SingleRecipeInput, SmeltingRecipe> quickCheck =
            RecipeManager.createCheck(RecipeType.SMELTING);

    public final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> burnTime;
                case 1 -> burnDuration;
                case 2 -> cookProgress;
                case 3 -> cookTotalTime;
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

    public IronFurnaceBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.IRON_FURNACE_BE.get(), pos, state, 3);
    }

    @Override
    protected boolean isItemValid(int slot, ItemResource resource) {
        if (slot == SLOT_OUTPUT) {
            return false;
        }

        if (slot == SLOT_FUEL && this.level != null) {
            return resource.is(Items.BUCKET) || resource.toStack().getBurnTime(RecipeType.SMELTING, this.level.fuelValues()) > 0;
        }

        return super.isItemValid(slot, resource);
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, @NonNull Inventory playerInv, @NonNull Player player) {
        return new IronFurnaceMenu(containerId, playerInv, this, this.dataAccess);
    }

    @Override
    public void serverTick(ServerLevel level, BlockPos pos, BlockState state) {
        boolean wasLit = this.burnTime > 0;
        boolean dirty = false;

        if (this.burnTime > 0) {
            this.burnTime--;
            dirty = true;
        }

        ItemStack inputStack = getStack(SLOT_INPUT);
        ItemStack fuelStack = getStack(SLOT_FUEL);
        boolean hasInput = !inputStack.isEmpty();
        boolean hasFuel = !fuelStack.isEmpty();

        if (this.burnTime > 0 || (hasInput && hasFuel)) {
            SmeltingRecipe recipe = null;
            ItemStack result = ItemStack.EMPTY;
            boolean canSmelt = false;

            if (hasInput) {
                SingleRecipeInput recipeInput = new SingleRecipeInput(inputStack);
                Optional<RecipeHolder<SmeltingRecipe>> holder = this.quickCheck.getRecipeFor(recipeInput, level);
                if (holder.isPresent()) {
                    recipe = holder.get().value();
                    result = recipe.assemble(recipeInput);
                    canSmelt = !result.isEmpty() && canInsertIntoOutputSlot(result);
                }
            }

            if (this.burnTime == 0 && canSmelt && hasFuel) {
                int fuelBurnTime = fuelStack.getBurnTime(RecipeType.SMELTING, level.fuelValues());
                if (fuelBurnTime > 0) {
                    this.burnTime = fuelBurnTime;
                    this.burnDuration = fuelBurnTime;
                    consumeFuelItem(fuelStack);
                    dirty = true;
                }
            }

            if (this.burnTime > 0 && canSmelt) {
                this.cookTotalTime = getCookTime(recipe);
                this.cookProgress++;
                dirty = true;

                if (this.cookProgress >= this.cookTotalTime) {
                    this.cookProgress = 0;
                    craftItem(inputStack, result);
                }
            } else if (!canSmelt) {
                if (this.cookProgress > 0) {
                    this.cookProgress = 0;
                    dirty = true;
                }
            } else if (this.cookProgress > 0) {
                this.cookProgress = Math.max(0, this.cookProgress - 2);
                dirty = true;
            }
        } else if (this.cookProgress > 0) {
            this.cookProgress = Math.max(0, this.cookProgress - 2);
            dirty = true;
        }

        boolean isLit = this.burnTime > 0;
        if (wasLit != isLit && state.hasProperty(BaseEntityMachineBlock.ACTIVE) && state.getValue(BaseEntityMachineBlock.ACTIVE) != isLit) {
            level.setBlock(pos, state.setValue(BaseEntityMachineBlock.ACTIVE, isLit), Block.UPDATE_ALL);
        }

        if (dirty) {
            setChanged();
        }
    }

    private static int getCookTime(SmeltingRecipe recipe) {
        return Math.max(1, recipe.cookingTime() * 4 / 5);
    }

    @SuppressWarnings("deprecation")
    private void consumeFuelItem(ItemStack fuel) {
        ItemStackTemplate remainder = fuel.getItem().getCraftingRemainder();
        fuel.shrink(1);
        if (fuel.isEmpty() && remainder != null) {
            setStack(SLOT_FUEL, remainder.create());
        } else {
            setStack(SLOT_FUEL, fuel);
        }
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

    private void craftItem(ItemStack inputStack, ItemStack result) {
        inputStack.shrink(1);
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
        output.putInt(KEY_BURN_TIME, this.burnTime);
        output.putInt(KEY_BURN_DURATION, this.burnDuration);
        output.putInt(KEY_COOK_PROGRESS, this.cookProgress);
        output.putInt(KEY_COOK_TOTAL_TIME, this.cookTotalTime);
    }

    @Override
    protected void loadAdditional(@NonNull ValueInput input) {
        super.loadAdditional(input);
        this.burnTime = input.getIntOr(KEY_BURN_TIME, 0);
        this.burnDuration = input.getIntOr(KEY_BURN_DURATION, 0);
        this.cookProgress = input.getIntOr(KEY_COOK_PROGRESS, 0);
        this.cookTotalTime = input.getIntOr(KEY_COOK_TOTAL_TIME, DEFAULT_COOK_TIME);
    }
}
