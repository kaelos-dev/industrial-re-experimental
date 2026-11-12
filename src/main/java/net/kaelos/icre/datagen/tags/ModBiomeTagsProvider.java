package net.kaelos.icre.datagen.tags;

import net.kaelos.icre.ICRE;
import net.kaelos.icre.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.tags.BiomeTags;
import net.neoforged.neoforge.common.Tags;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

public class ModBiomeTagsProvider extends BiomeTagsProvider {
    public ModBiomeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, ICRE.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.@NonNull Provider registries) {
        this.tag(ModTags.Biomes.HAS_RUBBER_TREE)
                .addTag(BiomeTags.IS_FOREST)
                .addTag(BiomeTags.IS_JUNGLE)
                .addOptionalTag(Tags.Biomes.IS_SWAMP);
    }
}
