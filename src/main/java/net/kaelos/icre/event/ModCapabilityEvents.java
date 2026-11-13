package net.kaelos.icre.event;

import net.kaelos.icre.ICRE;
import net.kaelos.icre.api.energy.IEuHandlerProvider;
import net.kaelos.icre.definitions.ModBlockEntities;
import net.kaelos.icre.entity.block.base.IItemHandlerProvider;
import net.kaelos.icre.init.ModCapabilities;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

@SuppressWarnings("unused")
@EventBusSubscriber(modid = ICRE.MOD_ID)
public class ModCapabilityEvents {
    private ModCapabilityEvents() {}

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        for (var entry : ModBlockEntities.BLOCK_ENTITIES.getEntries()) {
            BlockEntityType<?> type = entry.get();

            event.registerBlockEntity(
                    ModCapabilities.EU_CAPABILITY,
                    type,
                    (be, side) -> be instanceof IEuHandlerProvider provider ? provider.getEuStorage(side) : null
            );

            event.registerBlockEntity(
                    Capabilities.Item.BLOCK,
                    type,
                    (be, side) -> be instanceof IItemHandlerProvider provider ? provider.getItemHandler(side) : null
            );
        }
    }
}
