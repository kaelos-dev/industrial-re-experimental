package net.kaelos.icre.api.energy;

import net.minecraft.core.Direction;

import javax.annotation.Nullable;

public interface IEuHandlerProvider {
    @Nullable IEuStorage getEuStorage(@Nullable Direction side);
}
