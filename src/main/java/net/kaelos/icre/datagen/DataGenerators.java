package net.kaelos.icre.datagen;

import net.kaelos.icre.ICRE;
import net.kaelos.icre.datagen.loot.ModBlockLootSubProvider;
import net.kaelos.icre.datagen.models.ModModelProvider;
import net.kaelos.icre.datagen.tags.ModBiomeTagsProvider;
import net.kaelos.icre.datagen.tags.ModBlockTagsProvider;
import net.kaelos.icre.datagen.tags.ModItemTagsProvider;
import net.kaelos.icre.worldgen.ModWorldGen;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

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

        event.createProvider(ModRecipeProvider.Runner::new);

        event.createProvider(((output, lookupProvider) -> new LootTableProvider(
                output,
                Set.of(),
                List.of(new LootTableProvider.SubProviderEntry(ModBlockLootSubProvider::new, LootContextParamSets.BLOCK)),
                lookupProvider
        )));

        event.createBlockAndItemTags(ModBlockTagsProvider::new, ModItemTagsProvider::new);

        event.createDatapackRegistryObjects(new RegistrySetBuilder()
                .add(Registries.CONFIGURED_FEATURE, ModWorldGen::configured)
                .add(Registries.PLACED_FEATURE, ModWorldGen::placed)
                .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ModWorldGen::biomeModifiers));

        event.createProvider(ModBiomeTagsProvider::new);
    }
}
