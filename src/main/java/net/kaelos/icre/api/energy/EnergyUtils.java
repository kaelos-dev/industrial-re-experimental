package net.kaelos.icre.api.energy;

import net.kaelos.icre.init.ModCapabilities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;

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
}
