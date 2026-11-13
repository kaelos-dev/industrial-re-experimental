package net.kaelos.icre.entity.block.electric;

import net.kaelos.icre.api.energy.EnergyUtils;
import net.kaelos.icre.api.energy.IEuHandlerProvider;
import net.kaelos.icre.api.energy.IEuStorage;
import net.kaelos.icre.api.energy.VoltageTier;
import net.kaelos.icre.block.cable.CableBlock;
import net.kaelos.icre.definitions.ModBlockEntities;
import net.kaelos.icre.init.ModCapabilities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

import java.util.*;

public class CableBlockEntity extends BlockEntity implements IEuHandlerProvider {
    private static final int MAX_NETWORK_SCAN = 256;
    private static final int ELECTRIFIED_GRACE_TICKS = 10;

    private final VoltageTier maxTier;
    private long lastActiveTick = -1000L;

    public CableBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CABLE_BE.get(), pos, state);
        this.maxTier = state.getBlock() instanceof CableBlock cable ? cable.getTier() : VoltageTier.LV;
    }

    public boolean isElectrified() {
        return this.level != null && this.level.getGameTime() - this.lastActiveTick <= ELECTRIFIED_GRACE_TICKS;
    }

    private final IEuStorage energyHandler = new IEuStorage() {
        @Override
        public long receiveEnergy(long amount, VoltageTier tier, boolean simulate) {
            if (level == null || level.isClientSide() || amount <= 0) {
                return 0;
            }

            if (!maxTier.canAccept(tier)) {
                if (!simulate) {
                    EnergyUtils.burnOut(level, worldPosition);
                }

                return 0;
            }

            long distributed = distributeEnergyToNetwork(amount, tier, simulate);
            if (!simulate && distributed > 0) {
                lastActiveTick = level.getGameTime();
            }

            return distributed;
        }

        @Override
        public long extractEnergy(long maxExtract, boolean simulate) {
            return 0;
        }

        @Override
        public boolean canExtract() {
            return false;
        }

        @Override
        public long getEnergyStored() {
            return 0;
        }

        @Override
        public long getMaxEnergyStored() {
            return 0;
        }

        @Override
        public VoltageTier getTier() {
            return maxTier;
        }
    };

    @Override
    public @Nullable IEuStorage getEuStorage(@Nullable Direction side) {
        return this.energyHandler;
    }

    private long distributeEnergyToNetwork(long amount, VoltageTier tier, boolean simulate) {
        if (this.level == null) {
            return 0;
        }

        Queue<BlockPos> queue = new ArrayDeque<>();
        Set<BlockPos> visited = new HashSet<>();
        List<IEuStorage> validReceivers = new ArrayList<>();

        queue.add(this.worldPosition);
        visited.add(this.worldPosition);

        while (!queue.isEmpty() && visited.size() <= MAX_NETWORK_SCAN) {
            BlockPos currentPos = queue.poll();

            for (Direction direction : Direction.values()) {
                BlockPos neighborPos = currentPos.relative(direction);
                if (!visited.add(neighborPos)) {
                    continue;
                }

                IEuStorage neighborStorage = this.level.getCapability(
                        ModCapabilities.EU_CAPABILITY, neighborPos, direction.getOpposite()
                );

                if (neighborStorage == null) {
                    continue;
                }

                if (this.level.getBlockEntity(neighborPos) instanceof CableBlockEntity) {
                    if (!neighborStorage.getTier().canAccept(tier)) {
                        if (!simulate) {
                            neighborStorage.receiveEnergy(amount, tier, false);
                        }
                    } else {
                        queue.add(neighborPos);
                    }
                } else if (neighborStorage.canReceive() && neighborStorage.receiveEnergy(1, tier, true) > 0) {
                    validReceivers.add(neighborStorage);
                }
            }
        }

        if (validReceivers.isEmpty()) {
            return 0;
        }

        long totalAccepted = 0;
        long remaining = amount;
        List<IEuStorage> activeReceivers = new ArrayList<>(validReceivers);

        while (remaining > 0 && !activeReceivers.isEmpty()) {
            int count = activeReceivers.size();
            long baseShare = remaining / count;
            long extra = remaining % count;
            List<IEuStorage> nextRound = new ArrayList<>();

            for (int i = 0; i < count; i++) {
                if (remaining <= 0) {
                    break;
                }

                IEuStorage receiver = activeReceivers.get(i);
                long toGive = baseShare + (i < extra ? 1L : 0L);
                if (toGive <= 0) {
                    continue;
                }

                long accepted = receiver.receiveEnergy(toGive, tier, simulate);
                if (accepted > 0) {
                    totalAccepted += accepted;
                    remaining -= accepted;
                    if (!simulate && accepted == toGive) {
                        nextRound.add(receiver);
                    }
                }
            }

            if (simulate || nextRound.isEmpty()) {
                break;
            }

            activeReceivers = nextRound;
        }

        return totalAccepted;
    }
}
