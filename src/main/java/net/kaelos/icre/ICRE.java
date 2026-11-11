package net.kaelos.icre;

import com.mojang.logging.LogUtils;
import net.kaelos.icre.definitions.ModBlocks;
import net.kaelos.icre.definitions.ModCreativeModeTabs;
import net.kaelos.icre.definitions.ModItems;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(ICRE.MOD_ID)
public class ICRE {
    public static final String MOD_ID = "icre";

    public static final Logger LOGGER = LogUtils.getLogger();

    public static Identifier makeId(String id) {
        return Identifier.fromNamespaceAndPath(MOD_ID, id);
    }

    public ICRE(final IEventBus eventBus) {
        ModItems.register(eventBus);
        ModCreativeModeTabs.register(eventBus);
        ModBlocks.register(eventBus);
    }
}
