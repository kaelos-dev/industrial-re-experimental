package net.kaelos.icre.entity.block.base;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;

public interface ITickableBlockEntity {
    void serverTick(ServerLevel level, BlockPos pos, BlockState state);
}
