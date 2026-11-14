package net.kaelos.icre.api.energy;

import net.kaelos.icre.definitions.ModDataComponents;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;

public class ItemEuStorage implements IEuStorage {
    private final ItemAccess itemAccess;
    private final Item validItem;
    private final DataComponentType<Long> energyComponent;
    private final long capacity;
    private final long maxReceive;
    private final long maxExtract;
    private final VoltageTier tier;

    public ItemEuStorage(ItemAccess itemAccess, Item validItem, long capacity, long maxReceive, long maxExtract, VoltageTier tier) {
        this(itemAccess, validItem, ModDataComponents.ENERGY.get(), capacity, maxReceive, maxExtract, tier);
    }

    public ItemEuStorage(ItemAccess itemAccess, Item validItem, DataComponentType<Long> energyComponent,
                         long capacity, long maxReceive, long maxExtract, VoltageTier tier) {
        this.itemAccess = itemAccess;
        this.validItem = validItem;
        this.energyComponent = energyComponent;
        this.capacity = Math.max(0L, capacity);
        this.maxReceive = Math.max(0L, maxReceive);
        this.maxExtract = Math.max(0L, maxExtract);
        this.tier = tier;
    }

    @Override
    public long receiveEnergy(long amount, VoltageTier incomingTier, boolean simulate) {
        if (!canReceive() || amount <= 0L || !this.tier.canAccept(incomingTier)) {
            return 0L;
        }

        if (this.itemAccess.getAmount() != 1) {
            return 0L;
        }

        ItemResource resource = this.itemAccess.getResource();
        if (!resource.is(this.validItem)) {
            return 0L;
        }

        long stored = readEnergy(resource);
        long received = Math.min(amount, Math.min(this.maxReceive, this.capacity - stored));
        if (received <= 0L) {
            return 0L;
        }

        if (!simulate && writeEnergy(resource, stored + received)) {
            return 0L;
        }

        return received;
    }

    @Override
    public long extractEnergy(long maxExtract, boolean simulate) {
        if (!canExtract() || maxExtract <= 0L) {
            return 0L;
        }

        if (this.itemAccess.getAmount() != 1) {
            return 0L;
        }

        ItemResource resource = this.itemAccess.getResource();
        if (!resource.is(this.validItem)) {
            return 0L;
        }

        long stored = readEnergy(resource);
        long extracted = Math.min(maxExtract, Math.min(this.maxExtract, stored));
        if (extracted <= 0L) {
            return 0L;
        }

        if (!simulate && writeEnergy(resource, stored - extracted)) {
            return 0L;
        }

        return extracted;
    }

    @Override
    public long getEnergyStored() {
        if (this.itemAccess.getAmount() <= 0) {
            return 0L;
        }

        ItemResource resource = this.itemAccess.getResource();
        if (!resource.is(this.validItem)) {
            return 0L;
        }

        return readEnergy(resource);
    }

    @Override
    public long getMaxEnergyStored() {
        return this.itemAccess.getResource().is(this.validItem) ? this.capacity : 0L;
    }

    @Override
    public VoltageTier getTier() {
        return this.tier;
    }

    @Override
    public boolean canReceive() {
        return this.maxReceive > 0L;
    }

    @Override
    public boolean canExtract() {
        return this.maxExtract > 0L;
    }

    private long readEnergy(ItemResource resource) {
        long stored = resource.getOrDefault(this.energyComponent, 0L);
        return Math.clamp(stored, 0L, this.capacity);
    }

    @SuppressWarnings("deprecation")
    private boolean writeEnergy(ItemResource resource, long newEnergy) {
        long clamped = Math.clamp(newEnergy, 0L, this.capacity);
        ItemResource updated = clamped == 0L
                ? resource.without(this.energyComponent)
                : resource.with(this.energyComponent, clamped);

        try (Transaction tx = Transaction.open(Transaction.getCurrentOpenedTransaction())) {
            if (this.itemAccess.exchange(updated, 1, tx) == 1) {
                tx.commit();
                return false;
            }
        }

        return true;
    }
}
