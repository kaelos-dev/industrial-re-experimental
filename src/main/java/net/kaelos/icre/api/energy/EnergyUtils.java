package net.kaelos.icre.api.energy;

import net.kaelos.icre.init.ModCapabilities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;

public class EnergyUtils {
    private EnergyUtils() {}

    public static void burnOut(Level level, BlockPos pos) {
        if (level.isClientSide()) {
            return;
        }

        level.removeBlock(pos, false);
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(
                    ParticleTypes.LARGE_SMOKE,
                    pos.getX() + 0.5D,
                    pos.getY() + 0.5D,
                    pos.getZ() + 0.5D,
                    10,
                    0.2D,0.2D, 0.2D,
                    0.02D
            );

            serverLevel.playSound(
                    null,
                    pos,
                    SoundEvents.REDSTONE_TORCH_BURNOUT,
                    SoundSource.BLOCKS,
                    1.0F,
                    1.0F
            );
        }
    }

    public static void distributeEnergy(Level level, BlockPos sourcePos, EuStorage sourceStorage) {
        if (level.isClientSide() || sourceStorage.getEnergyStored() <= 0) {
            return;
        }

        long available = Math.min(sourceStorage.getEnergyStored(), sourceStorage.getTier().getMaxVoltage());
        if (available <= 0) {
            return;
        }

        for (Direction direction : Direction.values()) {
            BlockPos neighborPos = sourcePos.relative(direction);
            IEuStorage neighbor = level.getCapability(ModCapabilities.EU_CAPABILITY, neighborPos, direction.getOpposite());
            if (neighbor == null || !neighbor.canReceive()) {
                continue;
            }

            long accepted = neighbor.receiveEnergy(available, sourceStorage.getTier(), false);
            if (accepted > 0) {
                sourceStorage.consumeEnergy(accepted);
                available -= accepted;
                if (available <= 0) {
                    break;
                }
            }
        }
    }

    public static IEuStorage getItemEuStorage(ItemStack stack) {
        if (stack.isEmpty()) {
            return null;
        }
        return ItemAccess.forStack(stack).getCapability(ModCapabilities.EU_ITEM_CAPABILITY);
    }

    public static IEuStorage getItemEuStorage(ItemResource resource) {
        if (resource.isEmpty()) {
            return null;
        }
        return getItemEuStorage(resource.toStack());
    }

    public static IEuStorage getItemEuStorage(ItemStacksResourceHandler inv, int slot) {
        if (inv.getResource(slot).isEmpty() || inv.getAmountAsInt(slot) <= 0) {
            return null;
        }
        return ItemAccess.forHandlerIndex(inv, slot).getCapability(ModCapabilities.EU_ITEM_CAPABILITY);
    }

    public static boolean canCharge(ItemStack stack) {
        IEuStorage storage = getItemEuStorage(stack);
        return storage != null && storage.canReceive();
    }

    public static boolean canCharge(ItemResource resource) {
        IEuStorage storage = getItemEuStorage(resource);
        return storage != null && storage.canReceive();
    }

    public static boolean canDischarge(ItemStack stack) {
        IEuStorage storage = getItemEuStorage(stack);
        return storage != null && storage.canExtract();
    }

    public static boolean canDischarge(ItemResource resource, VoltageTier targetTier) {
        IEuStorage storage = getItemEuStorage(resource);
        return storage != null && storage.canExtract() && targetTier.canAccept(storage.getTier());
    }

    public static boolean chargeItemFromStorage(EuStorage source, ItemStacksResourceHandler inventory, int slot) {
        if (source.getEnergyStored() <= 0L) {
            return false;
        }

        IEuStorage itemEnergy = getItemEuStorage(inventory, slot);
        if (itemEnergy == null || !itemEnergy.canReceive()) {
            return false;
        }

        long maxOffer = Math.min(source.getEnergyStored(), source.getTier().getMaxVoltage());
        long accepted = itemEnergy.receiveEnergy(maxOffer, source.getTier(), false);
        if (accepted > 0L) {
            source.consumeEnergy(accepted);
            return true;
        }

        return false;
    }

    public static boolean dischargeItemIntoStorage(EuStorage target, ItemStacksResourceHandler inventory, int slot) {
        if (!target.canReceive()) {
            return false;
        }

        long space = target.getMaxEnergyStored() - target.getEnergyStored();
        if (space <= 0L) {
            return false;
        }

        IEuStorage itemEnergy = getItemEuStorage(inventory, slot);
        if (itemEnergy == null || !itemEnergy.canExtract() || !target.getTier().canAccept(itemEnergy.getTier())) {
            return false;
        }

        long maxPull = Math.min(space, target.getTier().getMaxVoltage());
        long available = itemEnergy.extractEnergy(maxPull, true);
        if (available <= 0L) {
            return false;
        }

        long accepted = target.receiveEnergy(available, itemEnergy.getTier(), false);
        if (accepted > 0L) {
            itemEnergy.extractEnergy(accepted, false);
            return true;
        }

        return false;
    }
}
