package net.kaelos.icre.entity.block.base;

import net.kaelos.icre.api.energy.EuStorage;
import net.kaelos.icre.api.energy.IEuHandlerProvider;
import net.kaelos.icre.api.energy.IEuStorage;
import net.kaelos.icre.api.energy.VoltageTier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public abstract class ElectricBlockEntity extends InventoryBlockEntity implements IEuHandlerProvider {
    public final EuStorage energyStorage;

    public ElectricBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, int inventorySize,
                               long energyCapacity, VoltageTier tier, boolean canReceive, boolean canExtract) {
        super(type, pos, state, inventorySize);
        this.energyStorage = new EuStorage(energyCapacity, tier, canReceive, canExtract, this::setChanged, this);
    }

    @Override
    public @Nullable IEuStorage getEuStorage(@Nullable Direction side) {
        return this.energyStorage;
    }

    @Override
    protected void saveAdditional(@NonNull ValueOutput output) {
        super.saveAdditional(output);
        this.energyStorage.serialize(output);
    }

    @Override
    protected void loadAdditional(@NonNull ValueInput input) {
        super.loadAdditional(input);
        this.energyStorage.deserialize(input);
    }
}
