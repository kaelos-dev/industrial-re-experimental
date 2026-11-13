package net.kaelos.icre.api.energy;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;
import org.jspecify.annotations.NonNull;

import javax.annotation.Nullable;

public class EuStorage implements IEuStorage, ValueIOSerializable {
    private static final String KEY_ENERGY = "energy";

    private long energy;
    private final long capacity;
    private final VoltageTier machineTier;
    private final boolean canReceive;
    private final boolean canExtract;
    private final Runnable onContentsChanged;
    private final @Nullable BlockEntity blockEntity;

    public EuStorage(long capacity, VoltageTier machineTier, boolean canReceive, boolean canExtract, Runnable onContentsChanged, @Nullable BlockEntity blockEntity) {
        this.capacity = capacity;
        this.machineTier = machineTier;
        this.canReceive = canReceive;
        this.canExtract = canExtract;
        this.onContentsChanged = onContentsChanged;
        this.blockEntity = blockEntity;
    }

    public static EuStorage receiver(long capacity, VoltageTier tier, Runnable onChanged, BlockEntity owner) {
        return new EuStorage(capacity, tier, true, false, onChanged, owner);
    }

    public static EuStorage producer(long capacity, VoltageTier tier, Runnable onChanged, BlockEntity owner) {
        return new EuStorage(capacity, tier, false, true, onChanged, owner);
    }

    public static EuStorage buffer(long capacity, VoltageTier tier, Runnable onChanged, BlockEntity owner) {
        return new EuStorage(capacity, tier, true, true, onChanged, owner);
    }

    @Override
    public long receiveEnergy(long amount, VoltageTier tier, boolean simulate) {
        if (!this.canReceive || amount <= 0) {
            return 0;
        }

        if (!this.machineTier.canAccept(tier)) {
            if (simulate) {
                return amount;
            }

            if (this.blockEntity != null && this.blockEntity.getLevel() != null) {
                EnergyUtils.burnOut(this.blockEntity.getLevel(), this.blockEntity.getBlockPos());
            }

            return 0;
        }

        long energyReceived = Math.min(this.capacity - this.energy, amount);
        if (!simulate && energyReceived > 0) {
            this.energy += energyReceived;
            this.onContentsChanged.run();
        }

        return energyReceived;
    }

    @Override
    public long extractEnergy(long maxExtract, boolean simulate) {
        if (!this.canExtract || maxExtract <= 0) {
            return 0;
        }

        return extractInternal(maxExtract, simulate);
    }

    public boolean consumeEnergy(long amount) {
        if (amount <= 0) {
            return true;
        }

        if (this.energy < amount) {
            return false;
        }

        this.energy -= amount;
        this.onContentsChanged.run();
        return true;
    }

    public long generateEnergy(long amount) {
        if (amount <= 0) {
            return 0;
        }

        long added = Math.min(this.capacity - this.energy, amount);
        if (added > 0) {
            this.energy += added;
            this.onContentsChanged.run();
        }

        return added;
    }

    private long extractInternal(long maxExtract, boolean simulate) {
        long extracted = Math.min(this.energy, maxExtract);
        if (!simulate && extracted > 0) {
            this.energy -= extracted;
            this.onContentsChanged.run();
        }

        return extracted;
    }

    @Override
    public boolean canReceive() {
        return this.canReceive;
    }

    @Override
    public boolean canExtract() {
        return this.canExtract;
    }

    @Override
    public long getEnergyStored() {
        return this.energy;
    }

    @Override
    public long getMaxEnergyStored() {
        return this.capacity;
    }

    @Override
    public VoltageTier getTier() {
        return this.machineTier;
    }

    public void setEnergy(long energy) {
        this.energy = Math.clamp(energy, 0L, this.capacity);
    }

    @Override
    public void serialize(@NonNull ValueOutput output) {
        output.putLong(KEY_ENERGY, this.energy);
    }

    @Override
    public void deserialize(@NonNull ValueInput input) {
        setEnergy(input.getLongOr(KEY_ENERGY, 0L));
    }
}
