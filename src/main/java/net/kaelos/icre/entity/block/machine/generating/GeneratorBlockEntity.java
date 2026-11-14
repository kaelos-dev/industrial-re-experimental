package net.kaelos.icre.entity.block.machine.generating;

import net.kaelos.icre.api.energy.EnergyUtils;
import net.kaelos.icre.api.energy.VoltageTier;
import net.kaelos.icre.block.base.BaseEntityMachineBlock;
import net.kaelos.icre.definitions.ModBlockEntities;
import net.kaelos.icre.entity.block.base.ElectricBlockEntity;
import net.kaelos.icre.entity.block.base.ITickableBlockEntity;
import net.kaelos.icre.menu.machine.generating.GeneratorMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.item.ItemResource;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class GeneratorBlockEntity extends ElectricBlockEntity implements ITickableBlockEntity {
    public static final int SLOT_FUEL = 0;
    public static final int SLOT_CHARGE = 1;
    private static final int ENERGY_PER_TICK = 10;

    private static final String KEY_BURN_TIME = "burn_time";
    private static final String KEY_MAX_BURN_TIME = "max_burn_time";

    private int burnTime = 0;
    private int maxBurnTime = 0;

    public final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> (int) energyStorage.getEnergyStored();
                case 1 -> (int) energyStorage.getMaxEnergyStored();
                case 2 -> burnTime;
                case 3 -> maxBurnTime;
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

    public GeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.GENERATOR_BE.get(), pos, state, 2, 4000, VoltageTier.LV, false, true);
    }

    @Override
    protected boolean isItemValid(int slot, ItemResource resource) {
        if (slot == SLOT_CHARGE) {
            return EnergyUtils.canCharge(resource);
        }
        return super.isItemValid(slot, resource);
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, @NonNull Inventory playerInv, @NonNull Player player) {
        return new GeneratorMenu(containerId, playerInv, this, this.dataAccess);
    }

    @Override
    public void serverTick(ServerLevel level, BlockPos pos, BlockState state) {
        boolean dirty = false;

        if (this.burnTime > 0) {
            this.burnTime--;
            this.energyStorage.generateEnergy(ENERGY_PER_TICK);
            dirty = true;
        }

        if (this.burnTime == 0 && this.energyStorage.getEnergyStored() < this.energyStorage.getMaxEnergyStored()) {
            ItemStack fuel = getStack(SLOT_FUEL);
            if (!fuel.isEmpty()) {
                int fuelBurnTime = fuel.getBurnTime(RecipeType.SMELTING, level.fuelValues());
                if (fuelBurnTime > 0) {
                    this.burnTime = fuelBurnTime;
                    this.maxBurnTime = fuelBurnTime;
                    consumeFuelItem(fuel);
                    dirty = true;
                }
            }
        }

        if (this.energyStorage.getEnergyStored() > 0) {
            if (EnergyUtils.chargeItemFromStorage(this.energyStorage, this.inventory, SLOT_CHARGE)) {
                dirty = true;
            }

            if (this.energyStorage.getEnergyStored() > 0) {
                EnergyUtils.distributeEnergy(level, pos, this.energyStorage);
            }
        }

        boolean working = this.burnTime > 0;
        if (state.hasProperty(BaseEntityMachineBlock.ACTIVE) && state.getValue(BaseEntityMachineBlock.ACTIVE) != working) {
            level.setBlock(pos, state.setValue(BaseEntityMachineBlock.ACTIVE, working), Block.UPDATE_ALL);
        }

        if (dirty) {
            setChanged();
        }
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

    @Override
    protected void saveAdditional(@NonNull ValueOutput output) {
        super.saveAdditional(output);
        output.putInt(KEY_BURN_TIME, this.burnTime);
        output.putInt(KEY_MAX_BURN_TIME, this.maxBurnTime);
    }

    @Override
    protected void loadAdditional(@NonNull ValueInput input) {
        super.loadAdditional(input);
        this.burnTime = input.getIntOr(KEY_BURN_TIME, 0);
        this.maxBurnTime = input.getIntOr(KEY_MAX_BURN_TIME, 0);
    }
}
