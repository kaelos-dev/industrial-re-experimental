package net.kaelos.icre.api.energy;

public interface IEuStorage {
    long receiveEnergy(long amount, VoltageTier tier, boolean simulate);

    long extractEnergy(long maxExtract, boolean simulate);

    long getEnergyStored();

    long getMaxEnergyStored();

    VoltageTier getTier();

    default boolean canReceive() {
        return true;
    }

    default boolean canExtract() {
        return true;
    }
}
