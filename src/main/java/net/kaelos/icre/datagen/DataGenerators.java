package net.kaelos.icre.datagen;

import net.kaelos.icre.ICRE;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.List;
import java.util.Set;

@SuppressWarnings("unused")
@EventBusSubscriber(modid = ICRE.MOD_ID)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherClientData(GatherDataEvent.Client event) {
        event.createProvider(ModModelProvider::new);

        for (ModLanguageProvider.Locale locale : ModLanguageProvider.Locale.values()) {
            event.createProvider(output -> new ModLanguageProvider(output, locale));
        }
    }

    @SubscribeEvent
    public static void gatherServerData(GatherDataEvent.Server event) {
        event.createProvider(ModRecipeProvider.Runner::new);

        event.createProvider(((output, lookupProvider) -> new LootTableProvider(
                output,
                Set.of(),
                List.of(new LootTableProvider.SubProviderEntry(ModBlockLootSubProvider::new, LootContextParamSets.BLOCK)),
                lookupProvider
        )));

        event.createBlockAndItemTags(ModBlockTagsProvider::new, ModBlockTagsProvider.ModItemTagsProvider::new);
    }
}
