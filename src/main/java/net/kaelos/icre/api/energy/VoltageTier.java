package net.kaelos.icre.api.energy;

public enum VoltageTier {
    ULV(8),
    LV(32),
    MV(128),
    HV(512),
    EV(2048),
    IV(8192);

    private final int maxVoltage;

    VoltageTier(int maxVoltage) {
        this.maxVoltage = maxVoltage;
    }

    public int getMaxVoltage() {
        return maxVoltage;
    }

    public boolean canAccept(VoltageTier incoming) {
        return incoming.maxVoltage <= this.maxVoltage;
    }
}
