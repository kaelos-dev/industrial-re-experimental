package net.kaelos.icre.block.machine.producing;

import net.kaelos.icre.block.base.BaseEntityMachineBlock;
import net.kaelos.icre.entity.block.machine.producing.IronFurnaceBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.NonNull;

public class IronFurnaceBlock extends BaseEntityMachineBlock<IronFurnaceBlockEntity> {
    public IronFurnaceBlock(Properties properties) {
        super(properties, IronFurnaceBlockEntity::new);
    }

    @Override
    public void animateTick(@NonNull BlockState state, @NonNull Level level, @NonNull BlockPos pos, @NonNull RandomSource random) {
        if (!state.getValue(ACTIVE)) {
            return;
        }

        double x = pos.getX() + 0.5D;
        double y = pos.getY();
        double z = pos.getZ() + 0.5D;

        if (random.nextDouble() < 0.1D) {
            level.playLocalSound(x, y, z, SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
        }

        Direction facing = state.getValue(FACING);
        Direction.Axis axis = facing.getAxis();
        double offset = random.nextDouble() * 0.6D - 0.3D;
        double dx = axis == Direction.Axis.X ? facing.getStepX() * 0.52D : offset;
        double dy = random.nextDouble() * 6.0D / 16.0D;
        double dz = axis == Direction.Axis.Z ? facing.getStepZ() * 0.52D : offset;

        level.addParticle(ParticleTypes.SMOKE, x + dx, y + dy, z + dz, 0.0D, 0.0D, 0.0D);
        level.addParticle(ParticleTypes.FLAME, x + dx, y + dy, z + dz, 0.0D, 0.0D, 0.0D);
    }
}
