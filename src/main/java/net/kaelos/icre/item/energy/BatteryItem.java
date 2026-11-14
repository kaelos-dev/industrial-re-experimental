package net.kaelos.icre.item.energy;

import net.kaelos.icre.api.energy.VoltageTier;

public class BatteryItem extends BaseEnergyItem {
    public BatteryItem(Properties properties, long capacity, long maxTransfer, VoltageTier tier) {
        super(properties, capacity, maxTransfer, maxTransfer, tier);
    }
}
