package net.kaelos.icre.init;

import net.kaelos.icre.ICRE;
import net.kaelos.icre.api.energy.IEuStorage;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.ItemCapability;
import net.neoforged.neoforge.transfer.access.ItemAccess;

public class ModCapabilities {
    private ModCapabilities() {}

    public static final BlockCapability<IEuStorage, Direction> EU_CAPABILITY =
            BlockCapability.createSided(ICRE.makeId("eu_energy"), IEuStorage.class);

    public static final ItemCapability<IEuStorage, ItemAccess> EU_ITEM_CAPABILITY =
            ItemCapability.create(ICRE.makeId("eu_energy"), IEuStorage.class, ItemAccess.class);
}
